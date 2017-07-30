package levsha.impl

import levsha.{RenderContext, XmlNs}

/**
  * This render context does nothing
  */
class DummyRenderContext[-M] extends RenderContext[M] {
  def openNode(xmlns: XmlNs, name: String): Unit = {}
  def closeNode(name: String): Unit = {}
  def setAttr(name: String, xmlNs: XmlNs, value: String): Unit = {}
  def addTextNode(text: String): Unit = {}
  def addMisc(misc: M): Unit = {}
}
