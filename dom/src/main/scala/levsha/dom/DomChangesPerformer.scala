package levsha.dom

import levsha.Id
import levsha.impl.DiffRenderContext.ChangesPerformer
import org.scalajs.dom.{Element, Node}
import org.scalajs.{dom => browserDom}

import scala.collection.mutable
import scala.scalajs.js

final class DomChangesPerformer(target: Element) extends ChangesPerformer {

  private val index = mutable.Map[Id, Node](Id(1.toShort) -> target)

  private def create(id: Id)(createNewElement: => Node): Unit = {
    val parentId = id.parent
    parentId.flatMap(index.get) foreach { parent =>
      val newEl = createNewElement
      newEl.asInstanceOf[js.Dynamic]
        .vid = id.mkString
      index.get(id) match {
        case Some(oldEl) if oldEl.parentNode == parent =>
          parent.replaceChild(newEl, oldEl)
          index.update(id, newEl)
        case _ =>
          parent.appendChild(newEl)
          index.update(id, newEl)
      }
    }
  }

  def createText(id: Id, text: String): Unit = create(id) {
    browserDom.document.createTextNode(text)
  }

  def create(id: Id, tag: String, xmlNs: String): Unit = create(id) {
    browserDom.document.createElementNS(xmlNs, tag)
  }

  def remove(id: Id): Unit = index.remove(id) foreach { el =>
    el.parentNode.removeChild(el)
  }

  def setAttr(id: Id, name: String, value: String): Unit = index.get(id) foreach {
    case el: Element =>
      el.setAttribute(name, value)
    case node => browserDom.console.warn(s"Can't set attribute to $node")
  }

  def removeAttr(id: Id, name: String): Unit = index.get(id) foreach {
    case el: Element => el.removeAttribute(name)
    case node => browserDom.console.warn(s"Can't remove attribute from $node")
  }
}
