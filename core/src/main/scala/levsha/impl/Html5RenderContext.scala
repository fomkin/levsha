package levsha.impl

import levsha.impl.internal.Op._

/**
  * Generates HTML5 output.
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class Html5RenderContext[MiscType](prettyPrinting: TextPrettyPrintingConfig)
  extends XhtmlRenderContext[MiscType](prettyPrinting) {

  import Html5RenderContext._

  override def closeNode(name: String): Unit = {
    indentation -= 1

    val selfClosing = SelfClosingTags.contains(name)

    if (!selfClosing && (lastOp == OpAttr || lastOp == OpOpen)) {
      builder.append('>')
    } else if (lastOp == OpStyle) {
      builder.append(if (selfClosing) "\"" else "\">")
    } else {
      addIndentation()
    }

    if (selfClosing) {
      builder.append('/')
      builder.append('>')
    } else {
      builder.append('<')
      builder.append('/')
      builder.append(name)
      builder.append('>')
    }

    builder.append(prettyPrinting.lineBreak)
    lastOp = OpClose
  }

}

private[impl] object Html5RenderContext {

  private val SelfClosingTags: Set[String] =
    Set(
      "area",
      "br",
      "col",
      "embed",
      "hr",
      "img",
      "input",
      "link",
      "meta",
      "param",
      "source",
      "track",
      "wbr"
    )

}
