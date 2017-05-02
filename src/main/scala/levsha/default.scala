package levsha

import levsha.impl.TextRenderContext

/**
  * Default template context and dsl
 *
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object default {

  val dsl = new TemplateDsl[Nothing]()

  /**
    * Provides Text render context to make html string
    * @example
    * {{{
    * val html = renderHtml { implicit rc =>
    *   'div('class /= "title", "Hello, I'm Levsha!")
    * }
    * }}}
    */
  def renderHtml(f: RenderContext[Nothing] => RenderUnit.Node): String = {
    val renderContext = new TextRenderContext()
    f(renderContext)
    renderContext.mkString
  }
}
