package levsha.impl

import java.nio.{ByteBuffer, ByteOrder}
import java.nio.charset.StandardCharsets

import levsha.RenderContext
import levsha.RenderUnit.{Attr, Misc, Node, Text}
import levsha.impl.DiffRenderContext._

import scala.annotation.switch
import internal.debox.IntStringMap

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

final class DiffRenderContext[M](mc: MiscCallback[M], bufferSize: Int) extends RenderContext[M] {

  if ((bufferSize == 0) || ((bufferSize & (bufferSize - 1)) != 0))
    throw new IllegalArgumentException("bufferSize should be power of two")

  private val counter = IdCounter()
  private var attrsOpened = false

  private val buffer = {
    val buff = ByteBuffer.allocateDirect(bufferSize)
    buff.order(ByteOrder.nativeOrder())
  }

  private var a = {
    buffer.limit(bufferSize / 2)
    buffer.slice().order(ByteOrder.nativeOrder())
  }

  private var b = {
    buffer.position(bufferSize / 2)
    buffer.limit(buffer.capacity)
    buffer.slice().order(ByteOrder.nativeOrder())
  }

  /** @inheritdoc */
  def openNode(name: String): Unit = {
    closeAttrs()
    attrsOpened = true
    counter.incId()
    counter.incLevel()
    a.put(OpOpen.toByte)
    a.putInt(name.hashCode)
    idents.update(name.hashCode, name)
  }

  /** @inheritdoc */
  def closeNode(name: String): Node.type = {
    closeAttrs()
    counter.decLevel()
    a.put(OpClose.toByte)
    Node
  }

  /** @inheritdoc */
  def setAttr(name: String, value: String): Attr.type = {
    idents.update(name.hashCode, name)
    a.put(OpAttr.toByte)
    a.putInt(name.hashCode)
    a.putShort(value.length.toShort)
    a.put(value.getBytes(StandardCharsets.UTF_8))
    Attr
  }

  /** @inheritdoc */
  def addTextNode(text: String): Text.type = {
    closeAttrs()
    counter.incId()
    a.put(OpText.toByte)
    a.putShort(text.length.toShort)
    a.put(text.getBytes(StandardCharsets.UTF_8))
    Text
  }

  /** @inheritdoc */
  def addMisc(misc: M): Misc = {
    mc(counter.currentString, misc)
    Misc
  }

  /** Swap buffers */
  def swap(): Unit = {
    a.flip()
    val t = b
    b = a
    a = t
    reset()
  }

  /** Cleanup current buffer */
  def reset(): Unit = {
    a.position(0)
    while (a.hasRemaining)
      a.putInt(0)
    a.clear()
  }

  def diff(performer: ChangesPerformer): Unit = {
    a.flip()
    counter.reset()

    while (a.hasRemaining) {
      val opA = op(a)
      val opB = op(b)
      if (opA == OpOpen && opB == OpOpen) {
        counter.incId()
        val tagA = readTag(a)
        if (tagA != readTag(b)) {
          performer.create(counter.currentString, idents(tagA))
          skipLoop(b)
          counter.incLevel()
          createLoop(a, performer)
          counter.decLevel()
        } else {
          compareAttrs(performer)
          counter.incLevel()
        }
      } else if (opA == OpText && opB == OpText) {
        counter.incId()
        if (!compareTexts()) {
          skipText(b)
          val textA = readText(a)
          performer.createText(counter.currentString, textA)
        } else {
          skipText(a)
          skipText(b)
        }
      } else if (opA == OpText && opB == OpOpen) {
        val aText = readText(a)
        readTag(b) // skip tag b
        counter.incId()
        performer.createText(counter.currentString, aText)
        skipLoop(b)
      } else if (opA == OpOpen && opB == OpText) {
        val tagA = readTag(a)
        counter.incId()
        skipText(b)
        performer.create(counter.currentString, idents(tagA))
        counter.incLevel()
        createLoop(a, performer)
        counter.decLevel()
      } else if (opA == OpClose && opB == OpClose) {
        counter.decLevel()
      } else if ((opA == OpClose || opA == OpEnd) && opB != OpClose && opB != OpEnd) {
        unOp(b)
        deleteLoop(b, performer)
        counter.decLevel()
      } else if (opA != OpClose && opA != OpEnd && (opB == OpClose || opB == OpEnd)) {
        unOp(a)
        createLoop(a, performer)
        counter.decLevel()
      }
    }
  }

  private def closeAttrs(): Unit = {
    if (attrsOpened) {
      attrsOpened = false
      a.put(OpLastAttr.toByte)
    }
  }

  private def op(x: ByteBuffer): Byte = {
    if (x.hasRemaining) x.get()
    else OpEnd.toByte
  }

  private def readTag(x: ByteBuffer): Int = {
    x.getInt()
  }

  private def skipText(x: ByteBuffer): Unit = {
    val len = x.getShort()
    x.position(x.position + len)
  }

  private def readText(x: ByteBuffer): String = {
    val len = x.getShort()
    readText(x, len)
  }

  private def readText(x: ByteBuffer, len: Int): String = {
    val bytes = new Array[Byte](len)
    x.get(bytes)
    new String(bytes, StandardCharsets.UTF_8)
  }

  private def skipAttr(x: ByteBuffer): Unit = {
    x.getInt()
    val len = x.getShort()
    x.position(x.position + len)
  }

  private def skipAttrText(x: ByteBuffer): Unit = {
    skipText(x)
  }

  /** true is further is attr; false if end of list */
  private def checkAttr(x: ByteBuffer): Boolean = {
    (op(x): @switch) match {
      case OpAttr => true
      case OpLastAttr => false
      case OpEnd => false
    }
  }

