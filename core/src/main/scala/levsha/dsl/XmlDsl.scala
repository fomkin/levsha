package levsha.dsl

import levsha.Document

import scala.language.experimental.macros

class XmlDsl[+M] {

  implicit class XmlStringContext(val sc: StringContext) {
    def attr(args: Any*): Document.Attr[M] =
      macro XmlDslMacro.attrStringContextImpl[M]
    def xml(args: Any*): Document.Node[M] =
      macro XmlDslMacro.xmlStringContextImpl[M]
  }
}
