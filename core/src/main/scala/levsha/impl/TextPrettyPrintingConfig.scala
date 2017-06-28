package levsha.impl

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
case class TextPrettyPrintingConfig(
  indentationChar: Char,
  indentationSize: Int,
  lineBreak: CharSequence
)

object TextPrettyPrintingConfig {
  val default = TextPrettyPrintingConfig(' ', 2, "\n")
  val noPrettyPrinting = TextPrettyPrintingConfig(' ', 0, "")
}
