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

import java.nio.ShortBuffer
import scala.collection.mutable

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object IdBuilder {
  def apply(maxLevel: Int = 256): IdBuilder =
    new IdBuilder(maxLevel)
}

final class IdBuilder(maxLevel: Int) extends FastId {

  private var level = 1
  private val buffer = ShortBuffer.allocate(maxLevel)
  private def index = level - 1

  // Initial limit
  buffer.limit(level)

  def incLevel(): Unit = {
    level += 1
    buffer.limit(level)
  }

  /** Just decreases level */
  def decLevelTmp(): Unit = {
    level -= 1
    buffer.limit(level)
  }

  /** Resets current id and decreases level */
  def decLevel(): Unit = {
    buffer.put(index, 0.toShort)
    level -= 1
    buffer.limit(level)
  }

  def incId(): Unit = {
    val updated = buffer.get(index) + 1
    buffer.put(index, updated.toShort)
  }

  def decId(): Unit = {
    val updated = buffer.get(index) - 1
    buffer.put(index, updated.toShort)
  }

  def getLevel: Int = level

  def mkArray: Array[Short] = {
    val clone = new Array[Short](buffer.limit)
    buffer.rewind()
    var i = 0
    while (buffer.hasRemaining) {
      clone(i) = buffer.get()
      i += 1
    }
    clone
  }

  def mkString: String = mkString(Id.DefaultSeparator)

  def mkString(sep: Char): String = {
    val sb = new mutable.StringBuilder()
    mkStringLevel(sb, sep, buffer.limit())
    sb.result()
  }

  def mkStringParent(sb: mutable.StringBuilder): Unit =
    mkStringLevel(sb, Id.DefaultSeparator, buffer.limit() - 1)

  def mkStringParent(sb: mutable.StringBuilder, sep: Char): Unit =
    mkStringLevel(sb, sep, buffer.limit())

  def mkString(sb: mutable.StringBuilder): Unit = {
    mkStringLevel(sb, Id.DefaultSeparator, buffer.limit())
  }

  def mkString(sb: mutable.StringBuilder, sep: Char): Unit = {
    mkStringLevel(sb, sep, buffer.limit())
  }

  private def mkStringLevel(sb: mutable.StringBuilder, sep: Char, l: Int): Unit = {
    buffer.rewind()
    var continue = true
    while (continue) {
      sb.append(buffer.get())
      if (buffer.position() >= l) continue = false
      else sb.append(sep)
    }
  }

  def mkId: Id = new Id(mkArray)

  def reset(): Unit = {
    buffer.clear()
    while (buffer.hasRemaining) buffer.put(0.toShort)
    level = 1
    buffer.rewind()
    buffer.limit(level)
  }
}
