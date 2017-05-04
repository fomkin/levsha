package levsha.impl

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import levsha.RenderContext
import levsha.RenderUnit.{Attr, Misc, Node, Text}
import levsha.impl.DiffRenderContext._

import scala.annotation.switch
import scala.collection.mutable

final class DiffRenderContext[MiscType](
  onMisc: MiscCallback[MiscType] = (_: String, _: MiscType) => (),
  bufferSize: Int = 1024 * 64,
  private val identIndex: mutable.Map[Int, String] = mutable.Map.empty) extends RenderContext[MiscType] {

  /*
  Structures

  node {
    byte OPEN
    int tag_hash_code
    attr attr_list[]
    byte LAST_ATTR
    node|text child[]
    byte CLOSE
  }

  text {
    byte TEXT
    short length
    byte value[length]
  }

  attr {
    byte ATTR
    int attr_name_hash_code
    short length
    byte value[length]
  }

 */

  val idCounter = IdCounter()
  val buffer = ByteBuffer.allocateDirect(bufferSize)
  var attrsOpened = false

  def openNode(name: String): Unit = {
    closeAttrs()
    attrsOpened = true
    idCounter.incId()
    idCounter.incLevel()
    buffer.put(OPEN.toByte)
    buffer.putInt(name.hashCode)
    identIndex.update(name.hashCode, name)
  }

  def closeNode(name: String): Node.type = {
    closeAttrs()
    idCounter.decLevel()
    buffer.put(CLOSE.toByte)
    Node
  }

  def setAttr(name: String, value: String): Attr.type = {
    identIndex.update(name.hashCode, name)
    buffer.put(ATTR.toByte)
    buffer.putInt(name.hashCode)
    buffer.putShort(value.length.toShort)
    buffer.put(value.getBytes(StandardCharsets.UTF_8))
    Attr
  }

  def addTextNode(text: String): Text.type = {
    closeAttrs()
    idCounter.incId()
    buffer.put(TEXT.toByte)
    buffer.putShort(text.length.toShort)
    buffer.put(text.getBytes(StandardCharsets.UTF_8))
    Text
  }

  def addMisc(misc: MiscType): Misc = {
    onMisc(idCounter.currentString, misc)
    Misc
  }

  private def closeAttrs(): Unit = {
    if (attrsOpened) {
      attrsOpened = false
      buffer.put(LAST_ATTR.toByte)
    }
  }

  def diff(bContext: DiffRenderContext[MiscType], performer: ChangesPerformer): Unit = {

    val counter = IdCounter()
    val a = buffer
    val b = bContext.buffer

    a.flip()
    b.flip()

    def op(x: ByteBuffer) = {
      if (x.hasRemaining) x.get()
      else END.toByte
    }
    def readOpA() = op(a)
    def readOpB() = op(b)
    def readTagA() = a.getInt()
    def readTagB() = b.getInt()
    def skipText(x: ByteBuffer) = {
      val len = x.getShort()
      x.position(x.position + len)
    }
    def readText(x: ByteBuffer) = {
      val len = x.getShort()
      val bytes = new Array[Byte](len)
      x.get(bytes)
      new String(bytes, StandardCharsets.UTF_8)
    }
    def skipAttr(x: ByteBuffer) = {
      x.getInt()
      val len = x.getShort()
      x.position(x.position + len)
    }
    def skipAttrText(x: ByteBuffer) = skipText(x)
    /** true is further is attr; false if end of list */
    def checkAttr(x: ByteBuffer) = (op(x): @switch) match {
      case ATTR => true
      case LAST_ATTR => false
    }
    def readAttrRaw(x: ByteBuffer) = x.getInt()
    def readAttr(x: ByteBuffer) = identIndex(x.getInt())
    def readAttrText(x: ByteBuffer) = readText(x)
    def unOp(x: ByteBuffer) = x.position(x.position - 1)

    def skipLoop(x: ByteBuffer) = {
      val startLevel = counter.getLevel
      var continue = true
      while (continue) {
        (op(x): @switch) match {
          case CLOSE =>
            if (counter.getLevel == startLevel) continue = false
            else counter.decLevel()
          case ATTR => skipAttr(x)
          case LAST_ATTR => // do nothing
          case TEXT => skipText(x)
          case OPEN =>
            x.getInt() // skip tag
            counter.incLevel()
        }
      }
    }

    def createLoop(x: ByteBuffer) = {
      val startLevel = counter.getLevel
      var continue = true
      while (continue) {
        (op(x): @switch) match {
          case CLOSE =>
            if (counter.getLevel == startLevel) continue = false
            else counter.decLevel()
          case ATTR =>
            counter.decLevelTmp()
            performer.setAttr(counter.currentString, readAttr(x), readAttrText(x))
            counter.incLevel()
          case TEXT =>
            counter.incId()
            performer.createText(counter.currentString, readText(x))
          case OPEN =>
            counter.incId()
            performer.create(counter.currentString, identIndex(x.getInt()))
            counter.incLevel()
          case LAST_ATTR => // do nothing
        }
      }
    }

    def deleteLoop(x: ByteBuffer) = {
      val startLevel = counter.getLevel
      var continue = true
      while (continue) {
        (op(x): @switch) match {
          case CLOSE =>
            if (counter.getLevel == startLevel) continue = false
            else counter.decLevel()
          case ATTR =>
            // node will be removed so we don't need to remove the attr
            skipAttr(x)
          case TEXT =>
            skipText(x)
            counter.incId()
            performer.remove(counter.currentString)
          case OPEN =>
            x.getInt() // skip int
            counter.incId()
            performer.remove(counter.currentString)
            counter.incLevel()
          case LAST_ATTR => // do nothing
        }
      }
    }

    /* O(n^2) */
    def compareAttrs(a: ByteBuffer, b: ByteBuffer, performer: ChangesPerformer): Unit = {
      val startPosA = a.position()
      val startPosB = b.position()
      // Check the attrs were removed
      while (checkAttr(b)) {
        val attrNameB = readAttrRaw(b)
        var needToRemove = true
        skipAttrText(b)
        a.position(startPosA)
        while (needToRemove && checkAttr(a)) {
          val attrNameA = readAttrRaw(a)
          skipAttrText(a)
          if (attrNameA == attrNameB)
            needToRemove = false
        }
        if (needToRemove) {
          performer.removeAttr(counter.currentString, identIndex(attrNameB))
        }
      }
      // Check the attrs were added
      val endPosB = b.position()
      a.position(startPosA)
      while (checkAttr(a)) {
        val attrNameA = readAttrRaw(a)
        val attrValueA = readAttrText(a)
        var needToSet = true
        b.position(startPosB)
        while (needToSet && checkAttr(b)) {
          val attrNameB = readAttrRaw(b)
          val attrValueB = readAttrText(b)
          if (attrNameA == attrNameB && attrValueA == attrValueB)
            needToSet = false
        }
        if (needToSet) {
          performer.setAttr(counter.currentString, identIndex(attrNameA), attrValueA)
        }
      }
      b.position(endPosB)
    }

    while (a.hasRemaining) {
      val opA = readOpA()
      val opB = readOpB()
      if (opA == OPEN && opB == OPEN) {
        counter.incId()
        val tagA = readTagA()
        if (tagA != readTagB()) {
          performer.create(counter.currentString, identIndex(tagA))
          skipLoop(b)
          counter.incLevel()
            createLoop(a)
          counter.decLevel()
        } else {
          compareAttrs(a, b, performer)
          counter.incLevel()
        }
      } else if (opA == TEXT && opB == TEXT) {
        val textA = readText(a)
        val textB = readText(b)
        counter.incId()
        if (textA != textB) {
          performer.createText(counter.currentString, textA)
        }
      } else if (opA == TEXT && opB == OPEN) {
        val aText = readText(a)
        readTagB() // skip tag b
        counter.incId()
        performer.createText(counter.currentString, aText)
        skipLoop(b)
      } else if (opA == OPEN && opB == TEXT) {
        val tagA = readTagA()
        counter.incId()
        skipText(b)
        performer.create(counter.currentString, identIndex(tagA))
        counter.incLevel()
        createLoop(a)
        counter.decLevel()
      } else if (opA == CLOSE && opB == CLOSE) {
        counter.decLevel()
//      } else if (opA == ATTR && opB == ATTR) {
//        val attrA = readAttr(a)
//        if (attrA != readAttr(b)) {
//          counter.decLevelTmp()
//          performer.setAttr(counter.currentString, attrA, readAttrText(a))
//          readAttrText(b) // skip attr text
//          counter.incLevel()
//        }
//      } else if (opA == ATTR && opB != ATTR) {
//        unOp(b)
//        counter.decLevelTmp()
//        performer.setAttr(counter.currentString, readAttr(a), readAttrText(a))
//        counter.incLevel()
//      } else if (opA != ATTR && opB == ATTR) {
//        unOp(a)
//        counter.decLevelTmp()
//        performer.removeAttr(counter.currentString, readAttr(b))
//        skipAttrText(b)
//        counter.incLevel()
      } else if ((opA == CLOSE || opA == END) && opB != CLOSE && opB != END) {
        unOp(b)
        deleteLoop(b)
      } else if (opA != CLOSE && opA != END && (opB == CLOSE || opB == END)) {
        unOp(a)
        createLoop(a)
        counter.decLevel()
      }
    }
  }
}

