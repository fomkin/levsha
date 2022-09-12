package levsha

import levsha.impl.{DummyRenderContext, Html5RenderContext, TextPrettyPrintingConfig}
import utest.TestSuite
import utest._

object TemplateTest extends TestSuite {

  import levsha.dsl._
  import levsha.dsl.html._

  final val tests = this {
    "reuse portable result from template" - {
      var template1Invocations = 0
      val template1 = Template() { (s: String) =>
        template1Invocations += 1
        a(s)
      }
      var template2Invocations = 0
      val template2 = Template() { (s: String) =>
        template2Invocations += 1
        p(s)
      }
      val doc = div(
        template1("cow"),
        template1("cow"),
        template1("wow"),
        template2("cow"),
        template2("cow"),
        template2("wow"),
      )
      val rc = new Html5RenderContext(TextPrettyPrintingConfig.noPrettyPrinting)
      doc.apply(rc)
      val html = rc.mkString
      assert(
        template1Invocations == 2,
        template2Invocations == 2,
        html == """<div><a>cow</a><a>cow</a><a>wow</a><p>cow</p><p>cow</p><p>wow</p></div>"""
      )
    }
  }
}