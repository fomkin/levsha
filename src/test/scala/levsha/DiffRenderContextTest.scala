package levsha

import levsha.impl.DiffRenderContext

import scala.collection.mutable
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
        original = { implicit rc => "m" },
        updated = { implicit rc =>
          'input('div('name /= "cow"))
        }
      )
      assert(
        changes == Seq(
          create("1", "input"),
          create("1_1", "div"),
          setAttr("1_1", "name", "cow")
        )
      )
    }

    "should text to node with chidlren" - {
      val changes = runDiff(
        original = { implicit rc =>
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
        updated = { implicit rc =>
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
          create("1_4","div"),
          setAttr("1_4","lang","ru"),
          create("1_6","span")
        )
      }
    }

    "should remove attribute" - {
      val changes = runDiff(
        original = { implicit rc => 'span('class /= "world",'style /= "margin: 10;", "q") },
        updated = { implicit rc =>'span('style /= "margin: 10;", "q") }
      )
      assert(changes == Seq(removeAttr("1", "class")))
    }
    
    "should remove only subroot, not entire tree" - {
      val changes = runDiff(
        original = { implicit rc =>
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
        updated = { implicit rc =>
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
  }

  // -----------------------

  def runDiff(original: RenderContext[Nothing] => RenderUnit, updated: RenderContext[Nothing] => RenderUnit): Seq[Change] = {
    val identIndex = mutable.Map.empty[Int, String]
    val performer = new DiffTestChangesPerformer()
    val rc1 = new DiffRenderContext[Nothing](identIndex = identIndex)
    val rc2 = new DiffRenderContext[Nothing](identIndex = identIndex)
    original(rc1)
    updated(rc2)
    rc2.diff(rc1, performer)
    performer.result
  }
}
