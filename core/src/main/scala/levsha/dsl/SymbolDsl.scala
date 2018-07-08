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
