/*
 * Copyright 2017-2019 Aleksey Fomkin
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

package levsha

import levsha.dsl.SymbolDsl
import levsha.events.{EventId, EventPhase}
import levsha.impl.DiffRenderContext
import org.scalajs.dom.Element
import org.scalajs.{dom => browser}

import scala.collection.mutable
import levsha.events.calculateEventPropagation

import scala.scalajs.js

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
package object dom {

  private class Root(root: Element) {

    val miscBuffer = mutable.Buffer.empty[(Id, Misc)]
    var events = Map.empty[EventId, () => Any]
    val performer = new DomChangesPerformer(root)
    val renderContext = DiffRenderContext[Misc](onMisc = Function.untupled(miscBuffer.+=))
    val registeredNativeEvents = mutable.Set.empty[String]
    val nativeEventHandler = { (nativeEvent: browser.Event) =>
      nativeEvent.target.asInstanceOf[js.Dynamic].vid.asInstanceOf[Any] match {
        case () | null => // do nothing
        case vid: String =>
          val targetId = Id(vid)
          calculateEventPropagation(targetId, nativeEvent.`type`) forall { eid =>
            events.get(eid) match {
              case Some(f) => f() == true
              case None => true
            }
          }
      }
    }

    def saveEvents(): Unit = {
      events = miscBuffer.toMap collect {
        case (id, Misc.Event(t, p, f)) =>
          if (!registeredNativeEvents.contains(t)) {
            registeredNativeEvents += t
            root.addEventListener(t, nativeEventHandler)
          }
          EventId(id, t , p) -> f
      }
      
      // Reset the buffer
      miscBuffer.clear()
    }
  }

  private val roots = mutable.Map.empty[Element, Root]

  val symbolDsl = new SymbolDsl[Misc]

  def render(target: Element)(node: Document.Node[Misc]): Unit = {
    val root = roots.getOrElseUpdate(target, new Root(target))
    node(root.renderContext)
    root.saveEvents()
    root.renderContext.diff(root.performer)
    root.renderContext.swap()
  }

  def event(`type`: String, phase: EventPhase = EventPhase.Bubbling)
           (callback: => Any): Misc.Event = {
    Misc.Event(`type`, phase, () => callback)
  }
}
