package levsha

import java.util.UUID

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import macrocompat.bundle

import scala.collection.concurrent.TrieMap

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
@bundle class TemplateDslMacro(val c: blackbox.Context) {

  import c.universe._

  import TemplateDslMacroHelper.Op
  import Op._

  def defaultNode[MT: WeakTypeTag](children: Tree*): Tree = {
    node()(children:_*)
  }

  def node[MT: WeakTypeTag](specials: Tree*)(children: Tree*): Tree = {

    // Hack. We need to exchange trees between different
    // macro expansion.
    val savedOps = TemplateDslMacroHelper.savedOps
      .asInstanceOf[TrieMap[String, Seq[Op[Tree]]]]

    val MT = weakTypeOf[MT]
    val q"$conv($in)" = c.prefix.tree
    val nodeName = toKebab(in)
    val id = UUID.randomUUID.toString

    val xmlNs = specials
      .collectFirst { case special if special.tpe =:= typeOf[levsha.XmlNs] => special }
      .getOrElse(q"levsha.XmlNs.html")

    val ops = {
      val innerOps = children
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
          case (Apply(Select(_, TermName("stringToNode")), value :: Nil), _) => Seq(addTextNode(value))
          case (Apply(Select(_, TermName("miscToNode")), value :: Nil), _) => Seq(addMisc(value))
          case (Typed(q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($k, $v) }", _), _) => Seq(setAttr(k, v))
          case (Typed(q"levsha.Document.Node.apply[$t] { rc => ..$ops }", _), _) =>
            val q"levsha.TemplateDslMacroHelper.id(${id: String})" :: _ = ops
            savedOps.remove(id).getOrElse(
              c.abort(c.enclosingPosition, s"Unexpected macro expansion order. $id not found"))
          case (utc, tc) if tc.tpe <:< weakTypeOf[Document[MT]] =>
            Seq(applyRc(utc))
        }
      openNode(nodeName, xmlNs) +: innerOps :+ closeNode(nodeName)
    }

    val opsCode = ops map {
      case openNode(name, xmlNs) => q"rc.openNode($name, $xmlNs)"
      case closeNode(name) => q"rc.closeNode($name)"
      case setAttr(k, v) => q"rc.setAttr($k, $v)"
      case addTextNode(text) => q"rc.addTextNode($text)"
      case addMisc(misc) => q"rc.addMisc($misc)"
      case applyRc(tree) => q"(${tree.duplicate}).apply(rc)"
    }

    savedOps.put(id, ops)

    q"""
      levsha.Document.Node.apply[$MT] { rc =>
        levsha.TemplateDslMacroHelper.id($id)
        ..$opsCode
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

object TemplateDslMacroHelper {

  // This method used by node simplifier macro.
  def id(x: String): Unit = ()

  // Cache of operations corresponding to node.
  // Trees should be untypechecked
  lazy val savedOps = TrieMap.empty[String, Seq[Op[_]]]

  sealed trait Op[+Tree]

  object Op {
    case class openNode[Tree](name: String, xmlNs: Tree) extends Op[Tree]
    case class closeNode(name: String) extends Op[Nothing]
    case class setAttr[Tree](name: Tree, value: Tree) extends Op[Tree]
    case class addTextNode[Tree](text: Tree) extends Op[Tree]
    case class addMisc[Tree](misc: Tree) extends Op[Tree]

    case class applyRc[Tree](tree: Tree) extends Op[Tree]
  }
}
