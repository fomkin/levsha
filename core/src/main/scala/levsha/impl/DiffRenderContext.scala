package levsha.impl

import java.nio.{ByteBuffer, ByteOrder}
import java.nio.charset.StandardCharsets

import levsha.{Id, IdBuilder, RenderContext}
import levsha.impl.DiffRenderContext._
import levsha.impl.internal.Op._

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

final class DiffRenderContext[-M](mc: MiscCallback[M], bufferSize: Int) extends RenderContext[M] {

  if ((bufferSize == 0) || ((bufferSize & (bufferSize - 1)) != 0))
    throw new IllegalArgumentException("bufferSize should be power of two")

  private val idb = IdBuilder()
  private var attrsOpened = false

  private val buffer = {
    val buff = ByteBuffer.allocateDirect(bufferSize)
    buff.order(ByteOrder.nativeOrder())
  }

  private var lhs = {
    buffer.limit(bufferSize / 2)
    buffer
      .slice()
      .order(ByteOrder.nativeOrder())
  }

  private var rhs = {
    buffer.position(bufferSize / 2)
    buffer.limit(buffer.capacity)
    buffer
      .slice()
      .order(ByteOrder.nativeOrder())
      .flip()
      .asInstanceOf[ByteBuffer]
  }

  def openNode(name: String): Unit = {
    closeAttrs()
    attrsOpened = true
    idb.incId()
    idb.incLevel()
    lhs.put(OpOpen.toByte)
    lhs.putInt(name.hashCode)
    idents.update(name.hashCode, name)
  }

  def closeNode(name: String): Unit = {
    closeAttrs()
    idb.decLevel()
    lhs.put(OpClose.toByte)
  }

  def setAttr(name: String, value: String): Unit = {
    val bytes = value.getBytes(StandardCharsets.UTF_8)
    idents.update(name.hashCode, name)
    lhs.put(OpAttr.toByte)
    lhs.putInt(name.hashCode)
    lhs.putShort(bytes.length.toShort)
    lhs.put(bytes)
  }

  def addTextNode(text: String): Unit = {
    val bytes = text.getBytes(StandardCharsets.UTF_8)
    closeAttrs()
    idb.incId()
    lhs.put(OpText.toByte)
    lhs.putShort(bytes.length.toShort)
    lhs.put(bytes)
  }

  def addMisc(misc: M): Unit = {
    idb.decLevelTmp()
    mc(idb.mkId, misc)
    idb.incLevel()
  }

  /** Swap buffers */
  def swap(): Unit = {
    val t = rhs
    rhs = lhs
    lhs = t
    reset()
  }

  /** Cleanup current buffer */
  def reset(): Unit = {
    idb.reset()
    lhs.position(0)
    while (lhs.hasRemaining)
      lhs.put(0.toByte)
    lhs.clear()
  }

  def diff(performer: ChangesPerformer): Unit = {
    lhs.flip()
    idb.reset()

    while (lhs.hasRemaining) {
      val opA = op(lhs)
      val opB = op(rhs)
      if (opA == OpOpen && opB == OpOpen) {
        idb.incId()
        val tagA = readTag(lhs)
        if (tagA != readTag(rhs)) {
          performer.create(idb.mkId, idents(tagA))
          skipLoop(rhs)
          idb.incLevel()
          createLoop(lhs, performer)
          idb.decLevel()
        } else {
          compareAttrs(performer)
          idb.incLevel()
        }
      } else if (opA == OpText && opB == OpText) {
        idb.incId()
        if (!compareTexts()) {
          skipText(rhs)
          val textA = readText(lhs)
          performer.createText(idb.mkId, textA)
        } else {
          skipText(lhs)
          skipText(rhs)
        }
      } else if (opA == OpText && opB == OpOpen) {
        val aText = readText(lhs)
        readTag(rhs) // skip tag b
        idb.incId()
        performer.createText(idb.mkId, aText)
        skipLoop(rhs)
      } else if (opA == OpOpen && opB == OpText) {
        val tagA = readTag(lhs)
        idb.incId()
        skipText(rhs)
        performer.create(idb.mkId, idents(tagA))
        idb.incLevel()
        createLoop(lhs, performer)
        idb.decLevel()
      } else if (opA == OpClose && opB == OpClose) {
        idb.decLevel()
      } else if ((opA == OpClose || opA == OpEnd) && opB != OpClose && opB != OpEnd) {
        unOp(rhs)
        deleteLoop(rhs, performer)
        idb.decLevel()
      } else if (opA != OpClose && opA != OpEnd && (opB == OpClose || opB == OpEnd)) {
        unOp(lhs)
        createLoop(lhs, performer)
        idb.decLevel()
      }
    }

    lhs.rewind()
    rhs.rewind()
  }

