package levsha

import levsha.impl.internal.CaseClassRenderContext
import levsha.impl.{Html5RenderContext, PortableRenderContext, TextPrettyPrintingConfig}

import scala.language.implicitConversions

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object PortableRenderContextTest extends utest.TestSuite {

  import levsha.dsl._
  import levsha.dsl.html._
  import utest.{TestableString, assert, TestableSymbol => _}

  final val tests = this {
    "should correctly apply result to rc which has not ApplyResult trait" - {
      val prc = new PortableRenderContext(1024)
      val node = div(
        name := "now",
        clazz := "wow",
        div(
          "cow"
        )
      )
      node(prc)
      val portableResult = prc.result()
      val targetRc = new Html5RenderContext(TextPrettyPrintingConfig.noPrettyPrinting)
      portableResult(targetRc)
      val result = targetRc.mkString
      val expected = """<div name="now" class="wow"><div>cow</div></div>"""
      assert(result == expected)
    }

    "should correctly apply result to another PortableRenderContext" - {
      val prc = new PortableRenderContext(1024)
      val node = div(
        name := "now",
        clazz := "wow",
        div(
          "cow"
        )
      )
      node(prc)
      val portableResult = prc.result()
      val prc2 = new PortableRenderContext(1024)
      prc2.openNode(XmlNs.html, "body")
      prc2.setAttr(XmlNs.html, "class", "bow")
      prc2.applyResult(portableResult)
      prc2.closeNode("body")
      val portableResult2 = prc2.result()
      val targetRc = new Html5RenderContext(TextPrettyPrintingConfig.noPrettyPrinting)
      portableResult2(targetRc)
      val result = targetRc.mkString
      val expected = """<body class="bow"><div name="now" class="wow"><div>cow</div></div></body>"""
      assert(result == expected)
    }

    "should add miscs in the right order" - {
      final case class TheMisc(value: String)
      import CaseClassRenderContext.Result._
      val prc = new PortableRenderContext[TheMisc](1024)
      val node = div(
        name := "now",
        clazz := "wow",
        TheMisc("good"),
        div(
          "cow",
          TheMisc("bad"),
          TheMisc("ugly"),
          div("bow")
        )
      )
      node(prc)
      val portableResult = prc.result()
      val targetRc = new CaseClassRenderContext[TheMisc]()
      portableResult(targetRc)
      val result = targetRc.result()
      val expected = Vector(
        OpenNode(XmlNs.html, "div"),
        SetAttr(XmlNs.html, "name", "now"),
        SetAttr(XmlNs.html, "class", "wow"),
        AddMisc(TheMisc("good")),
        OpenNode(XmlNs.html, "div"),
        AddTextNode("cow"),
        AddMisc(TheMisc("bad")),
        AddMisc(TheMisc("ugly")),
        OpenNode(XmlNs.html, "div"),
        AddTextNode("bow"),
        CloseNode("div"),
        CloseNode("div"),
        CloseNode("div")
      )
      assert(result == expected)
    }
  }
}