object DiffRenderContext {

  trait ChangesPerformer {
    def removeAttr(id: String, name: String): Unit
    def remove(id: String): Unit
    def setAttr(id: String, name: String, value: String): Unit
    def createText(id: String, text: String): Unit
    def create(id: String, tag: String): Unit
  }

  final class DummyChangesPerformer extends ChangesPerformer {
    def removeAttr(id: String, name: String): Unit = ()
    def remove(id: String): Unit = ()
    def setAttr(id: String, name: String, value: String): Unit = ()
    def createText(id: String, text: String): Unit = ()
    def create(id: String, tag: String): Unit = ()
  }

  type MiscCallback[MiscType] = (String, MiscType) => Unit

  // Opcodes
  final val OPEN = 1
  final val CLOSE = 2
  final val ATTR = 3
  final val TEXT = 4
  final val LAST_ATTR = 5
  final val END = 6

  def opLabel(op: Byte): String = (op: @switch) match {
    case OPEN => "OPEN"
    case CLOSE => "CLOSE"
    case ATTR => "ATTR"
    case TEXT => "TEXT"
    case LAST_ATTR => "LAST_ATTR"
    case END => "END"
    case _ => "UNKNOWN"
  }

  object IdCounter {
    def apply(maxLevel: Int = 256): IdCounter =
      new IdCounter(maxLevel)
  }