  private def closeAttrs(): Unit = {
    if (attrsOpened) {
      attrsOpened = false
      lhs.put(OpLastAttr.toByte)
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
    val startLevel = idb.getLevel
    var continue = true
    while (continue) {
      (op(x): @switch) match {
        case OpClose =>
          if (idb.getLevel == startLevel) continue = false
          else idb.decLevel()
        case OpAttr => skipAttr(x)
        case OpLastAttr => // do nothing
        case OpText => skipText(x)
        case OpOpen =>
          x.getInt() // skip tag
          idb.incLevel()
      }
    }
  }

  private def createLoop(x: ByteBuffer, performer: ChangesPerformer): Unit = {
    val startLevel = idb.getLevel
    var continue = true
    while (continue) {
      (op(x): @switch) match {
        case OpClose | OpEnd =>
          if (idb.getLevel == startLevel) continue = false
          else idb.decLevel()
        case OpAttr =>
          idb.decLevelTmp()
          performer.setAttr(idb.mkId, readAttr(x), readAttrText(x))
          idb.incLevel()
        case OpText =>
          idb.incId()
          performer.createText(idb.mkId, readText(x))
        case OpOpen =>
          idb.incId()
          performer.create(idb.mkId, idents(x.getInt()))
          idb.incLevel()
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
          idb.incId()
          performer.remove(idb.mkId)
          skipLoop(x)
        case OpText =>
          skipText(x)
          idb.incId()
          performer.remove(idb.mkId)
        case OpClose | OpEnd => continue = false
      }
    }
  }

  /* O(n^2). but it doesn't matter.
   most of nodes usually have just one or two attrs */
  private def compareAttrs(performer: ChangesPerformer): Unit = {
    val startPosA = lhs.position()
    val startPosB = rhs.position()
    // Check the attrs were removed
    while (checkAttr(rhs)) {
      val attrNameB = readAttrRaw(rhs)
      var needToRemove = true
      skipAttrText(rhs)
      lhs.position(startPosA)
      while (needToRemove && checkAttr(lhs)) {
        val attrNameA = readAttrRaw(lhs)
        skipAttrText(lhs)
        if (attrNameA == attrNameB)
          needToRemove = false
      }
      if (needToRemove) {
        performer.removeAttr(idb.mkId, idents(attrNameB))
      }
    }
    // Check the attrs were added
    val endPosB = rhs.position()
    lhs.position(startPosA)
    while (checkAttr(lhs)) {
      val nameA = readAttrRaw(lhs)
      val valueLenA = lhs.getShort()
      val valuePosA = lhs.position()
      var needToSet = true
      rhs.position(startPosB)
      while (needToSet && checkAttr(rhs)) {
        val nameB = readAttrRaw(rhs)
        val valueLenB = rhs.getShort()
        val valuePosB = rhs.position()
        // First condition: name of attributes should be equals
        if (nameA == nameB) {
          if (valueLenA == valueLenB) {
            var i = 0
            var eq = true
            lhs.position(valuePosA)
            while (eq && i < valueLenA) {
              if (lhs.get() != rhs.get()) eq = false
              i += 1
            }
            if (eq) needToSet = false
          }
        }
        rhs.position(valuePosB + valueLenB)
      }
      if (needToSet) {
        lhs.position(valuePosA)
        val valueA = readAttrText(lhs, valueLenA)
        performer.setAttr(idb.mkId, idents(nameA), valueA)
      } else {
        lhs.position(valuePosA + valueLenA)
      }
    }
    rhs.position(endPosB)
  }

  private def compareTexts(): Boolean = {
    val startPosA = lhs.position()
    val startPosB = rhs.position()
    val aLen = lhs.getShort()
    val bLen = rhs.getShort()
    if (aLen != bLen) {
      lhs.position(startPosA)
      rhs.position(startPosB)
      false
    } else {
      var i = 0
      var equals = true
      while (equals && i < aLen) {
        if (lhs.get() != rhs.get())
          equals = false
        i += 1
      }
      lhs.position(startPosA)
      rhs.position(startPosB)
      equals
    }
  }
}

object DiffRenderContext {

  private[impl] val idents = IntStringMap.ofSize(100)

  def apply[MiscType](
    onMisc: MiscCallback[MiscType] = (_: Id, _: MiscType) => (),
    bufferSize: Int = 1024 * 64
  ): DiffRenderContext[MiscType] = {
    new DiffRenderContext[MiscType](onMisc, bufferSize)
  }

  trait ChangesPerformer {
    def removeAttr(id: Id, name: String): Unit
    def remove(id: Id): Unit
    def setAttr(id: Id, name: String, value: String): Unit
    def createText(id: Id, text: String): Unit
    def create(id: Id, tag: String): Unit
  }

  object DummyChangesPerformer extends ChangesPerformer {
    def removeAttr(id: Id, name: String): Unit = ()
    def remove(id: Id): Unit = ()
    def setAttr(id: Id, name: String, value: String): Unit = ()
    def createText(id: Id, text: String): Unit = ()
    def create(id: Id, tag: String): Unit = ()
  }

  type MiscCallback[MiscType] = (Id, MiscType) => _
}
