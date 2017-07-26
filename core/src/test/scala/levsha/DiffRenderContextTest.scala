package levsha

import levsha.impl.DiffRenderContext
import levsha.impl.DiffRenderContext.DummyChangesPerformer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object DiffRenderContextTest extends utest.TestSuite {

  val dsl = new TemplateDsl[Nothing]()

  import utest.{TestableString, TestableSymbol => _}
  import Change._
  import dsl._

  val tests = this {

    "should replace text to node" - {
      val changes = runDiff(
        original = "m",
        updated = 'input('disabled /= "", 'div('name /= "cow"))
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

    "should text to node with chidlren" - {
      val changes = runDiff(
        original = {
          'div('class /= "world",
            'input('class /= "world",'lang /= "ru",
              "j"
            ),
            'p('class /= "world",'lang /= "ru"),
            "I",
            "j",
            "b",
            "V",
            "A",
            "d",
            "d",
            "o",
            'p('class /= "world")
          )
        },
        updated = {
          'div('class /= "world",
            'input('class /= "world",'lang /= "ru",
              "j"
            ),
            'p('class /= "world",'lang /= "ru"),
            "I",
            'div('lang /= "ru"),
            "b",
            'span(),
            "A",
            "d",
            "d",
            "o",
            'p('class /= "world")
          )
        }
      )
      assert {
        changes == Seq(
          create("1_4","div", XmlNs.html.uri),
          setAttr("1_4","lang", XmlNs.html.uri, "ru"),
          create("1_6","span", XmlNs.html.uri)
        )
      }
    }

    "should remove attribute" - {
      val changes = runDiff(
        original = { 'span('class /= "world",'style /= "margin: 10;", "q") },
        updated = { 'span('style /= "margin: 10;", "q") }
      )
      assert(changes == Seq(removeAttr("1", XmlNs.html.uri, "class")))
    }
    
    "should remove only subroot, not entire tree" - {
      val changes = runDiff(
        original = {
          'div('class /= "world",
            'div('class /= "world"),
            'div('class /= "hello",
              'span('name /= "cow",'class /= "hello",
                'span()
              ),
              'div('class /= "world",'style /= "margin: 10;")
            ),
            'button('class /= "world",
              'span('style /= "margin: 10;",'class /= "hello"),
              'span(),
              'div('lang /= "ru",'name /= "cow")
            ),
            "dasd"
          )
        },
        updated = {
          'div('class /= "world",
            'div('class /= "world"),
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

    "should save and restore state" - {
      val original = 'span('class /= "world",'style /= "margin: 10;", "q")
      val updated = 'span('style /= "margin: 10;", "q")
      val performer = new DiffTestChangesPerformer()
      val renderContext1 = DiffRenderContext[Nothing]()
      original(renderContext1)
      renderContext1.diff(DummyChangesPerformer)
      renderContext1.swap()
      val buffer = renderContext1.save()
      val renderContext2 = DiffRenderContext[Nothing](savedBuffer = Some(buffer))
      updated(renderContext2)
      renderContext2.diff(performer)
      val changes = performer.result
      assert(changes == Seq(removeAttr("1", XmlNs.html.uri, "class")))
    }

    "should consider two identical tags with different xmlNs as different tags" - {
      val changes = runDiff(
        original = {
          'div('svg('width /= "1"))
        },
        updated = {
          'div(ns.svg('svg)('width /= "1"))
        }
      )
      assert {
        changes == Seq(
          create(List(1, 1), "svg", XmlNs.svg.uri),
          setAttr(List(1, 1), "width", XmlNs.html.uri, "1")
        )
      }
    }

    "should consider two identical attributes with different xmlNs as different one" - {
      val changes = runDiff(
        original = {
          'div(
            'a /= "value",
            'b /= "value"
          )
        },
        updated = {
          'div(
            ns.mathml('a) /= "value",
            ns.svg('b) /= "value"
          )
        }
      )
      assert {
        changes == Seq(
          removeAttr(List(1), XmlNs.html.uri, "b"),
          removeAttr(List(1), XmlNs.html.uri, "a"),
          setAttr(List(1), "b", XmlNs.svg.uri, "value"),
          setAttr(List(1), "a", XmlNs.mathml.uri, "value")
        )
      }
    }
  }

  // -----------------------

  def runDiff(original: Document[Nothing], updated: Document[Nothing]): Seq[Change] = {
    val performer = new DiffTestChangesPerformer()
    val renderContext = DiffRenderContext[Nothing]()
    original(renderContext)
    renderContext.diff(DummyChangesPerformer)
    renderContext.swap()
    updated(renderContext)
    renderContext.diff(performer)
    performer.result
  }
}
