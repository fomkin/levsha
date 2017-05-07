package levsha

import RenderUnit._

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
trait RenderContext[-MiscType] {
  def openNode(name: String): Unit
  def closeNode(name: String): Node
  def setAttr(name: String, value: String): Attr
  def addTextNode(text: String): Text
  def addMisc(misc: MiscType): Misc
}
