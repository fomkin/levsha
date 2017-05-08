package levsha.dom

import levsha.events.EventPhase

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
sealed trait Misc

object Misc {
  final case class Event(
    `type`: Symbol,
    phase: EventPhase,
    callback: () => Any) extends Misc
}
