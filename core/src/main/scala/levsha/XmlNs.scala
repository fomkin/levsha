package levsha

import scala.language.experimental.macros

final case class XmlNs(shortcut: String, uri: String) {
  override val hashCode: Int = uri.hashCode
}

object XmlNs {
  val html = XmlNs("html", "http://www.w3.org/1999/xhtml")
  val svg = XmlNs("svg", "http://www.w3.org/2000/svg")
  val mathml = XmlNs("mathml", "http://www.w3.org/1998/Math/MathML")
}
