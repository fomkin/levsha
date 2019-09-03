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

import levsha.{Document, QualifiedName, XmlNs}

import scala.language.experimental.macros
import scala.language.implicitConversions

/**
 * Symbol based DSL allows to define documents
 * {{{
 *   'body(
 *     'h1(class /= "title", "Hello World"),
 *     'p("Lorem ipsum dolor")
 *   )
 * }}}
 */
class SymbolDsl[MiscType] extends Converters[MiscType] {

  import Document._

  implicit final class SymbolOps(symbol: Symbol) {

    def apply(children: Document[MiscType]*): Node[MiscType] =
      macro SymbolDslMacro.node[MiscType]

    def /=(value: String): Attr[MiscType] =
      macro SymbolDslMacro.attr[MiscType]

    def /=(value: Option[String]): Attr[MiscType] =
      macro SymbolDslMacro.attrOpt[MiscType]

  }

  implicit final class QualifiedNameOps(s: QualifiedName) {

    def apply(children: Document[MiscType]*): Node[MiscType] =
      macro SymbolDslMacro.node[MiscType]

    def /=(value: String): Attr[MiscType] =
      macro SymbolDslMacro.attr[MiscType]
  }

  implicit final class XmlNsOps(s: XmlNs) {

    /**
      * Create qualified name from symbol
      */
    def apply(symbol: Symbol): QualifiedName =
      macro SymbolDslMacro.xmlNsCreateQualifiedName
  }

  val void: Document.Empty.type = Empty

  val ns: XmlNs.type = XmlNs
}
