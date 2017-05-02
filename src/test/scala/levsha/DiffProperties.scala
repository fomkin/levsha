package levsha

import levsha.impl.DiffRenderContext
import levsha.impl.DiffRenderContext.{ChangesPerformer, DummyChangesPerformer}
import org.scalacheck.{Gen, Properties}
import org.scalacheck._

import scala.collection.mutable

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object DiffProperties extends Properties("Diff") {

  import Document._

  property("doesn't fail on arbitrary trees") = {
    val gen = for (a <- genDocument; b <- genDocument)
      yield (a, b)
    Prop.forAll(gen) {
      case (a, b) =>
        val index = mutable.Map.empty[Int, String]
        val dummyPerformer = new DummyChangesPerformer()
        val rcFirst = new DiffRenderContext[Nothing](identIndex = index)
        val rcSecond = new DiffRenderContext[Nothing](identIndex = index)
        a(rcFirst)
        b(rcSecond)
        rcSecond.diff(rcFirst, dummyPerformer)
        true
    }
  }

}

sealed trait Document {
  def apply(rc: RenderContext[Nothing]): Unit = this match {
    case Document.Text(text) => rc.addTextNode(text)
    case Document.Element(name, attrs, xs) =>
      rc.openNode(name)
      attrs foreach {
        case (attr, value) =>
          rc.setAttr(attr, value)
      }
      xs.foreach(x => x(rc))
      rc.closeNode(name)
  }
}

object Document {

  case class Text(value: String) extends Document
  case class Element(name: String, attrs: Map[String, String], xs: Seq[Document]) extends Document

  val genAttr = {
    Gen.frequency(
      (5, "class" -> "hello"),
      (7, "class" -> "world"),
      (1, "name" -> "cow"),
      (2, "lang" -> "ru"),
      (2, "lang" -> "ru"),
      (1, "style" -> "margin: 10;"),
      (1, "style" -> "padding: 10;")
    )
  }

  val genAttrs = {
    Gen.choose(0, 3) flatMap { s =>
      Gen.listOfN(s, genAttr).map(_.toMap)
    }
  }

  val genTag = {
    Gen.frequency(
      (8, "div"),
      (4, "span"),
      (1, "p"),
      (1, "input"),
      (1, "button")
    )
  }

  val genText = {
    Gen.alphaStr.map(s => Text(s))
  }

  val genElement = Gen.sized { size =>
    for {
      name <- genTag
      attrs <- genAttrs
      len <- Gen.choose(0, size)
      gen = Gen.resize(size / (len + 1), genDocument)
      xs <- Gen.listOfN(len, gen)
    } yield {
      Element(name, attrs, xs)
    }
  }

  def genDocument: Gen[Document] = Gen.lzy {
    Gen.oneOf(genText, genElement)
  }
}
