package levsha.impl

import levsha.{RenderContext, XmlNs}

/**
  * This render context does nothing
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class DummyRenderContext[-M] extends RenderContext[M] {
  def openNode(name: String, xmlns: XmlNs): Unit = {}
  def closeNode(name: String): Unit = {}
  def setAttr(name: String, value: String): Unit = {}
  def addTextNode(text: String): Unit = {}
  def addMisc(misc: M): Unit = {}
}
