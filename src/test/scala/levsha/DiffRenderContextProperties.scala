package levsha

import levsha.Change.DiffTestChangesPerformer
import levsha.impl.DiffRenderContext
import levsha.impl.DiffRenderContext.DummyChangesPerformer
import org.scalacheck.{Gen, Properties, _}

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
        val rcFirst = DiffRenderContext[Nothing](identIndex = index)
        val rcSecond = DiffRenderContext[Nothing](identIndex = index)
        a(rcFirst)
        b(rcSecond)
        rcSecond.diff(rcFirst, dummyPerformer)
        true
    }
  }

  property("generate valid changes set") = {
    Prop.forAll(ChangesTrial.genChangesTrial) { trial =>
      val index = mutable.Map.empty[Int, String]
      val performer = new DiffTestChangesPerformer()
      val rcFirst = DiffRenderContext[Nothing](identIndex = index)
      val rcSecond = DiffRenderContext[Nothing](identIndex = index)
      trial.originalDocument(rcFirst)
      trial.newDocument(rcSecond)
      rcSecond.diff(rcFirst, performer)
      val sample = trial.changes.sorted
      val res = performer.result.sorted
      assert(sample == res, {
        s"\nShould be:\n  ${sample.mkString("\n  ")}" +
          s"\nBut:\n  ${res.mkString("\n  ")}"
      })
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
  override def toString: String = {
    Document.docToString("", this)
  }
}

object Document {

  case class Text(value: String) extends Document
  case class Element(name: String, attrs: Map[String, String], xs: Seq[Document]) extends Document

  def docToString(level: String, doc: Document): String = {
    def attrsToString(attrs: Map[String, String]) =
      attrs
        .map { case (name, value) => s"""'$name /= "$value"""" }
        .mkString(",")
    doc match {
      case Document.Text(value) => s"""$level"$value""""
      case Document.Element(name, attrs, Nil) =>
        s"$level'$name(${attrsToString(attrs)})"
      case Document.Element(name, attrs, xs) =>
        val children = xs.map(docToString(level + "  ", _)).mkString(",\n")
        s"$level'$name(${attrsToString(attrs)},\n$children\n$level)"
    }
  }

  val genAttr = {
    Gen.frequency(
      (5, "class" -> "hello"),
      (7, "class" -> "world"),
      (7, "class" -> "cow"),
      (7, "class" -> "lol"),
      (1, "name" -> "cow"),
      (1, "name" -> "I"),
      (1, "name" -> "am"),
      (1, "name" -> "Korolev"),
      (2, "lang" -> "ru"),
      (2, "lang" -> "ru"),
      (3, "lang" -> "en"),
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
      (1, "button"),
      (1, "ul"),
      (1, "li"),
      (1, "form")
    )
  }

  val genText = {
    Gen.nonEmptyListOf(Gen.alphaChar).map(s => Text(s.mkString))
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
    Gen.frequency(
      2 -> genText,
      5 -> genElement
    )
  }
}

case class ChangesTrial(
    originalDocument: Document,
    newDocument: Document,
    changes: Seq[Change]
) {
  import Document._
  override def toString: String = {
    s"""Changes Trial
       |-------------
       |Original Document:
       |${docToString("  ", originalDocument)}
       |
       |New Document:
       |${docToString("  ", newDocument)}
     """.stripMargin
  }
}

object ChangesTrial {

  import Document._

  type FlatDoc = List[(List[Int], Document)]

  private def makeFlat(id: List[Int], d: Document): FlatDoc = d match {
    case t: Text => List(id -> t)
    case el: Element =>
      (id, el.copy(xs = Nil)) :: {
        el.xs.toList.zipWithIndex flatMap {
          case (child, i) =>
            makeFlat(id :+ (i + 1), child)
        }
      }
  }

  private def makeUnflat(flatDoc: FlatDoc) = {
    val (1 :: Nil, root) :: children = flatDoc.sortBy(_._1.toIterable)
    def findChildren(id: List[Int]): List[Document] = {
      def checkId(childId: List[Int]) =
        childId.length == (id.length + 1) && childId.startsWith(id)
      children collect {
        case (childId, t: Text) if checkId(childId) => t
        case (childId, e: Element) if checkId(childId) => e.copy(xs = findChildren(childId))
      }
    }
    root match {
      case _: Text => root
      case e: Element => e.copy(xs = findChildren(List(1)))
    }
  }

  sealed trait Intent {
    def id: List[Int]
  }

  object Intent {

    case class SetAttr(id: List[Int], attr: String, value: String) extends Intent
    case class RemoveAttr(id: List[Int], attr: String) extends Intent
    case class Append(id: List[Int], doc: Document) extends Intent
    case class Delete(id: List[Int]) extends Intent
    case class Replace(id: List[Int], doc: Document) extends Intent

    def genSetAttrIntent(id: List[Int]): Gen[SetAttr] = Document.genAttr map {
      case (attr, value) => SetAttr(id, attr, value)
    }
    def genRemoveAttrIntent(id: List[Int]): Gen[RemoveAttr] = Document.genAttr map {
      case (attr, _) => RemoveAttr(id, attr)
    }
    def genAppendIntent(id: List[Int]): Gen[Append] = Document.genDocument map { doc =>
      Append(id, doc)
    }
    def genReplaceIntent(id: List[Int]): Gen[Replace] = Document.genDocument map { doc =>
      Replace(id, doc)
    }
    def genDeleteIntent(id: List[Int]): Gen[Delete] = Gen.const { Delete(id) }

    def genIntents(flatDoc: FlatDoc): Gen[Seq[Intent]] = {
      for {
        intentNum <- Gen.choose(1, flatDoc.length)
        intentGens = (0 until intentNum) map { _ =>
          for {
            index <- Gen.choose(0, flatDoc.length - 1)
            (id, doc) = flatDoc(index)
            intentGen <- doc match {
              case _: Text => genReplaceIntent(id)
              case el: Element =>
                Gen.oneOf(
                  genSetAttrIntent(id).filter(setAttr => !el.attrs.contains(setAttr.attr)),
                  genRemoveAttrIntent(id).filter(rmAttr => el.attrs.contains(rmAttr.attr)),
                  genAppendIntent(id),
                  genDeleteIntent(id).filter(_ => id != List(1)),
                  genReplaceIntent(id).filter {
                    case Intent.Replace(_, _: Text) => true
                    case Intent.Replace(_, elToReplace: Element) => elToReplace.name != el.name
                  }
                )
            }
          } yield intentGen
        }
        intents <- Gen.sequence(intentGens)
      } yield {
        import collection.JavaConverters._
        val xs = intents.asScala
        xs.sortBy(_.id.toIterable).foldLeft(List.empty[Intent]) {
          case (acc, intent) if !acc.exists(x => intent.id.startsWith(x.id)) =>
            intent :: acc
          case (acc, _) => acc
        }
      }
    }
  }

  val genChangesTrial = {
    def flatDocToChanges(doc: (List[Int], Document)) = doc match {
      case (id, Text(value)) => List(Change.createText(id, value))
      case (id, Element(name, attrs, _)) =>
        Change.create(id, name) :: attrs.toList.map {
          case (attr, value) => Change.setAttr(id, attr, value)
        }
    }
    for {
      originalDocument <- genDocument
      flatDocument = makeFlat(List(1), originalDocument)
      intents <- Intent.genIntents(flatDocument)
    } yield {
      val changes = {
        val xs: Seq[Change] = intents.flatMap {
          case Intent.Replace(id, doc) => makeFlat(id, doc).flatMap(flatDocToChanges)
          case Intent.Append(id, doc) =>
            val nextId = flatDocument
              .filter { case (i, _) => i.length == id.length + 1 && i.startsWith(id) }
              .sortBy(_._1.toIterable)
              .lastOption
              .map(_._1.last + 1)
              .getOrElse(1)
            makeFlat(id :+ nextId, doc).flatMap(flatDocToChanges)
          case Intent.Delete(id) =>
            val parent = id.dropRight(1)
            val thisIndex = id.last
            val toRemove = flatDocument.collect {
              case (i, _)
                  if i.startsWith(parent) &&
                    i.length == id.length &&
                    i.last > thisIndex =>
                i
            }
            //(id :: toRemove).map(Change.remove)
            val idToRemove = if (toRemove.isEmpty) id else toRemove.last
            Seq(Change.remove(idToRemove))
          case Intent.SetAttr(id, attr, value) => Seq(Change.setAttr(id, attr, value))
          case Intent.RemoveAttr(id, attr) => Seq(Change.removeAttr(id, attr))
        }
        xs.filter {
            case _: Change.remove => true
            case change =>
              !xs.exists {
                case Change.remove(removeId) => change.id.startsWith(removeId)
                case _ => false
              }
          }
          .sorted
          .distinct
      }

      val updatedFlatDocument = changes.foldLeft(flatDocument.toMap) {
        case (acc, Change.create(id, name)) =>
          acc
            .get(id)
            .fold(acc) {
              case Element(`name`, _, _) => acc
              case _ => acc.filter(!_._1.startsWith(id))
            }
            .updated(id, Element(name, Map.empty, Nil))
        case (acc, Change.createText(id, text)) =>
          acc
            .filter(!_._1.startsWith(id))
            .updated(id, Text(text))
        case (acc, Change.remove(id)) => acc.filter(!_._1.startsWith(id))
        case (acc, Change.removeAttr(id, attr)) if acc.contains(id) =>
          acc(id) match {
            case _: Text => acc
            case el: Element =>
              val updatedEl = el.copy(attrs = el.attrs - attr)
              acc + (id -> updatedEl)
          }
        case (acc, Change.setAttr(id, name, value)) if acc.contains(id) =>
          acc(id) match {
            case _: Text => acc
            case el: Element =>
              val updatedEl = el.copy(attrs = el.attrs + (name -> value))
              acc + (id -> updatedEl)
          }
      }
      ChangesTrial(
        originalDocument,
        makeUnflat(updatedFlatDocument.toList),
        changes
      )
    }
  }
}
