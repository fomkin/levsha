package levsha

sealed trait Special

final case class XmlNs(uri: String) extends Special {
  override val hashCode: Int = uri.hashCode
}

object XmlNs {
  val html = XmlNs("http://www.w3.org/1999/xhtml")
  val svg = XmlNs("http://www.w3.org/2000/svg")
  val mathml = XmlNs("http://www.w3.org/1998/Math/MathML")
}
