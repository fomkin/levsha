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
