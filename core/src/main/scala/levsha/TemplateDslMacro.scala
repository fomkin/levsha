package levsha

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._

  def node[MT: WeakTypeTag](children: Tree*): Tree = {

    def optimize(tree: Tree): Tree = tree match {
      // Basic optimizations
      case Block(body, extractOpenNode(xs)) => q"..$body; ..$xs"
      case Select(_, TermName("void")) => EmptyTree
      case Typed(q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($ns, $k, $v) }", _) => q"rc.setAttr($ns, $k, $v)"
      case extractOpenNode(xs) => q"..$xs"
      case extractConverter("stringToNode", value) => q"rc.addTextNode($value)"
      case extractConverter("miscToNode", value) => q"rc.addMisc($value)"
      // Advanced optimizations
      case extractConverter("seqToNode", q"($seq).map[$b, $that](${f: Function})($bf)") =>
        q"""
          val $$iter = $seq.iterator
          while ($$iter.hasNext) {
            val ${f.vparams.head.name} = $$iter.next()
            ${optimize(f.body)}
          }
        """
      case extractConverter("optionToNode" | "optionToAttr", q"($opt).map[$b](${f: Function})") =>
        q"""
          val $$opt = $opt
          if ($$opt.nonEmpty) {
            val ${f.vparams.head.name} = $$opt.get
            ${optimize(f.body)}
          }
        """
      // Control flow optimization
      case q"if ($cond) ${left: Tree} else ${right: Tree}" =>
        q"if ($cond) ${optimize(left)} else ${optimize(right)}"
      case q"$skip match { case ..$cases }" =>
        val optimizedCases = cases map {
          case cq"$p => ${b: Tree}" => cq"$p => ${optimize(b)}"
          case cq"$p if $c => ${b: Tree}" => cq"$p if $c => ${optimize(b)}"
        }
        q"$skip match { case ..$optimizedCases }"
      // Can't optimize
      case _ =>
        if (notOptimizedWarningsEnabled)
          c.warning(tree.pos, "Can't optimize")
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
          case (tree, _) =>
            Seq(optimize(identCleaner.transform(tree)))
        }
    }

    val MT = weakTypeOf[MT]
    val (nodeXmlNs, nodeName) = unfoldQualifiedName(c.prefix.tree)

    q"""
      levsha.Document.Node.apply[$MT] { rc =>
        rc.openNode($nodeXmlNs, $nodeName)
        ..$ops
        rc.closeNode($nodeName)
      }
    """
  }

  def attr[MT: WeakTypeTag](value: Tree): Tree = {
    val MT = weakTypeOf[MT]
    val (xmlNs, attr) = unfoldQualifiedName(c.prefix.tree)

    q"""
      levsha.Document.Attr.apply[$MT] { rc =>
        rc.setAttr($xmlNs, $attr, $value)
      }
    """
  }

  def xmlNsCreateQualifiedName(symbol: Tree): Tree = {
    q"levsha.QualifiedName(${c.prefix.tree}, $symbol)"
  }

  // Utils

  private def unfoldQualifiedName(tree: Tree): (Tree, String) = tree match {
    case Apply(Select(_, TermName("QualifiedNameOps")), Typed(Apply(_, List(xmlNs, rawName)), _) :: Nil) =>
      (xmlNs, toKebab(rawName))
    case expr @ q"$conv(${rawName: Tree})" =>
      (q"levsha.XmlNs.html", toKebab(rawName))
  }

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
      case Typed(q"levsha.Document.Node.apply[$t] { rc => ..${ops: Seq[Tree]} }", _) => Some(ops)
      case _ => None
    }
  }

  // Misc

  private val notOptimizedWarningsEnabled = {
    val propName = "levsha.macros.notOptimizedWarnings"
    sys.props.get(propName)
      .orElse(sys.env.get(propName))
      .fold(false)(x => if (x == "true") true else false)
  }
}
