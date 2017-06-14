package levsha

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._

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

  private def transformContent(content: Tree): Seq[Tree] = content match {
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
    case Typed(q"levsha.RenderUnit.Node.apply[$t] { rc => ..$ops }", _) =>
      ops flatMap {
        case q"rc.openNode($name)" => Seq(q"rc.openNode($name)")
        case q"rc.closeNode($name)" => Seq(q"rc.closeNode($name)")
        case q"rc.setAttr($name, $value)" => Seq(q"rc.setAttr($name, $value)")
        case q"rc.addTextNode($text)" => Seq(q"rc.addTextNode($text)")
        case q"rc.addMisc($misc)" => Seq(q"rc.addMisc($misc)")
        case op => transformContent(op)
      }
    case Typed(q"levsha.RenderUnit.Attr.apply[$t] { rc => rc.setAttr($k, $v) }", _) =>
      Seq(q"rc.setAttr($k, $v)")
    case tree if tree.tpe <:< typeOf[RenderUnit[_]] =>
      Seq(q"$tree(rc)")
  }
  
  def node(children: Tree*): Tree = {
    val q"$conv($in)" = c.prefix.tree
    val nodeName = toKebab(in)
    val preparedChildren = children.flatMap(transformContent)
    val result = q"""
      levsha.RenderUnit.Node.apply[RC] { rc =>
        rc.openNode($nodeName)
        ..$preparedChildren
        rc.closeNode($nodeName)
      }
    """
    println(result)
    result
  }

  def attr(value: Tree): Tree = {
    val q"$conv($in)" = c.prefix.tree
    val attr = toKebab(in)
    q"""
      levsha.RenderUnit.Attr.apply[RC] { rc =>
        rc.setAttr($attr, $value)
      }
    """
  }

  //  def enableAttr(s: Tree)(rc: Tree): Tree = {
//    val attr = toKebab(s)
//    q"""$rc.setAttr($attr, "")"""
//  }

  def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }
}
