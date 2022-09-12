package levsha.impl.internal

import CaseClassRenderContext.Result
import levsha.{RenderContext, XmlNs}

import scala.collection.mutable

class CaseClassRenderContext[M] extends RenderContext[M] {

  private final val buffer = mutable.Buffer.empty[Result[M]]

  def openNode(xmlNs: XmlNs, name: String): Unit =
    buffer += Result.OpenNode(xmlNs, name)

  def closeNode(name: String): Unit =
    buffer += Result.CloseNode(name)

  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit =
    buffer += Result.SetAttr(xmlNs, name, value)

  def setStyle(name: String, value: String): Unit =
    buffer += Result.SetStyle(name, value)

  def addTextNode(text: String): Unit =
    buffer += Result.AddTextNode(text)

  def addMisc(misc: M): Unit =
    buffer += Result.AddMisc(misc)

  def result(): IndexedSeq[Result[M]] = buffer.toVector
}

object CaseClassRenderContext {
  sealed trait Result[+M]

  object Result {
    final case class OpenNode(xmlNs: XmlNs, name: String) extends Result[Nothing]
    final case class CloseNode(name: String) extends Result[Nothing]
    final case class SetAttr(xmlNs: XmlNs, name: String, value: String) extends Result[Nothing]
    final case class SetStyle(name: String, value: String) extends Result[Nothing]
    final case class AddTextNode(text: String) extends Result[Nothing]
    final case class AddMisc[+M](misc: M) extends Result[M]
  }
}
