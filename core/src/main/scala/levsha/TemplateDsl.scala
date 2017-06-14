package levsha

import scala.annotation.compileTimeOnly
import scala.language.experimental.macros
import scala.language.implicitConversions

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class TemplateDsl[MiscType] {

  import RenderUnit._
  
  type RC = RenderContext[MiscType]

//  /** Converts symbol to attribute without value
//    * @example 'button('disabled, "Press me")
//    */
//  @inline implicit def symbolToAttr(s: Symbol)(implicit rc: RC): Attr.type =
//    macro TemplateDslMacro.enableAttr

  /** Converts String to text document node */
  implicit def stringToNode(value: String): Node[RC] =
    Node { rc => rc.addTextNode(value) }

  /** Converts MiscType to text document node */
  implicit def miscToNode(value: MiscType): Node[RC] =
    Node { rc => rc.addMisc(value) }

  /** Converts iterable of templates to document fragment */
  implicit def seqToNode(xs: Iterable[Node[RC]]): Node[RC] =
    Node(rc => xs.foreach(f => f(rc))) // Apply render context to elements

  /** Converts sequence of T (which can be converted to Node) to document fragment */
  implicit def arbitrarySeqToNode[T](xs: Iterable[T])(implicit ev: T => Node[RC]): Node[RC] =
    Node(rc => xs.foreach(f => ev(f).apply(rc)))

  /** Implicitly unwraps optional documents */
  implicit def optionToNode(value: Option[Node[RC]]): Node[RC] =
    Node(rc => if (value.nonEmpty) value.get(rc))

  /** Converts option of T (which can be converted to Node) to document node */
  implicit def arbitraryOptionToNode[T](value: Option[T])(implicit ev: T => Node[RC]): Node[RC] =
    Node(rc => if (value.nonEmpty) ev(value.get).apply(rc))

  /** Symbol based DSL allows to define documents
    * {{{
    *   'body(
    *     'h1(class /= "title")("Hello World"),
    *     'p("Lorem ipsum dolor")
    *   )
    * }}}
    */
  implicit final class SymbolOps(s: Symbol) {
    def apply[RC](children: RenderUnit[RC]*): Node[RC] =
      macro TemplateDslMacro.node

    def /=(value: String): Attr[RC] =
      macro TemplateDslMacro.attr
  }

  @deprecated("Use void instead of <>", since = "0.4.0")
  val <> = Empty

  val void = Empty
}