  final class IdCounter(maxLevel: Int) {

    private var level = 1
    private val buff = ByteBuffer.allocate(maxLevel * 2)

    private def index = (level - 1) * 2

    // Initial limit
    buff.limit(level * 2)

    def incLevel(): Unit = {
      level += 1
      buff.limit(level * 2)
    }

    /** Just decreases level */
    def decLevelTmp(): Unit = {
      level -= 1
      buff.limit(level * 2)
    }

    /** Resets current id and decreases level */
    def decLevel(): Unit = {
      buff.putShort(index, 0)
      level -= 1
      buff.limit(level * 2)
    }

    def incId(): Unit = {
      val updated = buff.getShort(index) + 1
      buff.putShort(index, updated.toShort)
    }

    def getLevel: Int = level

    def current: Array[Byte] = {
      val clone = new Array[Byte](buff.limit)
      buff.rewind()
      var i = 0
      while (buff.hasRemaining) {
        clone(i) = buff.get()
        i += 1
      }
      clone
    }

    def currentString: String = {
      val builder = StringBuilder.newBuilder
      buff.rewind()
      while (buff.hasRemaining) {
        builder.append(buff.getShort())
        if (buff.hasRemaining)
          builder.append('_')
      }
      builder.mkString
    }

    def reset(): Unit = {
      level = 1
      buff.clear()
      while (buff.hasRemaining)
        buff.putInt(0)
    }
  }
}
