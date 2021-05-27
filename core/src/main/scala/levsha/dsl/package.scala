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

import levsha.Document.{Attr, Empty, Node, Style}

import scala.language.implicitConversions

package object dsl extends levsha.dsl.Optimize {

  trait TagDef {
    def ns: XmlNs
    def name: String
    def apply[M](content: Document[M]*): Node[M] = Node { rc =>
      rc.openNode(ns, name)
      content.filter(_ != Empty).sortBy(_.kind).foreach(_(rc))
      rc.closeNode(name)
    }
  }

  trait AttrDef {
    def ns: XmlNs
    def name: String
    def :=[M](value: String): Attr[M] = Attr[M] { rc =>
      rc.setAttr(ns, name, value)
    }
  }

  trait StyleDef {
    def name: String
    def @=[M](value: String): Style[M] = Style[M] { rc =>
      rc.setStyle(name, value)
    }
  }

  def TagDef(tagName: String): TagDef = TagDef(XmlNs.html, tagName)

  def TagDef(namespace: XmlNs, tagName: String): TagDef = new TagDef {
    def ns: XmlNs = namespace
    def name: String = tagName
  }

  def AttrDef(attrName: String): AttrDef = AttrDef(XmlNs.html, attrName)

  def AttrDef(namespace: XmlNs, attrName: String): AttrDef = new AttrDef {
    def ns: XmlNs = namespace
    def name: String = attrName
  }

  def StyleDef(styleName: String): StyleDef = new StyleDef {
    def name: String = styleName
  }

//
//  final case class RichAttrDef[T](ns: XmlNs, attrName: String, mkString: T => String) {
//    def :=[M](value: T): Attr[M] = Attr[M] { rc =>
//      rc.setAttr(ns, attrName, mkString(value))
//    }
//    def :=[M](value: String): Attr[M] = Attr[M] { rc =>
//      rc.setAttr(ns, attrName, value)
//    }
//  }
//
//  final case class RichStyleDef[T](attrName: String, mkString: T => String) {
//    def @=[M](value: String): Style[M] = Style[M] { rc =>
//      rc.setStyle(attrName, value)
//    }
//    def @=[M](value: T): Style[M] = Style[M] { rc =>
//      rc.setStyle(attrName, mkString(value))
//    }
//  }

  def void[T]: Document.Node[T] with Document.Attr[T] = Empty

  /**
    * Add node or attribute conditionally
    * @example
    * {{{
    * button(
    *   when(inProgress)(disabled)
    *   "Push me"
    * )
    * }}}
    */
  def when[T, D >: Document[T]](condition: Boolean)(doc: D)(implicit ev: Document[T] =:= D): D =
    if (condition) doc else ev(Empty)

  /**
    * Use it when want overwrite default click behavior.
    * {{{
    *   a(href := "http://example.com", preventDefaultClickBehavior)
    * }}}
    */
  val preventDefaultClickBehavior: Attr[Nothing] = Attr { rc =>
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
