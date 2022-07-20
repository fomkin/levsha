/*
 * Copyright 2017-2020 Aleksey Fomkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package levsha.dom

import levsha.{FastId, Id, XmlNs}
import levsha.impl.DiffRenderContext.ChangesPerformer
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{Element, Node}
import org.scalajs.{dom => browserDom}

import scala.collection.mutable
import scala.scalajs.js

final class DomChangesPerformer(target: Element) extends ChangesPerformer {

  private val index = mutable.Map[Id, Node](Id.TopLevel -> target)

  private def create(fastId: FastId)(createNewElement: => Node): Unit = {
    val id = fastId.mkId
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

  def createText(id: FastId, text: String): Unit = create(id) {
    browserDom.document.createTextNode(text)
  }

  def create(id: FastId, xmlNs: String, tag: String): Unit = create(id) {
    browserDom.document.createElementNS(xmlNs, tag)
  }

  def remove(id: FastId): Unit = index.remove(id.mkId) foreach { el =>
    el.parentNode.removeChild(el)
  }

  def setAttr(id: FastId, xmlNs: String, name: String, value: String): Unit = index.get(id.mkId) foreach {
    case node: Element if xmlNs eq XmlNs.html.uri => node.setAttribute(name, value)
    case node: Element => node.setAttributeNS(xmlNs, name, value)
    case node => browserDom.console.warn(s"Can't set attribute to $node")
  }

  def removeAttr(id: FastId, xmlNs: String, name: String): Unit = index.get(id.mkId) foreach {
    case node: Element if xmlNs eq XmlNs.html.uri => node.removeAttribute(name)
    case node: Element => node.removeAttributeNS(xmlNs, name)
    case node => browserDom.console.warn(s"Can't remove attribute from $node")
  }

  def setStyle(id: FastId, name: String, value: String): Unit = index.get(id.mkId) foreach {
    case node: HTMLElement => node.style.setProperty(name, value)
    case node => browserDom.console.warn(s"Can't set style to $node")
  }

  def removeStyle(id: FastId, name: String): Unit = index.get(id.mkId) foreach {
    case node: HTMLElement => node.style.removeProperty(name)
    case node => browserDom.console.warn(s"Can't remove style from $node")
  }
}
