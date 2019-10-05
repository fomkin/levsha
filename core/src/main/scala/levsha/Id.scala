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

import java.util

final class Id(private val array: Array[Short]) { lhs =>

  def level: Int = array.length

  def parent: Option[Id] = if (level == 1) None else Some(take(level - 1))

  def take(num: Int): Id = {
    val na = new Array[Short](num)
    Array.copy(array, 0, na, 0, num)
    new Id(na)
  }

  def mkString: String = mkString('_')

  def mkString(sep: Char): String = if (array.length == 0) {
    ""
  } else {
    val sb = new StringBuilder()
    var i = 0
    var continue = true
    while (continue) {
      sb.append(array(i))
      i += 1
      if (i >= array.length) continue = false
      else sb.append(sep)
    }
    sb.mkString
  }

  def toList: List[Short] = array.toList

  override def toString: String = mkString

  override def equals(obj: Any): Boolean = obj match {
    case rhs: Id => util.Arrays.equals(lhs.array, rhs.array)
    case _ => false
  }

  override lazy val hashCode: Int = util.Arrays.hashCode(array)
}

object Id {

  final val TopLevel = Id(1.toShort)

  def apply(xs: Short*): Id =
    new Id(xs.toArray)

  def apply(s: String, sep: Char = '_'): Id =
    new Id(s.split(sep).map(_.toShort))
}
