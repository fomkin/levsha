package levsha

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._

  def node[MT: WeakTypeTag](children: Tree*): Tree = {

    // 1. Moves nested templates to current scope
    // 2. Simplifies implicit wrappers (stringToNode, miscToNode)
    def transformContent(content: Tree): Seq[Tree] = content match {
      case Apply(Select(_, TermName("stringToNode")), value :: Nil) => Seq(q"rc.addTextNode($value)")
      case Apply(Select(_, TermName("miscToNode")), value :: Nil) => Seq(q"rc.addMisc($value)")
      case Typed(q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($k, $v) }", _) => Seq(q"rc.setAttr($k, $v)")
      case Typed(q"levsha.Document.Node.apply[$t] { rc => ..$ops }", _) =>
        // Partial workaround for situation
        // https://stackoverflow.com/questions/11208790/how-can-i-reuse-definition-ast-subtrees-in-a-macro
        ops flatMap {
          case q"rc.openNode($name)" => Seq(q"rc.openNode($name)")
          case q"rc.closeNode($name)" => Seq(q"rc.closeNode($name)")
          case q"rc.setAttr($name, $value)" => Seq(q"rc.setAttr($name, $value)")
          case q"rc.addTextNode($text)" => Seq(q"rc.addTextNode($text)")
          case q"rc.addMisc($misc)" => Seq(q"rc.addMisc($misc)")
          case q"($expr).apply($rc)" => Seq(q"($expr).apply(rc)")
          case op if op.tpe <:< weakTypeOf[Document[MT]] => transformContent(op)
        }
      case tree if tree.tpe <:< weakTypeOf[Document[MT]] => Seq(q"($tree).apply(rc)")
    }

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
      // Hope https://issues.scala-lang.org/browse/SI-5464 will not touch this
      val tree = c.typecheck(c.untypecheck(child))
      if (tree.tpe =:= weakTypeOf[Document[MT]])
        c.error(tree.pos, "Should be node, attribute or void")
      tree
    }

    val MT = weakTypeOf[MT]
    val q"$conv($in)" = c.prefix.tree
    val nodeName = toKebab(in)

    val preparedChildren = trees
      // Attributes always on top
      .sortBy(_.tpe) { (x, y) =>
        if (x =:= weakTypeOf[Document.Attr[MT]]) -1
        else 0
      }
      .flatMap(transformContent)

    q"""
      levsha.Document.Node.apply[$MT] { rc =>
        rc.openNode($nodeName)
        ..$preparedChildren
        rc.closeNode($nodeName)
      }
    """
  }

  def attr[MT: WeakTypeTag](value: Tree): Tree = {
    val MT = weakTypeOf[MT]
    val q"$conv($in)" = c.prefix.tree
    val attr = toKebab(in)

    q"""
      levsha.Document.Attr.apply[$MT] { rc =>
        rc.setAttr($attr, $value)
      }
    """
  }

  // Converts symbol 'camelCase to "kebab-case"
  private def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }
}
