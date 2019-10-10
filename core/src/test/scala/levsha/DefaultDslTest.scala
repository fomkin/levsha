package levsha

import levsha.Document.Node
import levsha.impl.TextPrettyPrintingConfig
import levsha.text._

object DefaultDslTest extends utest.TestSuite {

  import levsha.dsl._
  import levsha.dsl.html.tags.{div, a, h1, ul, li, p}
  import levsha.dsl.html.attributes.{clazz, href}
  import levsha.dsl.html.styles.{backgroundColor, border}

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
      "complex template 1" - {
        val result1 = renderHtml(complexTemplate1)
        val result2 = renderHtml(optimizedComplexTemplate1)
        assert(result1 == result2)
      }
      "sort content for unoptimized templates" - {
        val expect = """<div style="background-color:black;" class="foo"><a></a>hello</div>"""
        val result = renderHtml(dynTemplate, TextPrettyPrintingConfig.noPrettyPrinting)
        assert(result == expect)
      }
    }
  }

  def ifexpr1(expr: Boolean): Node[Nothing] =
    optimize(div(if (expr) clazz := "hello" else void))
  def ifexpr2(expr: Boolean): Node[Nothing] =
    optimize(div(if (expr) void else clazz := "hello"))
  def ifexpr3(expr: Boolean): Node[Nothing] =
    optimize(div(if (expr) clazz := "foo" else clazz := "bar"))

  val items = Seq(1, 2, 3)
  val optValue1 = Option("hello")
  val optValue2 = Option.empty[String]

  val complexTemplate1 =
    div(
      border @= "1 px solid",
      h1(backgroundColor @= "red", "The Items!"),
      optValue1.map(x => p(x)),
      ul(
        items map { i =>
          li(
            optValue2.map(x => clazz := x),
            a(href := s"http://example.com/items/$i", s"Go $i")
          )
        }
      )
    )

  val dynTemplate = {
    div(
      a(),
      "hello",
      void,
      clazz := "foo",
      backgroundColor @= "black"
    )
  }

  val optimizedComplexTemplate1 = optimize {
    div(
      border @= "1 px solid",
      h1(backgroundColor @= "red", "The Items!"),
      optValue1.map(x => p(x)),
      ul(
        items map { i =>
          li(
            optValue2.map(x => clazz := x),
            a(href := s"http://example.com/items/$i", s"Go $i")
          )
        }
      )
    )
  }

}
