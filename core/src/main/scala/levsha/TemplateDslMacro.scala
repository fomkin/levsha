package levsha

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._

  def node(children: Tree*)(rc: Tree): Tree = {
    val q"$conv($in)" = c.prefix.tree
    val nodeName = toKebab(in)
    val sortedChildren = children
      .sortBy(_.tpe)(nodeChildrenOrdering)
    q"""
      $rc.openNode($nodeName)
      ..$sortedChildren
      $rc.closeNode($nodeName)
    """
  }

  def attr(value: Tree)(rc: Tree): Tree = {
    val q"$conv($in)" = c.prefix.tree
    val attr = toKebab(in)
    q"$rc.setAttr($attr, $value)"
  }

  def enableAttr(s: Tree)(rc: Tree): Tree = {
    val attr = toKebab(s)
    q"""$rc.setAttr($attr, "")"""
  }

  def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }

  val nodeChildrenOrdering = new Ordering[Type] {
    def compare(x: Type, y: Type): Int = (x, y) match {
      case (a, b) if a =:= typeOf[RenderUnit.Attr.type] => -1
      case _ => 0
    }
  }
}
