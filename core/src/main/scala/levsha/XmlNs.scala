package levsha

import scala.language.experimental.macros

final case class XmlNs(uri: String) {

  /**
    * Create qualified name from symbol
    */
  // TODO move this to SymbolDsl
  def apply(symbol: Symbol): QualifiedName =
    macro dsl.SymbolDslMacro.xmlNsCreateQualifiedName

  override val hashCode: Int = uri.hashCode
}

object XmlNs {
  val html = XmlNs("http://www.w3.org/1999/xhtml")
  val svg = XmlNs("http://www.w3.org/2000/svg")
  val mathml = XmlNs("http://www.w3.org/1998/Math/MathML")
}
