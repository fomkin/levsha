/*
 * Copyright 2017-2018 Aleksey Fomkin
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

import levsha.{Document, XmlNs}

import scala.language.experimental.macros
import scala.language.implicitConversions

class XmlDsl[M] extends Converters[M] {

  // Hack. `xml` string interpolator takes documents. Types
  // suck as String, Option or Iterable
  // legally wraps into Node by Converters.
  // In XML-dsl we need to allow custom XML namespaces
  // so levsha.Xml also should be wrapped to node,
  // and unwrapped via macros.
  implicit def xmlNsToNode(ns: XmlNs): Document[M] =
    Document.Node(_ => ()) // stub. do nothing

  implicit final class XmlStringContext(val sc: StringContext) {
    def attr(args: Document[M]*): Document.Attr[M] =
      macro XmlDslMacro.attrStringContextImpl[M]
    def html(args: Document[M]*): Document.Node[M] =
      macro XmlDslMacro.xmlStringContextImpl[M]
    /** Use this if your template is valid XML */
    def xml(args: Document[M]*): Document.Node[M] =
      macro XmlDslMacro.xmlStringContextImpl[M]
  }
}
