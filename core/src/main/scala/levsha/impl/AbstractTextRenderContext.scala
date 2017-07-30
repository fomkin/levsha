package levsha.impl

import levsha.{RenderContext, XmlNs}
import levsha.impl.internal.Op._

/**
  * Generates HTML
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
abstract class AbstractTextRenderContext[MiscType] extends RenderContext[MiscType] {

  def prettyPrinting: TextPrettyPrintingConfig

  private var lastOp = OpClose
  private var indentation = 0

  val builder = StringBuilder.newBuilder

  private def addIndentation() = {
    var i = 0
    while (i < indentation) {
      var j = 0
      while (j < prettyPrinting.indentationSize) {
        builder.append(prettyPrinting.indentationChar)
        j += 1
      }
      i += 1
    }
  }

  def openNode(xmlns: XmlNs, name: String): Unit = {
    if (lastOp != OpClose && lastOp != OpText) {
      builder.append('>')
      builder.append(prettyPrinting.lineBreak)
    }
    addIndentation()
    builder.append('<')
    builder.append(name)
    lastOp = OpOpen
    indentation += 1
  }

  def closeNode(name: String): Unit = {
    indentation -= 1
    if (lastOp == OpAttr || lastOp == OpOpen) {
      builder.append('>')
    } else {
      addIndentation()
    }
    builder.append('<')
    builder.append('/')
    builder.append(name)
    builder.append('>')
    builder.append(prettyPrinting.lineBreak)
    lastOp = OpClose
  }

  def setAttr(name: String, xmlNs: XmlNs, value: String): Unit = {
    builder.append(' ')
    builder.append(name)
    builder.append('=')
    builder.append('"')
    builder.append(value)
    builder.append('"')
    lastOp = OpAttr
  }

  def addTextNode(text: String): Unit = {
    if (lastOp != OpClose && lastOp != OpText) {
      builder.append('>')
      builder.append(prettyPrinting.lineBreak)
    }
    addIndentation()
    builder.append(text)
    builder.append(prettyPrinting.lineBreak)
    lastOp = OpText
  }

  def addMisc(misc: MiscType): Unit = {}

  /** Creates string from buffer */
  def mkString: String = builder.mkString
}
