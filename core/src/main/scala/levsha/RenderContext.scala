package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
trait RenderContext[-MiscType] {
  def openNode(xmlns: XmlNs, name: String): Unit
  def closeNode(name: String): Unit
  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit
  def addTextNode(text: String): Unit
  def addMisc(misc: MiscType): Unit
}
