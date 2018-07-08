package levsha

import levsha.dsl.{SymbolDsl, XmlDsl}
import levsha.impl.{TextPrettyPrintingConfig, TextRenderContext}

/**
  * Default template context and dsl
 *
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object text {

  val symbolDsl = new SymbolDsl[Nothing]()

  val xmlDsl = new XmlDsl[Nothing]()

  /**
    * Provides Text render context to make html string
    * @example
    * {{{
    * val html = renderHtml {
    *   'div('class /= "title", "Hello, I'm Levsha!")
    * }
    * }}}
    */
  def renderHtml(f: Document.Node[Nothing],
                 prettyPrinting: TextPrettyPrintingConfig = TextPrettyPrintingConfig.default): String = {
    val renderContext = new TextRenderContext(prettyPrinting)
    f(renderContext)
    renderContext.mkString
  }
}
