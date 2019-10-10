package levsha

import levsha.impl.DiffRenderContext.ChangesPerformer

import scala.collection.mutable
import scala.language.implicitConversions

sealed trait Change {
  def id: List[Int]
}

object Change {

  implicit def parseId(s: String): List[Int] = s.split('_').toList.map(_.toInt)

  final class DiffTestChangesPerformer extends ChangesPerformer {
    private val buffer = mutable.Buffer.empty[Change]
    def removeAttr(id: Id, xmlNs: String, name: String): Unit =
      buffer += Change.removeAttr(id.toList.map(_.toInt), xmlNs: String, name)
    def removeStyle(id: Id, name: String): Unit =
      buffer += Change.removeStyle(id.toList.map(_.toInt), name)
    def remove(id: Id): Unit =
      buffer += Change.remove(id.toList.map(_.toInt))
    def setAttr(id: Id, xmlNs: String, name: String, value: String): Unit =
      buffer += Change.setAttr(id.toList.map(_.toInt), name, xmlNs, value)
    def setStyle(id: Id, name: String, value: String): Unit =
      buffer += Change.setStyle(id.toList.map(_.toInt), name, value)
    def createText(id: Id, text: String): Unit =
      buffer += Change.createText(id.toList.map(_.toInt), text)
    def create(id: Id, xmlNs: String, tag: String): Unit =
      buffer += Change.create(id.toList.map(_.toInt), tag, xmlNs)
    def result: Seq[Change] = buffer.toVector
  }

  case class removeAttr(id: List[Int], xmlNs: String, name: String) extends Change
  case class removeStyle(id: List[Int], name: String) extends Change
  case class remove(id: List[Int]) extends Change
  case class setAttr(id: List[Int], name: String, xmlNs: String, value: String) extends Change
  case class setStyle(id: List[Int], name: String, value: String) extends Change
  case class createText(id: List[Int], text: String) extends Change
  case class create(id: List[Int], tag: String, xmlNs: String) extends Change

  implicit val ordering = new Ordering[Change] {
    import Ordering.Implicits._
    private val underlying = implicitly[Ordering[List[Int]]]
    def compare(x: Change, y: Change): Int = {
      underlying.compare(x.id, y.id)
    }
  }
}
