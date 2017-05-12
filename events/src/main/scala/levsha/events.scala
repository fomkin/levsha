package levsha

import scala.annotation.tailrec

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object events {

  import EventPhase._

  def calculateEventPropagation(target: Id, `type`: String): Seq[EventId] = {
    @tailrec def capture(acc: List[EventId], i: Int, v: Id): List[EventId] = {
      if (i == v.level) {
        acc
      } else {
        val id = v.take(i)
        val eh = EventId(id, `type`, Capturing)
        capture(eh :: acc, i + 1, v)
      }
    }

    val capturing = capture(Nil, 1, target).reverse
    val atTarget = EventId(target, `type`, AtTarget)
    val bubbling = {
      val xs = capturing.reverse.map(_.copy(phase = Bubbling))
      EventId(target, `type`, Bubbling) :: xs
    }

    capturing ::: (atTarget :: bubbling)
  }

  sealed trait EventPhase

  object EventPhase {
    case object Capturing extends EventPhase
    case object AtTarget extends EventPhase
    case object Bubbling extends EventPhase
  }

  case class EventId(target: Id, `type`: String, phase: EventPhase)
}
