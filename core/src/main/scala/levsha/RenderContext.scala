package levsha

import Document._

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
trait RenderContext[-MiscType] {
  def openNode(name: String): Unit
  def closeNode(name: String): Unit
  def setAttr(name: String, value: String): Unit
  def addTextNode(text: String): Unit
  def addMisc(misc: MiscType): Unit
}
