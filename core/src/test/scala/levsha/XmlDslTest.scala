package levsha

import levsha.impl.TextPrettyPrintingConfig
import utest._
import utest.framework.{Test, Tree}

object XmlDslTest extends TestSuite {
  import levsha.text._
  import xmlDsl._

  val tests: Tree[Test] = this {
    "simple case" - {
      val document = xml"""<a href="https://exmaple.com">Example</a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a href="https://exmaple.com">Example</a>""")
    }
    "three nested nodes" - {
      val document = xml"""<a><b><c></c></b></a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a><b><c></c></b></a>""")
    }

    "mix of nodes" - {
      val node = xml"<b></b>"
      val document = xml"""<a>$node <![CDATA[cow]]> hello!</a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a><b></b> cow hello!</a>""")
    }
    "comment" - {
      val document = xml"""<a><!-- nope --></a>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<a></a>""")
    }
    "node with variable text" - {
      val hello = "Hello world"
      val document = xml"""<div>$hello</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div>Hello world</div>""")
    }
    "node with variable tag name" - {
      val tag = "div"
      val document = xml"""<$tag>Hello world</$tag>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div>Hello world</div>""")
    }
    "node with option body" - {
      val hello = Option(xml"<p>Hello world</p>")
      val document = xml"""<div>$hello</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div><p>Hello world</p></div>""")
    }
    "node with iterable body" - {
      val xs = Seq(1, 2, 3)
      val document =
        xml"""<ul>${xs.map { x => xml"<li>${x.toString()}</li>" }}</ul>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<ul><li>1</li><li>2</li><li>3</li></ul>""")
    }
    "node with variable attr value" - {
      val clazz = "title"
      val document = xml"""<div class="block $clazz">Cow</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div class="block title">Cow</div>""")
    }
    "node with variable attr name" - {
      val clazz = "class"
      val document = xml"""<div $clazz="block">Cow</div>"""
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div class="block">Cow</div>""")
    }
    "node with variable attr" - {
      val clazz = "title"
      val attr = attr"class=$clazz"
      val document = xml"<div $attr>Cow</div>"
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div class="title">Cow</div>""")
    }
    "node with variable list of attrs" - {
      val xs = Seq(1, 2, 3)
      val attrs = xs.map(_.toString()).map(x => attr"""data-n$x="x-$x"""")
      val document = xml"<div $attrs>Cow</div>"
      val result = renderHtml(document, TextPrettyPrintingConfig.noPrettyPrinting)
      assert(result == """<div data-n1="x-1" data-n2="x-2" data-n3="x-3">Cow</div>""")
    }
  }
}
