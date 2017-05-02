package levsha.impl

import levsha.RenderContext
import levsha.RenderUnit.{Attr, Misc, Node, Text}

/**
  * Generates HTML
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
final class TextRenderContext extends RenderContext[Nothing] {

  private val builder = StringBuilder.newBuilder
  private var attrsStarted = false

  def openNode(name: String): Unit = {
    if (attrsStarted) builder.append('>')
    builder.append('<')
    builder.append(name)
    attrsStarted = true
  }

  def closeNode(name: String): Node.type = {
    builder.append('<')
    builder.append('/')
    builder.append(name)
    builder.append('>')
    attrsStarted = false
    Node
  }

  def setAttr(name: String, value: String): Attr.type = {
    builder.append(' ')
    builder.append(name)
    builder.append('=')
    builder.append('"')
    builder.append(value)
    builder.append('"')
    Attr
  }

  def addTextNode(text: String): Text.type = {
    if (attrsStarted) builder.append('>')
    builder.append(text)
    Text
  }

  def addMisc(misc: Nothing): Misc.type = Misc

  /** Creates string from buffer */
  def mkString: String = builder.mkString
}
