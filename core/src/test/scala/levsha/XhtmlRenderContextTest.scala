package levsha

import levsha.impl.{TextPrettyPrintingConfig, XhtmlRenderContext}
import utest._

object XhtmlRenderContextTest extends TestSuite {

  import levsha.dsl._
  import levsha.dsl.html._

  val tests = this {
    test("text nodes should be escaped") - {
      val rc = new XhtmlRenderContext(TextPrettyPrintingConfig.noPrettyPrinting)
      div("1 < 3 > 2 && \"ha ha ha\" ").apply(rc)
      val result = rc.mkString
      assert(result == "<div>1 &lt; 3 &gt; 2 &amp;&amp; &quot;ha ha ha&quot; </div>")
    }
    test("attr values should be escaped") - {
      val rc = new XhtmlRenderContext(TextPrettyPrintingConfig.noPrettyPrinting)
      input(value := "project=\"PRJ\"").apply(rc)
      val result = rc.mkString
      assert(result == "<input value=\"project=&quot;PRJ&quot;\"></input>")
    }

  }
}
