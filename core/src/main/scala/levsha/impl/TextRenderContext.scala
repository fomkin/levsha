package levsha.impl

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
final class TextRenderContext(val prettyPrinting: TextPrettyPrintingConfig = TextPrettyPrintingConfig.default)
    extends AbstractTextRenderContext[Nothing]
