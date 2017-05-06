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
    val tag = toKebab(in)
    q"""
      $rc.openNode($tag)
      ..$children
      $rc.closeNode($tag)
    """
  }

  def attr(value: Tree)(rc: Tree): Tree = {
    val q"$conv($in)" = c.prefix.tree
    val attr = toKebab(in)
    q"$rc.setAttr($attr, $value)"
  }

  def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }
}
