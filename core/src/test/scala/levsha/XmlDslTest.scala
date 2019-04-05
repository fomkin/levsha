package levsha

import levsha.Document.Node
import levsha.dsl.XmlDsl
import levsha.impl.TextPrettyPrintingConfig
import utest._
import utest.framework.{Test, Tree}

import scala.collection.mutable

object XmlDslTest extends TestSuite {

  val tests: Tree[Test] = this {
    "simple case" - {
      import levsha.text._
      import xmlDsl._
      val document = xml"""<a href="https://exmaple.com">Example</a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a href="https://exmaple.com">Example</a>""")
    }
    "three nested nodes" - {
      import levsha.text._
      import xmlDsl._
      val document = xml"""<a><b><c></c></b></a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a><b><c></c></b></a>""")
    }

    "mix of nodes" - {
      import levsha.text._
      import xmlDsl._
      val node = xml"<b></b>"
      val document = xml"""<a>$node <![CDATA[cow]]> hello!</a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a><b></b> cow hello!</a>""")
    }
    "comment" - {
      import levsha.text._
      import xmlDsl._
      val document = xml"""<a><!-- nope --></a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a></a>""")
    }
    "node with variable text" - {
      import levsha.text._
      import xmlDsl._
      val hello = "Hello world"
      val document = xml"""<div>$hello</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div>Hello world</div>""")
    }
    "node with variable tag name" - {
      import levsha.text._
      import xmlDsl._
      val tag = "div"
      val document = xml"""<$tag>Hello world</$tag>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div>Hello world</div>""")
    }
    "node with option body" - {
      import levsha.text._
      import xmlDsl._
      val hello = Option(xml"<p>Hello world</p>")
      val document = xml"""<div>$hello</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div><p>Hello world</p></div>""")
    }
    "node with iterable body" - {
      import levsha.text._
      import xmlDsl._
      val xs = Seq(1, 2, 3)
      val document =
        xml"""<ul>${xs.map { x => xml"<li>${x.toString()}</li>" }}</ul>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<ul><li>1</li><li>2</li><li>3</li></ul>""")
    }
    "node with variable attr value" - {
      import levsha.text._
      import xmlDsl._
      val clazz = "title"
      val document = xml"""<div class="block $clazz">Cow</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div class="block title">Cow</div>""")
    }
    "node with variable attr name" - {
      import levsha.text._
      import xmlDsl._
      val clazz = "class"
      val document = xml"""<div $clazz="block">Cow</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div class="block">Cow</div>""")
    }
    "node with variable attr" - {
      import levsha.text._
      import xmlDsl._
      val clazz = "title"
      val attr = attr"class=$clazz"
      val document = xml"<div $attr>Cow</div>"
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div class="title">Cow</div>""")
    }
    "node with variable list of attrs" - {
      import levsha.text._
      import xmlDsl._
      val xs = Seq(1, 2, 3)
      val attrs = xs.map(_.toString()).map(x => attr"""data-n$x="x-$x"""")
      val document = xml"<div $attrs>Cow</div>"
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div data-n1="x-1" data-n2="x-2" data-n3="x-3">Cow</div>""")
    }
    "misc inclusions" - {
      case class Msk(x: Int)
      val xmlDsl = new XmlDsl[Msk]()
      import xmlDsl._
      val xs = Seq(1, 2, 3)
      val document = xml"<div>Cow ${Msk(2)}</div>"
      val nodes = mutable.Buffer.empty[String]
      val miscs = mutable.Buffer.empty[Msk]
      val rc = new RenderContext[Msk] {
        def openNode(xmlns: XmlNs, name: String): Unit = nodes += name
        def closeNode(name: String): Unit = ()
        def setAttr(xmlNs: XmlNs, name: String, value: String): Unit = ()
        def addTextNode(text: String): Unit = nodes += text
        def addMisc(misc: Msk): Unit = miscs += misc
      }
      document(rc)
      assert(nodes == Seq("div", "Cow "))
      assert(miscs == Seq(Msk(2)))
    }

    "node-returning method" - {
      import levsha.text._
      import xmlDsl._

      def method(x: String, y: String)(children: Node[Nothing]*): Node[Nothing] =
        xml"<p>$x - $y $children</p>"
      val document = xml"""<div><sf:method x="a" y="b"><a>yes</a><a>no</a></sf:method></div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div><p>a - b <a>yes</a><a>no</a></p></div>""")
    }

    "node-returning object apply method" - {
      import levsha.text._
      import xmlDsl._
      object component {
        def apply(x: String, y: Int)(children: Node[Nothing]*): Node[Nothing] =
          xml"<p>$x - ${y.toString} $children</p>"
      }
      val b = "b"
      val document = xml"""<div><sf:component x="a" y=$b><a>yes</a><a>no</a></sf:component></div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div><p>a - b <a>yes</a><a>no</a></p></div>""")
    }

//    "node-returning object implicitly added apply method" - {
//      import levsha.text._
//      import xmlDsl._
//      class ComponentLike
//      implicit final class ComponentLikeOps(x: ComponentLike) {
//        def apply(x: String, y: String)(children: Node[Nothing]*): Node[Nothing] =
//          xml"<p>$x - $y $children</p>"
//      }
//      object component extends ComponentLike
//      val document = xml"""<div><sf:component x="a" y="b"><a>yes</a><a>no</a></sf:component></div>"""
//      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
//      assert(result == """<div><p>a - b <a>yes</a><a>no</a></p></div>""")
//    }

    //    "node-returning function" - {
//      import levsha.text._
//      import xmlDsl._
//      val fun: (String, String) => Seq[Node[Nothing]] => Node[Nothing] =
//        (x, y) => children => xml"<p>$x - $y $children</p>"
//      val document = xml"""<div><sf:fun x="a" y="b"><a>yes</a><a>no</a></sf:fun></div>"""
//      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
//      assert(result == """<div><p>a - b <a>yes</a><a>no</a></p></div>""")
//    }

  }
}
