package levsha

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._
  
  def unfoldQualifiedName(tree: Tree): (Tree, String) = tree match {
    case Apply(Select(_, TermName("QualifiedNameOps")), Typed(Apply(_, List(xmlNs, rawName)), _) :: Nil) =>
      (xmlNs, toKebab(rawName))
    case expr @ q"$conv(${rawName: Tree})" =>
      (q"levsha.XmlNs.html", toKebab(rawName))
  }

  def node[MT: WeakTypeTag](children: Tree*): Tree = {

    def optimize(tree: Tree): Tree = tree match {
      case Block(body, extractOpenNode(xs)) => q"..$body; ..$xs"
      case Select(_, TermName("void")) => EmptyTree
      case extractOpenNode(xs) => q"..$xs"
      case extractConverter("stringToNode", value) => q"rc.addTextNode($value)"
      case extractConverter("miscToNode", value) => q"rc.addMisc($value)"
      case Typed(q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($k, $ns, $v) }", _) => q"rc.setAttr($k, $ns, $v)"
      case extractConverter("seqToNode", q"($seq).map[$b, $that](${f: Function})($bf)") =>
        q"""
          val i = $seq.iterator
          while (i.hasNext) {
            val ${f.vparams.head.name} = i.next()
            ${optimize(f.body)}
          }
        """
      case q"if ($cond) ${left: Tree} else ${right: Tree}" =>
        q"if ($cond) ${optimize(left)} else ${optimize(right)}"
      case q"$skip match { case ..$cases }" =>
        val optimizedCases = cases map {
          case cq"$p => ${b: Tree}" => cq"$p => ${optimize(b)}"
        }
        q"$skip match { case ..$optimizedCases }"
      case _ =>
        println(showRaw(tree))
        q"$tree.apply(rc)"
    }

    val ops = {
      children
        // Check one of children has ambiguous Document type
        // Children should be Attr, Node or Empty
        .map { child =>
          // I've encountered with strange behavior in macros.
          // When I give if expression to def macro,
          // 1) Macro invokes twice
          // 2) On first invoke if expression has
          //    unexpected type: if (true) A(1) else A(2) has type X[Int] but I expect A[Int]
          //    where sealed trait X[-T]; case class A[-T](v: T) extends X[T].
          // Reproduced in 2.11.11 and 2.12.2
          // Hope https://issues.scala-lang.org/browse/SI-5464 will not touch this
          val utc = c.untypecheck(child)
          val tc = c.typecheck(utc)
          if (tc.tpe =:= weakTypeOf[Document[MT]])
            c.error(tc.pos, "Should be node, attribute or void")
          // Save both untypechecked tree and typechecked tree.
          // Untypechecked tree can be saved for future use
          // Typechecked tree will be used right no to decide
          // how not transform the tree.
          (utc, tc)
        }
        // Attributes always on top
        .sortBy(_._2.tpe) { (x, y) =>
          if (x =:= weakTypeOf[Document.Attr[MT]]) -1
          else 0
        }
        .flatMap {
          case (tree, _) => Seq(optimize(identCleaner.transform(tree)))
        }
    }

    val MT = weakTypeOf[MT]
    val (nodeXmlNs, nodeName) = unfoldQualifiedName(c.prefix.tree)
    val code = q"""
      levsha.Document.Node.apply[$MT] { rc =>
        rc.openNode($nodeXmlNs, $nodeName)
        ..$ops
        rc.closeNode($nodeName)
      }
    """
    println(code)
    code
  }

  def attr[MT: WeakTypeTag](value: Tree): Tree = {
    val MT = weakTypeOf[MT]
    val (xmlNs, attr) = unfoldQualifiedName(c.prefix.tree)

    q"""
      levsha.Document.Attr.apply[$MT] { rc =>
        rc.setAttr($attr, $xmlNs, $value)
      }
    """
  }

  def xmlNsCreateQualifiedName(symbol: Tree): Tree = {
    q"levsha.QualifiedName(${c.prefix.tree}, $symbol)"
  }

  // Utils

  /**  Converts symbol 'camelCase to "kebab-case" */
  private def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }

  /** Remove context binding from idents */
  private object identCleaner extends Transformer {
    override def transform(tree: Tree): Tree = {
      tree match {
        case Ident(TermName(name)) => Ident(TermName(name))
        case _ => super.transform(tree)
      }
    }
  }

  private object extractConverter {
    def unapply(tree: Tree): Option[(String, Tree)] = tree match {
      case Apply(Select(_, TermName(fun)), value :: Nil) => Some((fun, value))
      case _ => None
    }
  }

  private object extractOpenNode {
    def unapply(tree: Tree): Option[Seq[Tree]] = tree match {
      case Typed(q"levsha.Document.Node.apply[$t] { rc => ..$ops }", _) => Some(ops)
      case _ => None
    }
  }
}
