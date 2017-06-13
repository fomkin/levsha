package levsha.impl

import levsha.RenderContext
import levsha.RenderUnit.{Attr, Misc, Node, Text}

/**
  * This render context does nothing
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class DummyRenderContext[-M] extends RenderContext[M] {
  def openNode(name: String): Unit = {}
  def closeNode(name: String): Node.type = Node
  def setAttr(name: String, value: String): Attr.type = Attr
  def addTextNode(text: String): Text.type = Text
  def addMisc(misc: M): Misc.type = Misc
}