package levsha

/**
  * Default template context and dsl
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object default {

  val dsl = new TemplateDsl[Nothing](new TemplateContext[Nothing]())

  /**
    * Provides Text render context to make html string
    * @example
    * {{{
    * val html = renderHtml { implicit rc =>
    *   'div('class /= "title", "Hello, I'm Levsha!")
    * }
    * }}}
    */
  def renderHtml(f: dsl.templateContext.RenderContext => dsl.templateContext.Node.type): String = {
    val renderContext = new dsl.templateContext.TextRenderContext()
    f(renderContext)
    renderContext.mkString
  }
}
