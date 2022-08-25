package levsha

import levsha.impl.DiffRenderContext
import levsha.impl.DiffRenderContext.DummyChangesPerformer

import scala.language.implicitConversions

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
  object DiffRenderContextTest extends utest.TestSuite {
  
  import utest.{TestableString, TestableSymbol => _, assert}
  import Change._
  import levsha.dsl._
  import html._

  val tests = this {

    "should replace text to node" - {
      val changes = runDiff(
        original = "m",
        updated = input(disabled, div(name := "cow"))
      )
      assert(
        changes == Seq(
          create("1", "input", XmlNs.html.uri),
          setAttr("1", "disabled", XmlNs.html.uri, ""),
          create("1_1", "div", XmlNs.html.uri),
          setAttr("1_1", "name", XmlNs.html.uri, "cow")
        )
      )
    }

    /*
changes:  List(setAttr(List(1, 1),class,http://www.w3.org/1999/xhtml,война), create(List(1, 4),div,http://www.w3.org/1999/xhtml), setAttr(List(1, 4),lang,http://www.w3.org/1999/xhtml,ru), create(List(1, 6),span,http://www.w3.org/1999/xhtml))
expected: List(setAttr(List(1, 1),class,война,http://www.w3.org/1999/xhtml), create(List(1, 4),div,http://www.w3.org/1999/xhtml), setAttr(List(1, 4),lang,http://www.w3.org/1999/xhtml,ru), create(List(1, 6),span,http://www.w3.org/1999/xhtml))

    */
    "should text to node with chidlren" - {
      val changes = runDiff(
        original = {
          div(clazz := "world",
            input(clazz := "мир",lang := "ru",
              "j"
            ),
            p(clazz := "мир",lang := "ru"),
            "I",
            "j",
            "b",
            "V",
            "A",
            "d",
            "d",
            "o",
            p(clazz := "world")
          )
        },
        updated = {
          div(clazz := "world",
            input(clazz := "война",lang := "ru",
              "j"
            ),
            p(clazz := "мир",lang := "ru"),
            "I",
            div(lang := "ru"),
            "b",
            span(),
            "A",
            "d",
            "d",
            "o",
            p(clazz := "world")
          )
        }
      )
      val expected = Seq(setAttr("1_1","class",XmlNs.html.uri,"война"), create("1_4","div", XmlNs.html.uri), setAttr("1_4","lang", XmlNs.html.uri, "ru"), create("1_6","span", XmlNs.html.uri))
      assert(changes == expected)
    }

    "should remove attribute" - {
      val changes = runDiff(
        original = { span(style := "margin: 10;", clazz := "world", "q") },
        updated = { span(style := "margin: 10;", "q") }
      )
      val expected = Seq(removeAttr("1", XmlNs.html.uri, "class"))
      assert(changes == expected)
    }
    
    "should remove only subroot, not entire tree" - {
      val changes = runDiff(
        original = {
          div(clazz := "world",
            div(clazz := "world"),
            div(clazz := "hello",
              span(name := "cow",clazz := "hello",
                span()
              ),
              div(clazz := "world",style := "margin: 10;")
            ),
            button(clazz := "world",
              span(style := "margin: 10;",clazz := "hello"),
              span(),
              div(lang := "ru",name := "cow")
            ),
            "dasd"
          )
        },
        updated = {
          div(clazz := "world",
            div(clazz := "world"),
            "gGi"
          )
        }
      )
      assert {
        changes == Seq(
          createText(List(1, 2), "gGi"),
          remove(List(1, 3)),
          remove(List(1, 4))
        )
      }
    }

    "should replace node with another one" - {
      val changes = runDiff(
        original = div(span(div(), div(), div())),
        updated = div(div())
      )
      assert(
        changes == Seq(
          create("1_1", "div", XmlNs.html.uri)
        )
      )
    }

    "should save and restore state" - {
      val original = span(clazz := "world",style := "margin: 10;", "q")
      val updated = span(style := "margin: 10;", "q")
      val performer = new DiffTestChangesPerformer()
      val renderContext1 = DiffRenderContext[Nothing]()
      original(renderContext1)
      renderContext1.finalizeDocument()
      renderContext1.swap()
      renderContext1.reset()
      val buffer = renderContext1.save()
      val renderContext2 = DiffRenderContext[Nothing](savedBuffer = Some(buffer))
      updated(renderContext2)
      renderContext2.finalizeDocument()
      renderContext2.diff(performer)
      val changes = performer.result
      assert(changes == Seq(removeAttr("1", XmlNs.html.uri, "class")))
    }

    "should consider two identical tags with different xmlNs as different tags" - {
      import svg._
      val HtmlSvg = TagDef(XmlNs.html, "svg")
      val changes = runDiff(
        original = {
          div(HtmlSvg(width := "1"))
        },
        updated = {
          div(Svg(width := "1"))
        }
      )
      assert {
        changes == Seq(
          create(List(1, 1), "svg", XmlNs.svg.uri),
          setAttr(List(1, 1), "width", XmlNs.html.uri, "1")
        )
      }
    }
  }

  // -----------------------

  def runDiff(original: Document[Nothing], updated: Document[Nothing]): Seq[Change] = {
    val performer = new DiffTestChangesPerformer()
    val renderContext = DiffRenderContext[Nothing]()
    original(renderContext)
    renderContext.finalizeDocument()
    renderContext.swap()
    updated(renderContext)
    renderContext.finalizeDocument()
    renderContext.diff(performer)
    performer.result
  }
}
