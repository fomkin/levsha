package levsha

class QualifiedName

object QualifiedName {
  def apply(xmlNs: XmlNs, name: Symbol): QualifiedName = {
    throw new Exception("QualifiedName should be not used in runtime")
  }
}
