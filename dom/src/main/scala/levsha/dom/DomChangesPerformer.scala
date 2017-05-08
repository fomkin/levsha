package levsha.dom

import levsha.Id
import levsha.impl.DiffRenderContext.ChangesPerformer
import org.scalajs.dom.{Element, Node}
import org.scalajs.{dom => browserDom}

import scala.collection.mutable

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
final class DomChangesPerformer(target: Element) extends ChangesPerformer {

  private val index = mutable.Map[Id, Node](Id(1) -> target)

  private def create(id: Id, createNewElement: () => Node): Unit = {
    val parentId = id.parent
    index.get(parentId) foreach { parent =>
      val newEl = createNewElement()
      index.get(id) match {
        case Some(oldEl) =>
          parent.replaceChild(newEl, oldEl)
          index.update(id, newEl)
        case None =>
          parent.appendChild(newEl)
          index.update(id, newEl)
      }
    }
  }

  def createText(id: Id, text: String): Unit =
    create(id, () => browserDom.document.createTextNode(text))

  def create(id: Id, tag: String): Unit =
    create(id, () => browserDom.document.createElement(tag))

  def remove(id: Id): Unit = index.remove(id) foreach { el =>
    el.parentNode.removeChild(el)
  }

  def setAttr(id: Id, name: String, value: String): Unit = index.get(id) foreach {
    case el: Element => el.setAttribute(name, value)
    case node => browserDom.console.warn(s"Can't set attribute to $node")
  }

  def removeAttr(id: Id, name: String): Unit = index.get(id) foreach {
    case el: Element => el.removeAttribute(name)
    case node => browserDom.console.warn(s"Can't remove attribute from $node")
  }
}
