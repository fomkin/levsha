package levsha

import levsha.impl.DiffRenderContext
import levsha.impl.DiffRenderContext.ChangesPerformer

import scala.collection.mutable

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object TemplateDiffTest extends utest.TestSuite {

  import TemplateDiffTestComponents._
  import utest._

  val tests = this {
    'DiffRenderContext {
      val identIndex = mutable.Map.empty[Int, String]
      val rc1 = new DiffRenderContext[Nothing](identIndex = identIndex)
      val rc2 = new DiffRenderContext[Nothing](identIndex = identIndex)
      val template1 = {
        implicit val rc = rc1
        component1()
      }
      val template2 = {
        implicit val rc = rc2
        component2()
      }
      rc2.diff(rc1, performer =
        new ChangesPerformer {
          def removeAttr(id: String, name: String): Unit = println(s"remove attr $name at $id")
          def create(id: String, tag: String): Unit = println(s"create $tag at $id")
          def createText(id: String, text: String): Unit = println(s"create text '$text' at $id")
          def remove(id: String): Unit = println(s"remove $id")
          def setAttr(id: String, name: String, value: String): Unit = println(s"set attr $name to $value at $id")
        }
      )
    }
  }
}

object TemplateDiffTestComponents {

  val dsl = new TemplateDsl[Nothing]()

  import dsl._

  def component1()(implicit rc: DiffRenderContext[Nothing]) = {
    'div(
      'div(
        'class /= "first", 'name /= "unknown",
        'div(), 'div()
      ),
      'div('input(), 'input(), 'input()),
      'div('span(), 'input())
    )
  }
  def component2()(implicit rc: DiffRenderContext[Nothing]) = {
    /*
    create span at 1_1_1
    create text 'Cow' at 1_1_2
    create div at 1_1_3
    create p at 1_2
    create text 'Hello' at 1_2_1
    create div at 1_3_1
    remove 1_3_2
     */
    'div(
      'div(
        'class /= "first",
        'span('class /= "lol"), "Cow", 'div()
      ),
      'p("Hello"),
      'div('div())
    )
  }
}
