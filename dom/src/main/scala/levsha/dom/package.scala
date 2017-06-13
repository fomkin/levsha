package levsha

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
          val `type` = t.name
          if (!registeredNativeEvents.contains(`type`)) {
            registeredNativeEvents += `type`
            root.addEventListener(`type`, nativeEventHandler)
          }
          EventId(id, `type` , p) -> f
      }
      
      // Reset the buffer
      miscBuffer.clear()
    }
  }

  private val roots = mutable.Map.empty[Element, Root]

  val dsl = new TemplateDsl[Misc]

  def render(target: Element)(f: RenderContext[Misc] => RenderUnit): Unit = {
    val root = roots.getOrElseUpdate(target, new Root(target))
    f(root.renderContext)
    root.saveEvents()
    root.renderContext.diff(root.performer)
    root.renderContext.swap()
  }

  def event(`type`: Symbol, phase: EventPhase = EventPhase.Bubbling)
           (callback: => Any): Misc.Event = {
    Misc.Event(`type`, phase, () => callback)
  }
}