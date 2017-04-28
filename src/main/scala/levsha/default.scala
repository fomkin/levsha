package levsha

/**
  * Default template context and dsl
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object default {

  val context = new TemplateContext[Nothing]()

  val dsl = new TemplateDsl[Nothing](context)

  /**
    * Provides Text render context to make html string
    * @example
    * {{{
    * val html = renderHtml { implicit rc =>
    *   'div('class /= "title", "Hello, I'm Levsha!")
    * }
    * }}}
    */
  def renderHtml(f: context.RenderContext => context.Node.type): String = {
    val renderContext = new context.TextRenderContext()
    f(renderContext)
    renderContext.mkString
  }
}
