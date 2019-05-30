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

package levsha.dsl

import scala.language.implicitConversions

trait Converters[MiscType] {

  import levsha.Document._

  /** Converts String to text document node */
  implicit def stringToNode(value: String): Node[MiscType] =
    Node { rc => rc.addTextNode(value) }

  /** Converts MiscType to text document node */
  implicit def miscToNode(value: MiscType): Node[MiscType] =
    Node { rc => rc.addMisc(value) }

  /** Converts iterable of attributes to document one attr */
  implicit def seqToAttr(xs: Iterable[Attr[MiscType]]): Attr[MiscType] =
    Attr(rc => xs.foreach(f => f(rc)))

  /** Converts iterable of templates to document fragment */
  implicit def seqToNode(xs: Iterable[Node[MiscType]]): Node[MiscType] =
    Node(rc => xs.foreach(f => f(rc))) // Apply render context to elements

  /** Converts sequence of T (which can be converted to Node) to document fragment */
  implicit def arbitrarySeqToNode[T](xs: Iterable[T])(implicit ev: T => Node[MiscType]): Node[MiscType] =
    Node(rc => xs.foreach(f => ev(f).apply(rc)))

  /** Implicitly unwraps optional attributes */
  implicit def optionToAttr(value: Option[Attr[MiscType]]): Attr[MiscType] =
    Attr(rc => if (value.nonEmpty) value.get(rc))

  /** Implicitly unwraps optional documents */
  implicit def optionToNode(value: Option[Node[MiscType]]): Node[MiscType] =
    Node(rc => if (value.nonEmpty) value.get(rc))

  /** Converts option of T (which can be converted to Node) to document node */
  implicit def arbitraryOptionToNode[T](value: Option[T])(implicit ev: T => Node[MiscType]): Node[MiscType] =
    Node(rc => if (value.nonEmpty) ev(value.get).apply(rc))

}
