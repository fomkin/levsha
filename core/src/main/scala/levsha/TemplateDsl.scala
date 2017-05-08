package levsha

import scala.language.experimental.macros
import scala.language.implicitConversions

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class TemplateDsl[MiscType] {

  import RenderUnit._
  
  type RC = RenderContext[MiscType]
  
  /** Converts () to empty template */
  @inline implicit def unitToEmpty(value: Unit): RenderUnit = Empty

  /** Converts [[String]] to text document node */
  @inline implicit def stringToText(value: String)(implicit rc: RC): Text.type =
    rc.addTextNode(value)

  /** Converts [[MiscType]] to text document node */
  @inline implicit def miscWrapper(value: MiscType)(implicit rc: RC): Misc.type =
    rc.addMisc(value)

  /** Converts iterable of templates to document fragment */
  @inline implicit def seqToNodeFragment(xs: Iterable[NodeLike]): RenderUnit = Node

  /** Implicitly unwraps optional documents */
  @inline implicit def unwrapOption(templateOpt: Option[RenderUnit]): RenderUnit = Node

  /** Symbol based DSL allows to define documents
    * {{{
    *   'body(
    *     'h1(class /= "title", "Hello World"),
    *     'p("Lorem ipsum dolor")
    *   )
    * }}}
    */
  implicit final class SymbolOps(s: Symbol) {
    def apply(children: RenderUnit*)(implicit rc: RC): Node.type =
      macro TemplateDslMacro.node

    def /=(value: String)(implicit rc: RC): Attr.type =
      macro TemplateDslMacro.attr
  }

  @deprecated("Use () instead of <>", since = "0.4.0")
  val <> = Empty
}
