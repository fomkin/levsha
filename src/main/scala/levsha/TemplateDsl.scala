package levsha

import scala.language.experimental.macros
import scala.language.implicitConversions

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class TemplateDsl[MiscType](val templateContext: TemplateContext[MiscType]) {

  import templateContext._

  /** Converts () to empty template */
  @inline implicit def unit(value: Unit): RenderUnit = Empty

  /** Converts [[String]] to text document node */
  @inline implicit def text(value: String)(implicit rc: RenderContext): Text.type =
    rc.addTextNode(value)

  /** Converts [[MiscType]] to text document node */
  @inline implicit def misc(value: MiscType)(implicit rc: RenderContext): Misc.type =
    rc.addMisc(value)

  /** Converts iterable of templates to document fragment */
  @inline implicit def seq(xs: Iterable[NodeLike]): RenderUnit = Node

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
    def apply(children: RenderUnit*)(implicit rc: RenderContext): Node.type =
      macro TemplateMacro.node

    def /=(value: String)(implicit rc: RenderContext): Attr.type =
      macro TemplateMacro.attr
  }

  @deprecated("Use () instead of <>", since = "0.4.0")
  val <> = Empty
}
