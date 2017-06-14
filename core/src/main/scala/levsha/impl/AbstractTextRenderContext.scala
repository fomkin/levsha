package levsha.impl

import levsha.RenderContext
import levsha.impl.internal.Op._

/**
  * Generates HTML
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
abstract class AbstractTextRenderContext[MiscType] extends RenderContext[MiscType] {

  private var lastOp = OpClose

  val builder = StringBuilder.newBuilder

  def openNode(name: String): Unit = {
    if (lastOp != OpClose && lastOp != OpText) builder.append('>')
    builder.append('<')
    builder.append(name)
    lastOp = OpOpen
  }

  def closeNode(name: String): Unit = {
    if (lastOp == OpAttr || lastOp == OpOpen) {
      builder.append('/')
      builder.append('>')
    } else {
      builder.append('<')
      builder.append('/')
      builder.append(name)
      builder.append('>')
    }
    lastOp = OpClose
  }

  def setAttr(name: String, value: String): Unit = {
    builder.append(' ')
    builder.append(name)
    builder.append('=')
    builder.append('"')
    builder.append(value)
    builder.append('"')
    lastOp = OpAttr
  }

  def addTextNode(text: String): Unit = {
    if (lastOp != OpClose && lastOp != OpText) builder.append('>')
    builder.append(text)
    lastOp = OpText
  }

  def addMisc(misc: MiscType): Unit = {}

  /** Creates string from buffer */
  def mkString: String = builder.mkString
}
