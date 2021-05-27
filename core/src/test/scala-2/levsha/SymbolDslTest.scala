package levsha

import levsha.Document.Node
import levsha.impl.TextPrettyPrintingConfig
import levsha.text._
import levsha.dsl.SymbolDsl

object SymbolDslTest extends utest.TestSuite {

  def tests = {
    import utest._
    this {
      "if (expr) node else void" - {
        val result = renderHtml(ifexpr1(true), TextPrettyPrintingConfig.noPrettyPrinting)
        assert(result == """<div class="hello"></div>""")
      }
      "if (expr) void else node" - {
        val result = renderHtml(ifexpr2(false), TextPrettyPrintingConfig.noPrettyPrinting)
        assert(result == """<div class="hello"></div>""")
      }
      "if (expr) foo else bar" - {
        val result1 = renderHtml(ifexpr3(true), TextPrettyPrintingConfig.noPrettyPrinting)
        val result2 = renderHtml(ifexpr3(false), TextPrettyPrintingConfig.noPrettyPrinting)
        assert(result1 == """<div class="foo"></div>""")
        assert(result2 == """<div class="bar"></div>""")
      }
      "'div('attr /= value)" - {
        assert(renderHtml(attrOpt(Some("foo")), TextPrettyPrintingConfig.noPrettyPrinting) == """<div attr="foo"></div>""")
        assert(renderHtml(attrOpt(None), TextPrettyPrintingConfig.noPrettyPrinting) == """<div></div>""")
      }
    }
  }

  val symbolDsl = new SymbolDsl[Nothing]()
  import symbolDsl._

  def ifexpr1(expr: Boolean): Node[Nothing] =
    'div(if (expr) 'class /= "hello" else void)
  def ifexpr2(expr: Boolean): Node[Nothing] =
    'div(if (expr) void else 'class /= "hello")
  def ifexpr3(expr: Boolean): Node[Nothing] =
    'div(if (expr) 'class /= "foo" else 'class /= "bar")
  def attrOpt(value: Option[String]): Node[Nothing] =
    'div('attr /= value)

}