  private def readAttrRaw(x: ByteBuffer) = {
    x.getInt()
  }

  private def readAttr(x: ByteBuffer) = {
    idents(x.getInt())
  }

  private def readAttrText(x: ByteBuffer, len: Int) = {
    readText(x, len)
  }

  private def readAttrText(x: ByteBuffer) = {
    readText(x)
  }

  private def unOp(x: ByteBuffer) = {
    x.position(x.position - 1)
  }

  private def skipLoop(x: ByteBuffer): Unit = {
    val startLevel = counter.getLevel
    var continue = true
    while (continue) {
      (op(x): @switch) match {
        case OpClose =>
          if (counter.getLevel == startLevel) continue = false
          else counter.decLevel()
        case OpAttr => skipAttr(x)
        case OpLastAttr => // do nothing
        case OpText => skipText(x)
        case OpOpen =>
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
        case OpClose | OpEnd =>
          if (counter.getLevel == startLevel) continue = false
          else counter.decLevel()
        case OpAttr =>
          counter.decLevelTmp()
          performer.setAttr(counter.currentString, readAttr(x), readAttrText(x))
          counter.incLevel()
        case OpText =>
          counter.incId()
          performer.createText(counter.currentString, readText(x))
        case OpOpen =>
          counter.incId()
          performer.create(counter.currentString, idents(x.getInt()))
          counter.incLevel()
        case OpLastAttr => // do nothing
      }
    }
  }

  private def deleteLoop(x: ByteBuffer, performer: ChangesPerformer): Unit = {
    var continue = true
    while (continue) {
      (op(x): @switch) match {
        case OpOpen =>
          x.getInt() // skip tag
          counter.incId()
          performer.remove(counter.currentString)
          skipLoop(x)
        case OpText =>
          skipText(x)
          counter.incId()
          performer.remove(counter.currentString)
        case OpClose | OpEnd => continue = false
      }
    }
  }

  /* O(n^2). but it doesn't matter.
   most of nodes usually have just one or two attrs */
  private def compareAttrs(performer: ChangesPerformer): Unit = {
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
      val nameA = readAttrRaw(a)
      val valueLenA = a.getShort()
      val valuePosA = a.position()
      var needToSet = true
      b.position(startPosB)
      while (needToSet && checkAttr(b)) {
        val nameB = readAttrRaw(b)
        val valueLenB = b.getShort()
        val valuePosB = b.position()
        // First condition: name of attributes should be equals
        if (nameA == nameB) {
          if (valueLenA == valueLenB) {
            var i = 0
            var eq = true
            a.position(valuePosA)
            while (eq && i < valueLenA) {
              if (a.get() != b.get()) eq = false
              i += 1
            }
            if (eq) needToSet = false
          }
        }
        b.position(valuePosB + valueLenB)
      }
      if (needToSet) {
        a.position(valuePosA)
        val valueA = readAttrText(a, valueLenA)
        performer.setAttr(counter.currentString, idents(nameA), valueA)
      } else {
        a.position(valuePosA + valueLenA)
      }
    }
    b.position(endPosB)
  }

  private def compareTexts(): Boolean = {
    val startPosA = a.position()
    val startPosB = b.position()
    val aLen = a.getShort()
    val bLen = b.getShort()
    if (aLen != bLen) {
      a.position(startPosA)
      b.position(startPosB)
      false
    } else {
      var i = 0
      var equals = true
      while (equals && i < aLen) {
        if (a.get() != b.get())
          equals = false
        i += 1
      }
      a.position(startPosA)
      b.position(startPosB)
      equals
    }
  }
}

object DiffRenderContext {

  private[impl] val idents = IntStringMap.ofSize(100)

  def apply[MiscType](
    onMisc: MiscCallback[MiscType] = (_: String, _: MiscType) => (),
    bufferSize: Int = 1024 * 64
  ): DiffRenderContext[MiscType] = {
    new DiffRenderContext[MiscType](onMisc, bufferSize)
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
  final val OpOpen = 1
  final val OpClose = 2
  final val OpAttr = 3
  final val OpText = 4
  final val OpLastAttr = 5
  final val OpEnd = 6

  object IdCounter {
    def apply(maxLevel: Int = 256): IdCounter =
      new IdCounter(maxLevel)
  }

  final class IdCounter(maxLevel: Int) {

    private var level = 1
    private val buffer = ByteBuffer.allocate(maxLevel * 2)

    private def index = (level - 1) * 2

    // Initial limit
    buffer.limit(level * 2)

    def incLevel(): Unit = {
      level += 1
      buffer.limit(level * 2)
    }

    /** Just decreases level */
    def decLevelTmp(): Unit = {
      level -= 1
      buffer.limit(level * 2)
    }

    /** Resets current id and decreases level */
    def decLevel(): Unit = {
      buffer.putShort(index, 0)
      level -= 1
      buffer.limit(level * 2)
    }

    def incId(): Unit = {
      val updated = buffer.getShort(index) + 1
      buffer.putShort(index, updated.toShort)
    }

    def getLevel: Int = level

    def current: Array[Byte] = {
      val clone = new Array[Byte](buffer.limit)
      buffer.rewind()
      var i = 0
      while (buffer.hasRemaining) {
        clone(i) = buffer.get()
        i += 1
      }
      clone
    }

    def currentString: String = {
      val builder = StringBuilder.newBuilder
      buffer.rewind()
      while (buffer.hasRemaining) {
        builder.append(buffer.getShort())
        if (buffer.hasRemaining)
          builder.append('_')
      }
      builder.mkString
    }

    def reset(): Unit = {
      buffer.clear()
      while (buffer.hasRemaining)
        buffer.putInt(0)
      level = 1
      buffer.rewind()
      buffer.limit(level * 2)
    }
  }
}
