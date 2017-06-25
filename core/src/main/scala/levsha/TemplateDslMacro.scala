package levsha

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._

  def node[RC: WeakTypeTag](children: Tree*): Tree = {
    // Check one of children has ambiguous Document type
    // Children should be Attr, Node or Empty
    val trees = children map { child =>
      // I've encountered with strange behavior in macros.
      // When I give if expression to def macro,
      // 1) Macro invokes twice
      // 2) On first invoke if expression has
      //    unexpected type: if (true) A(1) else A(2) has type X[Int] but I expect A[Int]
      //    where sealed trait X[-T]; case class A[-T](v: T) extends X[T].
      // Reproduced in 2.11.11 and 2.12.2
      val tree = c.typecheck(c.untypecheck(child))
      if (tree.tpe =:= weakTypeOf[Document[RC]])
        c.error(tree.pos, "Should be node, attribute or void")
      tree
    }

    val RC = weakTypeOf[RC]
    val q"$conv($in)" = c.prefix.tree
    val nodeName = toKebab(in)

    val preparedChildren = trees
      .sortBy(_.tpe)(nodeChildrenOrdering[RC])
      .flatMap(transformContent)

    q"""
      levsha.Document.Node.apply[$RC] { rc =>
        rc.openNode($nodeName)
        ..$preparedChildren
        rc.closeNode($nodeName)
      }
    """
  }

  def attr[RC: WeakTypeTag](value: Tree): Tree = {
    val RC = weakTypeOf[RC]
    val q"$conv($in)" = c.prefix.tree
    val attr = toKebab(in)

    q"""
      levsha.Document.Attr.apply[$RC] { rc =>
        rc.setAttr($attr, $value)
      }
    """
  }

  trait MethodExtractor {
    def unapply(arg: Tree): Option[Tree]
  }

  private def matchDslImplicit(name: String) = new MethodExtractor {
    def unapply(arg: Tree): Option[Tree] = arg match {
      case Apply(Select(_, TermName(`name`)), value :: Nil) => Some(value)
      case _ => None
    }
  }

  private val stringToNode = matchDslImplicit("stringToNode")
  private val miscToNode = matchDslImplicit("miscToNode")
  private val seqToNode = matchDslImplicit("seqToNode")
  private val optionToNode = matchDslImplicit("optionToNode")

  def nodeChildrenOrdering[T: WeakTypeTag] = new Ordering[Type] {
    def compare(x: Type, y: Type): Int = (x, y) match {
      case (a, _) if a =:= weakTypeOf[Document.Attr[T]] => -1
      case _ => 0
    }
  }

  private def transformContent[RC: WeakTypeTag](content: Tree): Seq[Tree] = content match {
    case stringToNode(value) => Seq(q"rc.addTextNode($value)")
    case miscToNode(value) => Seq(q"rc.addMisc($value)")
    case optionToNode(value) =>
      val result =
        q"""{
         val valueOpt = $value
         if (valueOpt.nonEmpty) valueOpt.get(rc)
        }"""
      Seq(result)
    case seqToNode(value) =>
      val result =
        q""" {
         val iterator = $value.iterator
         while (iterator.hasNext) {
           val node = iterator.next
           node(rc)
         }
       }
       """
      Seq(result)
    case Typed(q"levsha.Document.Node.apply[$t] { rc => ..$ops }", _) =>
      ops flatMap {
        case q"rc.openNode($name)" => Seq(q"rc.openNode($name)")
        case q"rc.closeNode($name)" => Seq(q"rc.closeNode($name)")
        case q"rc.setAttr($name, $value)" => Seq(q"rc.setAttr($name, $value)")
        case q"rc.addTextNode($text)" => Seq(q"rc.addTextNode($text)")
        case q"rc.addMisc($misc)" => Seq(q"rc.addMisc($misc)")
        case op => transformContent(op)
      }
    case Typed(q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($k, $v) }", _) =>
      Seq(q"rc.setAttr($k, $v)")
    case tree if tree.tpe <:< weakTypeOf[Document[RC]] =>
      Seq(q"$tree(rc)")
  }

  private def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }
}
