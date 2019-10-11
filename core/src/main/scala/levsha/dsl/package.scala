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

import levsha.Document.{Attr, Empty, Node, Style}

import scala.language.implicitConversions
import scala.language.experimental.macros

package object dsl {

  def optimize[T](node: Node[T]): Node[T] = macro DslOptimizerMacro.optimize[T]

  final case class TagDef(ns: XmlNs, tagName: String) {
    def apply[M](content: Document[M]*): Node[M] = Node { rc =>
      rc.openNode(ns, tagName)
      content.filter(_ != Empty).sortBy(_.kind).foreach(_(rc))
      rc.closeNode(tagName)
    }
  }

  final case class AttrDef(ns: XmlNs, attrName: String) {
    def :=[M](value: String): Attr[M] = Attr[M] { rc =>
      rc.setAttr(ns, attrName, value)
    }
  }

  final case class RichAttrDef[T](ns: XmlNs, attrName: String, mkString: T => String) {
    def :=[M](value: T): Attr[M] = Attr[M] { rc =>
      rc.setAttr(ns, attrName, mkString(value))
    }
    def :=[M](value: String): Attr[M] = Attr[M] { rc =>
      rc.setAttr(ns, attrName, value)
    }
  }

  final case class StyleDef(styleName: String) {
    def @=[M](value: String): Style[M] = Style[M] { rc =>
      rc.setStyle(styleName, value)
    }
  }

  final case class RichStyleDef[T](attrName: String, mkString: T => String) {
    def @=[M](value: String): Style[M] = Style[M] { rc =>
      rc.setStyle(attrName, value)
    }
    def @=[M](value: T): Style[M] = Style[M] { rc =>
      rc.setStyle(attrName, mkString(value))
    }
  }

  val void = Empty

  /**
    * Use it when want overwrite default click behavior.
    * {{{
    *   a(href := "http://example.com", preventDefaultClickBehavior)
    * }}}
    */
  val preventDefaultClickBehavior = Attr[Nothing] { rc =>
    rc.setAttr(XmlNs.html, "onclick", "return false")
  }

  // Converters

  /** Converts String to text document node */
  implicit def stringToNode[M](value: String): Node[M] =
    Node { rc => rc.addTextNode(value) }

  /** Converts MiscType to text document node */
  implicit def miscToNode[M](value: M): Node[M] =
    Node { rc => rc.addMisc(value) }

  /** Converts iterable of attributes to document one attr */
  implicit def seqToAttr[M](xs: Iterable[Attr[M]]): Attr[M] =
    Attr(rc => xs.foreach(f => f(rc)))

  /** Converts iterable of templates to document fragment */
  implicit def seqToNode[M](xs: Iterable[Node[M]]): Node[M] =
    Node(rc => xs.foreach(f => f(rc))) // Apply render context to elements

  /** Converts sequence of T (which can be converted to Node) to document fragment */
  implicit def arbitrarySeqToNode[M, T](xs: Iterable[T])(implicit ev: T => Node[M]): Node[M] =
    Node(rc => xs.foreach(f => ev(f).apply(rc)))

  /** Implicitly unwraps optional attributes */
  implicit def optionToAttr[M](value: Option[Attr[M]]): Attr[M] =
    Attr(rc => if (value.nonEmpty) value.get(rc))

  /** Implicitly unwraps optional documents */
  implicit def optionToNode[M](value: Option[Node[M]]): Node[M] =
    Node(rc => if (value.nonEmpty) value.get(rc))

  /** Converts option of T (which can be converted to Node) to document node */
  implicit def arbitraryOptionToNode[M, T](value: Option[T])(implicit ev: T => Node[M]): Node[M] =
    Node(rc => if (value.nonEmpty) ev(value.get).apply(rc))
}
