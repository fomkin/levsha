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

import scala.collection.mutable
import java.util

trait FastId {
  def mkId: Id
  def hasParent: Boolean
  def mkStringParent(sb: mutable.StringBuilder): Unit
  def mkStringParent(sb: mutable.StringBuilder, sep: Char): Unit
  def mkString(sb: mutable.StringBuilder): Unit
  def mkString(sb: mutable.StringBuilder, sep: Char): Unit
}

final class Id(private val array: Array[Short]) extends FastId { lhs =>

  import Id.DefaultSeparator

  def level: Int = array.length

  def parent: Option[Id] = if (level == 1) None else Some(take(level - 1))

  def take(num: Int): Id = {
    val na = new Array[Short](num)
    Array.copy(array, 0, na, 0, num)
    new Id(na)
  }

  def mkString: String = mkString(DefaultSeparator)

  def mkString(sep: Char): String =
    if (array.length == 0) {
      ""
    } else {
      val sb = new mutable.StringBuilder()
      mkString(sb, sep)
      sb.result()
    }

  def mkString(sb: mutable.StringBuilder): Unit =
    mkString(sb, DefaultSeparator)

  def mkString(sb: mutable.StringBuilder, sep: Char): Unit = {
    mkStringLevel(sb, sep, array.length)
  }

  def mkStringParent(sb: mutable.StringBuilder): Unit = {
    mkStringLevel(sb, DefaultSeparator, array.length - 1)
  }

  def mkStringParent(sb: mutable.StringBuilder, sep: Char): Unit = {
    mkStringLevel(sb, sep, array.length - 1)
  }

  def mkId: Id = this

  def hasParent: Boolean = level > 1

  private def mkStringLevel(sb: mutable.StringBuilder, sep: Char, l: Int): Unit = {
    var i = 0
    var continue = true
    while (continue) {
      sb.append(array(i))
      i += 1
      if (i >= l) continue = false
      else sb.append(sep)
    }
  }

  def toList: List[Short] = array.toList

  override def toString: String = mkString

  override def equals(obj: Any): Boolean = obj match {
    case rhs: Id => util.Arrays.equals(lhs.array, rhs.array)
    case _       => false
  }

  override lazy val hashCode: Int = util.Arrays.hashCode(array)
}

object Id {

  final val TopLevel = Id(1.toShort)
  final val DefaultSeparator = '_'

  def apply(xs: Short*): Id =
    new Id(xs.toArray)

  def apply(s: String, sep: Char = DefaultSeparator): Id =
    new Id(s.split(sep).map(_.toShort))
}
