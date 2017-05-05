package levsha.impl

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import levsha.RenderContext
import levsha.RenderUnit.{Attr, Misc, Node, Text}
import levsha.impl.DiffRenderContext._

import scala.annotation.switch
import scala.collection.mutable

final class DiffRenderContext[M](
  mc: MiscCallback[M],
  bufferSize: Int,
  private val idents: mutable.Map[Int, String]) extends RenderContext[M] {

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

  private val counter = IdCounter()
  private val buffer = ByteBuffer.allocateDirect(bufferSize)
  private var attrsOpened = false

  def openNode(name: String): Unit = {
    closeAttrs()
    attrsOpened = true
    counter.incId()
    counter.incLevel()
    buffer.put(OPEN.toByte)
    buffer.putInt(name.hashCode)
    idents.update(name.hashCode, name)
  }

  def closeNode(name: String): Node.type = {
    closeAttrs()
    counter.decLevel()
    buffer.put(CLOSE.toByte)
    Node
  }

  def setAttr(name: String, value: String): Attr.type = {
    idents.update(name.hashCode, name)
    buffer.put(ATTR.toByte)
    buffer.putInt(name.hashCode)
    buffer.putShort(value.length.toShort)
    buffer.put(value.getBytes(StandardCharsets.UTF_8))
    Attr
  }

  def addTextNode(text: String): Text.type = {
    closeAttrs()
    counter.incId()
    buffer.put(TEXT.toByte)
    buffer.putShort(text.length.toShort)
    buffer.put(text.getBytes(StandardCharsets.UTF_8))
    Text
  }

  def addMisc(misc: M): Misc = {
    mc(counter.currentString, misc)
    Misc
  }

  private def closeAttrs(): Unit = {
    if (attrsOpened) {
      attrsOpened = false
      buffer.put(LAST_ATTR.toByte)
    }
  }

  def diff(bContext: DiffRenderContext[M], performer: ChangesPerformer): Unit = {

    val a = buffer
    val b = bContext.buffer

    a.flip()
    b.flip()
    counter.reset()

    while (a.hasRemaining) {
      val opA = op(a)
      val opB = op(b)
      if (opA == OPEN && opB == OPEN) {
        counter.incId()
        val tagA = readTag(a)
        if (tagA != readTag(b)) {
          performer.create(counter.currentString, idents(tagA))
          skipLoop(b)
          counter.incLevel()
          createLoop(a, performer)
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
        readTag(b) // skip tag b
        counter.incId()
        performer.createText(counter.currentString, aText)
        skipLoop(b)
      } else if (opA == OPEN && opB == TEXT) {
        val tagA = readTag(a)
        counter.incId()
        skipText(b)
        performer.create(counter.currentString, idents(tagA))
        counter.incLevel()
        createLoop(a, performer)
        counter.decLevel()
      } else if (opA == CLOSE && opB == CLOSE) {
        counter.decLevel()
      } else if ((opA == CLOSE || opA == END) && opB != CLOSE && opB != END) {
        unOp(b)
        deleteLoop(b, performer)
        counter.decLevel()
      } else if (opA != CLOSE && opA != END && (opB == CLOSE || opB == END)) {
        unOp(a)
        createLoop(a, performer)
        counter.decLevel()
      }
    }
  }

  private def op(x: ByteBuffer): Byte = {
    if (x.hasRemaining) x.get()
    else END.toByte
  }

  private def readTag(x: ByteBuffer): Int = x.getInt()

  private def skipText(x: ByteBuffer): Unit = {
    val len = x.getShort()
    x.position(x.position + len)
  }

  private def readText(x: ByteBuffer): String = {
    val len = x.getShort()
    val bytes = new Array[Byte](len)
    x.get(bytes)
    new String(bytes, StandardCharsets.UTF_8)
  }

  private def skipAttr(x: ByteBuffer): Unit = {
    x.getInt()
    val len = x.getShort()
    x.position(x.position + len)
  }

  private def skipAttrText(x: ByteBuffer): Unit = skipText(x)

  /** true is further is attr; false if end of list */
  private def checkAttr(x: ByteBuffer): Boolean = {
    (op(x): @switch) match {
      case ATTR => true
      case LAST_ATTR => false
    }
  }

  private def readAttrRaw(x: ByteBuffer) = x.getInt()
  private def readAttr(x: ByteBuffer) = idents(x.getInt())
  private def readAttrText(x: ByteBuffer) = readText(x)
  private def unOp(x: ByteBuffer) = x.position(x.position - 1)

  private def skipLoop(x: ByteBuffer): Unit = {
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

  private def createLoop(x: ByteBuffer, performer: ChangesPerformer): Unit = {
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
          performer.create(counter.currentString, idents(x.getInt()))
          counter.incLevel()
        case LAST_ATTR => // do nothing
      }
    }
  }

  private def deleteLoop(x: ByteBuffer, performer: ChangesPerformer): Unit = {
    var continue = true
    while (continue) {
      (op(x): @switch) match {
        case OPEN =>
          x.getInt() // skip tag
          counter.incId()
          performer.remove(counter.currentString)
          skipLoop(x)
        case TEXT =>
          skipText(x)
          counter.incId()
          performer.remove(counter.currentString)
        case CLOSE | END => continue = false
      }
    }
  }

  /* O(n^2) */
  private def compareAttrs(a: ByteBuffer, b: ByteBuffer, performer: ChangesPerformer): Unit = {
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
        performer.removeAttr(counter.currentString, idents(attrNameB))
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
        performer.setAttr(counter.currentString, idents(attrNameA), attrValueA)
      }
    }
    b.position(endPosB)
  }
}

object DiffRenderContext {

  def apply[MiscType](
    onMisc: MiscCallback[MiscType] = (_: String, _: MiscType) => (),
    bufferSize: Int = 1024 * 64,
    identIndex: mutable.Map[Int, String] = mutable.Map.empty
  ): DiffRenderContext[MiscType] = {
    new DiffRenderContext[MiscType](onMisc, bufferSize, identIndex)
  }

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
      buff.clear()
      while (buff.hasRemaining)
        buff.putInt(0)
      level = 1
      buff.rewind()
      buff.limit(level * 2)
    }
  }
}
