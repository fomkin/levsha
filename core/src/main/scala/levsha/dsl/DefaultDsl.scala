package levsha.dsl

import levsha.Document.{Attr, Empty, Node}
import levsha.{Document, XmlNs}

import scala.language.experimental.macros
import scala.language.implicitConversions

class DefaultDsl[M] extends Converters[M] {
}

object DefaultDsl {

  def optimize[T](node: Node[T]): Node[T] =
    macro DslOptimizerMacro.optimize[T]

  final case class TagDef(ns: XmlNs, tagName: String) {
    def apply(children: Document[Nothing]*): Node[Nothing] = Node { rc =>
      rc.openNode(ns, tagName)
      children.foreach(_(rc))
      rc.closeNode(tagName)
    }
  }

  final case class AttrDef[T](ns: XmlNs, attrName: String, mkString: T => String) {
    def :=(value: T): Attr[Nothing] = Attr[Nothing] { rc =>
      rc.setAttr(ns, attrName, mkString(value))
    }
  }

  object Html {
    import XmlNs.html
    val div = TagDef(html, "div")
    val ul = TagDef(html, "ul")
    val h1 = TagDef(html, "h1")
    val li = TagDef(html, "li")
    val a = TagDef(html, "a")
    val p = TagDef(html, "p")

    val clazz: AttrDef[String] = AttrDef(html, "class", identity)
    val href: AttrDef[String] = AttrDef(html, "href", identity)
    val void = Empty
  }

  object Converters {
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
}
