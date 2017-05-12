package levsha.impl

import levsha.RenderContext
import levsha.RenderUnit.{Attr, Misc, Node, Text}
import levsha.impl.internal.Op._

/**
  * Generates HTML
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
final class TextRenderContext extends RenderContext[Nothing] {

  private val builder = StringBuilder.newBuilder
  private var lastOp = OpClose

  def openNode(name: String): Unit = {
    if (lastOp != OpClose) builder.append('>')
    builder.append('<')
    builder.append(name)
    lastOp = OpOpen
  }

  def closeNode(name: String): Node.type = {
    if (lastOp == OpAttr) builder.append('>')
    builder.append('<')
    builder.append('/')
    builder.append(name)
    builder.append('>')
    lastOp = OpClose
    Node
  }

  def setAttr(name: String, value: String): Attr.type = {
    builder.append(' ')
    builder.append(name)
    builder.append('=')
    builder.append('"')
    builder.append(value)
    builder.append('"')
    lastOp = OpAttr
    Attr
  }

  def addTextNode(text: String): Text.type = {
    if (lastOp != OpClose) builder.append('>')
    builder.append(text)
    lastOp = OpText
    Text
  }

  def addMisc(misc: Nothing): Misc.type = Misc

  /** Creates string from buffer */
  def mkString: String = builder.mkString
}
