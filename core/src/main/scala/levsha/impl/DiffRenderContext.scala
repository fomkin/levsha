/*
 * Copyright 2017-2020 Aleksey Fomkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package levsha.impl

import java.nio.{ByteBuffer, ByteOrder}
import java.nio.charset.StandardCharsets

import levsha._
import levsha.impl.DiffRenderContext._
import levsha.impl.internal.Op._

import scala.annotation.switch
import internal.debox.{IntStringMap, StringSet}

import scala.util.hashing.MurmurHash3

/*
Structures

node {
  byte OPEN
  int sum
  int end
  int tag_hash_code
  int xmlns_hash_code
  attr attr_list[]
  byte LAST_ATTR
  node|text child[]
  byte CLOSE
}

text {
  byte TEXT
  int sum
  int length
  byte value[length]
}

attr {
  byte ATTR
  int attr_name_hash_code
  int xmlns_hash_code (0 for styles)
  bytes (0 - attr; 1 - style)
  int length
  byte value[length]
}

*/

final class DiffRenderContext[-M](mc: MiscCallback[M], initialBufferSize: Int, savedBuffer: ByteBuffer)
  extends StatefulRenderContext[M] {

  if ((initialBufferSize == 0) || ((initialBufferSize & (initialBufferSize - 1)) != 0))
    throw new IllegalArgumentException("initialBufferSize should be power of two")

  private val idb = IdBuilder()
  private val hashStack = new HashBuilder()
  private var attrsOpened = false

  private var buffer: ByteBuffer = _
  private var lhs: ByteBuffer = _
  private var rhs: ByteBuffer = _

  // Restore saved buffer if it exists
  if (savedBuffer != null) {
    savedBuffer.clear()
    val lhsPos = savedBuffer.getInt
    val lhsLimit = savedBuffer.getInt
    val rhsPos = savedBuffer.getInt
    val rhsLimit = savedBuffer.getInt
    buffer = ByteBuffer.allocateDirect(savedBuffer.capacity() - 16)
    buffer.put(savedBuffer)
    buffer.clear()
    // Restore lhs and rhs
    buffer.limit(buffer.capacity() / 2)
    lhs = buffer.slice()
    lhs.position(lhsPos)
    lhs.limit(lhsLimit)
    lhs.order(ByteOrder.nativeOrder())
    buffer.position(buffer.capacity() / 2)
    buffer.limit(buffer.capacity())
    rhs = buffer.slice()
    rhs.order(ByteOrder.nativeOrder())
    rhs.position(rhsPos)
    rhs.limit(rhsLimit)
  } else {
    resizeBuffer(0)
  }

  def openNode(xmlns: XmlNs, name: String): Unit = {
    closeAttrs()
    attrsOpened = true
    idb.incId()
    idb.incLevel()
    requestResize(OpOpenSize)
    lhs.put(OpOpen.toByte)
    hashStack.open(lhs)
    lhs.putInt(0) // sum
    lhs.putInt(0) // end
    lhs.putInt(idents.add(name))
    lhs.putInt(idents.add(xmlns.uri))
  }

  def closeNode(name: String): Unit = {
    closeAttrs()
    idb.decLevel()
    requestResize(OpSize)
    lhs.put(OpClose.toByte)
    hashStack.close(lhs)
  }

  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit = {
    val nameHash = idents.add(name)
    val xmlNsHash = idents.add(xmlNs.uri)
    val bytes = value.getBytes(StandardCharsets.UTF_8)
//    var hash = MurmurHash3.mapSeed
//    hash = MurmurHash3.mix(hash, nameHash)
//    hash = MurmurHash3.mix(hash, xmlNsHash)
//    hash = MurmurHash3.mix(hash, MurmurHash3.bytesHash(bytes))
//    hash = MurmurHash3.finalizeHash(hash, 3)
    requestResize(OpAttrSize + bytes.length * 2)
    lhs.put(OpAttr.toByte)
//    lhs.putInt(hash) // sum
//    lhs.putInt(lhs.position() + 17 + bytes.length) // end
    lhs.putInt(nameHash)
    lhs.putInt(xmlNsHash)
    lhs.put(0.toByte) // this is not a style
    lhs.putInt(bytes.length)
    lhs.put(bytes)
  }

  def setStyle(name: String, value: String): Unit = {
    val bytes = value.getBytes(StandardCharsets.UTF_8)
    requestResize(OpAttrSize + bytes.length * 2)
    lhs.put(OpAttr.toByte)
    lhs.putInt(idents.add(name))
    lhs.putInt(0) // no uri for style
    lhs.put(1.toByte) // this is a style
    lhs.putInt(bytes.length)
    lhs.put(bytes)
  }

  def addTextNode(text: String): Unit = {
    val bytes = text.getBytes(StandardCharsets.UTF_8)
    closeAttrs()
    idb.incId()
    requestResize(OpTextSize + bytes.length * 2)
    val p = lhs.position()
    lhs.put(OpText.toByte)
    lhs.putInt(MurmurHash3.bytesHash(bytes)) // string hash
    lhs.putInt(bytes.length)
    lhs.put(bytes)
    hashStack.hashText(lhs, p)
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
  }

  /** Cleanup current buffer */
  def reset(): Unit = {
    idb.reset()
    lhs.position(0)
    while (lhs.hasRemaining())
      lhs.put(0.toByte)
    lhs.clear()
  }

  def save(): ByteBuffer = {
    // capacity + 4 + 4 where 4 sizes of lhs and rhs
    val buff = ByteBuffer.allocate(buffer.capacity() + 16)
    val lhsPos = lhs.position()
    val rhsPos = rhs.position()
    buff.putInt(lhsPos)
    buff.putInt(lhs.limit())
    buff.putInt(rhsPos)
    buff.putInt(rhs.limit())
    buff.put(lhs)
    buff.put(rhs)
    buff.clear()
    // Restore positions
    lhs.position(lhsPos)
    rhs.position(rhsPos)
    buff
  }

  def finalizeDocument(): Unit = {
    lhs.flip()
    idb.reset()
  }

  def diff(performer: ChangesPerformer): Unit = {

    while (lhs.hasRemaining()) {
      val opA = op(lhs)
      val opB = op(rhs)
      if (opA == OpOpen && opB == OpOpen) {
        idb.incId()
        val sumA = readSum(lhs)
        val endA = readEnd(lhs)
        val sumB = readSum(rhs)
        val endB = readEnd(rhs)
        if (sumA != sumB) {
          val tagA = readTag(lhs)
          val xmlNsA = readXmlNs(lhs)
          val tagB = readTag(rhs)
          val xmlNsB = readXmlNs(rhs)
          if (tagA != tagB || xmlNsA != xmlNsB) {
            performer.create(idb, idents(xmlNsA), idents(tagA))
            skipLoop(rhs)
            idb.incLevel()
            createLoop(lhs, performer)
            idb.decLevel()
          } else {
            compareAttrs(performer)
            idb.incLevel()
          }
        } else {
          lhs.position(endA)
          rhs.position(endB)
        }
      } else if (opA == OpText && opB == OpText) {
        idb.incId()
        if (!compareTexts()) {
          skipText(rhs)
          val textA = readText(lhs)
          performer.createText(idb, textA)
        } else {
          skipText(lhs)
          skipText(rhs)
        }
      } else if (opA == OpText && opB == OpOpen) {
        val aText = readText(lhs)
        readSum(rhs) // skip tag b
        readEnd(rhs) // skip tab b
        readTag(rhs) // skip tag b
        readXmlNs(rhs) // skip tag b
        idb.incId()
        performer.createText(idb, aText)
        skipLoop(rhs)
      } else if (opA == OpOpen && opB == OpText) {
        val sumA = readSum(lhs)
        val endA = readEnd(lhs)
        val tagA = readTag(lhs)
        val xmlNsA = readXmlNs(lhs)
        idb.incId()
        skipText(rhs)
        performer.create(idb, idents(xmlNsA), idents(tagA))
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

  def currentId: Id = idb.mkId

  def currentContainerId: Id = {
    idb.decLevelTmp()
    val id = idb.mkId
    idb.incLevel()
    id
  }

  def subsequentId: Id = {
    idb.incId()
    val id = idb.mkId
    idb.decId()
    id
  }

  private def closeAttrs(): Unit = {
    if (attrsOpened) {
      hashStack.hashAttrs(lhs)
      attrsOpened = false
      requestResize(OpSize)
      lhs.put(OpLastAttr.toByte)
    }
  }

  private def op(x: ByteBuffer): Byte = {
    if (x.hasRemaining) x.get()
    else OpEnd.toByte
  }

  private def readSum(x: ByteBuffer): Int =
    x.getInt()

  private def readEnd(x: ByteBuffer): Int =
    x.getInt()

  private def readTag(x: ByteBuffer): Int = {
    x.getInt()
  }

  private def readXmlNs(x: ByteBuffer): Int = {
    x.getInt
  }

  private def skipText(x: ByteBuffer): Unit = {
    x.getInt // hash
    skipString(x)
  }

  private def skipString(x: ByteBuffer): Unit = {
    val len = x.getInt()
    x.position(x.position() + len)
  }

  private def readString(x: ByteBuffer, len: Int): String = {
    val bytes = new Array[Byte](len)
    x.get(bytes)
    new String(bytes, StandardCharsets.UTF_8)
  }

  private def readString(x: ByteBuffer): String = {
    val len = x.getInt()
    readString(x, len)
  }

  private def readText(x: ByteBuffer): String = {
    x.getInt() // hash
    readString(x)
  }

  private def skipAttr(x: ByteBuffer): Unit = {
    x.getInt() // tag name
    x.getInt() // ns
    x.get() // is style
    val len = x.getInt()
    x.position(x.position() + len)
  }

  private def skipAttrText(x: ByteBuffer): Unit = {
    skipString(x)
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

  private def readIsStyle(x: ByteBuffer) =
    if (x.get() == 1) true else false

  private def readAttr(x: ByteBuffer) = {
    idents(x.getInt())
  }

  private def readAttrXmlNs(x: ByteBuffer) = {
    x.getInt()
  }

  private def readAttrText(x: ByteBuffer, len: Int) = {
    readString(x, len)
  }

  private def readAttrText(x: ByteBuffer) = {
    readString(x)
  }

  private def unOp(x: ByteBuffer) = {
    x.position(x.position() - 1)
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
          readSum(x)
          readEnd(x)
          readTag(x)
          readXmlNs(x)
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
          val attr = readAttr(x)
          val xmlNs = readAttrXmlNs(x)
          val isStyle = readIsStyle(x)
          if (isStyle) performer.setStyle(idb, attr, readAttrText(x))
          else performer.setAttr(idb, idents(xmlNs), attr, readAttrText(x))
          idb.incLevel()
        case OpText =>
          idb.incId()
          performer.createText(idb, readText(x))
        case OpOpen =>
          idb.incId()
          readSum(x)
          readEnd(x)
          val tagA = readTag(x)
          val xmlNsA = readXmlNs(x)
          performer.create(idb, idents(xmlNsA), idents(tagA))
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
          readSum(x) // skip sum
          readEnd(x) // skip end
          readTag(x) // skip tag
          readXmlNs(x) // skip ns
          idb.incId()
          performer.remove(idb)
          skipLoop(x)
        case OpText =>
          skipText(x)
          idb.incId()
          performer.remove(idb)
        case OpClose | OpEnd => continue = false
      }
    }
  }

//  private val attrsDiffMap = IntStringMap.ofSize(diffIdentSetSize)

  private def compareAttrs(performer: ChangesPerformer): Unit = {
    // TODO maybe it make sence to chose algorithm depend attrs length and shape
    val startPosA = lhs.position()
    val startPosB = rhs.position()
//    // Let's find attrs to add
//    while (checkAttr(rhs)) {
//      val attrName = readAttrRaw(rhs)
//      readAttrXmlNs(rhs)
//      readIsStyle(rhs)
//      val value = readAttrText(rhs)
//      attrsDiffMap.update(attrName, value)
//    }
//    while (checkAttr(lhs)) {
//      val name = readAttrRaw(lhs)
//      val ns = readAttrXmlNs(lhs)
//      val isStyle = readIsStyle(lhs)
//      val value = readAttrText(lhs)
//      val oldValue = attrsDiffMap(name)
//      if (oldValue == null || oldValue != value) {
//        if (isStyle) performer.setStyle(idb, idents(name), value)
//        else performer.setAttr(idb, idents(ns), idents(name), value)
//      }
//    }
//    // Lets find attrs to remove
//    lhs.position(startPosA)
//    rhs.position(startPosB)
//    attrsDiffMap.clear()
//    while (checkAttr(lhs)) {
//      val attrName = readAttrRaw(lhs)
//      readAttrXmlNs(lhs)
//      readIsStyle(lhs)
//      val value = readAttrText(lhs)
//      attrsDiffMap.update(attrName, value)
//    }
//    while (checkAttr(rhs)) {
//      val name = readAttrRaw(rhs)
//      val ns = readAttrXmlNs(rhs)
//      val isStyle = readIsStyle(rhs)
//      val value = readAttrText(rhs)
//      val oldValue = attrsDiffMap(name)
//      if (oldValue == null) {
//        if (isStyle) performer.removeStyle(idb, idents(name))
//        else performer.removeAttr(idb, idents(ns), idents(name))
//      }
//    }
//    attrsDiffMap.clear()

    // O(n*m) implementation
    // Real word test shows that this implementation is faster.
    // -----
    // Check the attrs were removed
    while (checkAttr(rhs)) {
      val attrNameB = readAttrRaw(rhs)
      val attrXmlNsB = readAttrXmlNs(rhs)
      val isStyleB = readIsStyle(rhs)
      var needToRemove = true
      skipAttrText(rhs)
      lhs.position(startPosA)
      while (needToRemove && checkAttr(lhs)) {
        val attrNameA = readAttrRaw(lhs)
        val attrXmlNsA = readAttrXmlNs(lhs)
        val isStyleA = readIsStyle(lhs)
        skipAttrText(lhs)
        if (attrNameA == attrNameB && attrXmlNsA == attrXmlNsB && isStyleA == isStyleB)
          needToRemove = false
      }
      if (needToRemove) {
        if (isStyleB) performer.removeStyle(idb, idents(attrNameB))
        else performer.removeAttr(idb, idents(attrXmlNsB), idents(attrNameB))
      }
    }
    // Check the attrs were added
    val endPosB = rhs.position()
    lhs.position(startPosA)
    while (checkAttr(lhs)) {
      val nameA = readAttrRaw(lhs)
      val xmlNsA = readAttrXmlNs(lhs)
      val isStyleA = readIsStyle(lhs)
      val valueLenA = lhs.getInt()
      val valuePosA = lhs.position()
      var needToSet = true
      rhs.position(startPosB)
      while (needToSet && checkAttr(rhs)) {
        val nameB = readAttrRaw(rhs)
        val xmlNsB = readAttrXmlNs(rhs)
        val isStyleB = readIsStyle(rhs)
        val valueLenB = rhs.getInt()
        val valuePosB = rhs.position()
        // First condition: name of attributes should be equals
        if (nameA == nameB && xmlNsA == xmlNsB && isStyleA == isStyleB) {
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
        if (isStyleA) performer.setStyle(idb, idents(nameA), valueA)
        else performer.setAttr(idb, idents(xmlNsA), idents(nameA), valueA)
      } else {
        lhs.position(valuePosA + valueLenA)
      }
    }
    rhs.position(endPosB)
  }

  private def compareTexts(): Boolean = {
    val startPosA = lhs.position()
    val startPosB = rhs.position()
    val aHash = lhs.getInt()
    val bHash = rhs.getInt()
    lhs.position(startPosA)
    rhs.position(startPosB)
    aHash == bHash
  }

  /* Check write to lhs is available */
  private def requestResize(additionalSize: Int) = {
    if (additionalSize > lhs.remaining)
      resizeBuffer(additionalSize)
  }

  private def resizeBuffer(additionalSize: Int) = {
    val oldSize =
      if (buffer != null) buffer.capacity
      else -1

    val newSize = {
      if (oldSize > 0) {
        val totalAdditionalSize = oldSize + additionalSize
        var size = oldSize
        while (size < totalAdditionalSize) size = size * 2
        size
      }
      else {
        // Ignore additional size
        initialBufferSize
      }
    }

    buffer = ByteBuffer.allocateDirect(newSize)
    buffer.order(ByteOrder.nativeOrder())

    lhs = {
      buffer.limit(newSize / 2)
      val buff = buffer.slice().order(ByteOrder.nativeOrder())
      if (lhs != null) {
        lhs.flip()
        buff.put(lhs)
      }
      buff
    }

    rhs = {
      buffer.position(newSize / 2)
      buffer.limit(buffer.capacity)
      val buff = buffer.slice().order(ByteOrder.nativeOrder())
      if (rhs != null) {
        rhs.position(0)
        buff.put(rhs)
      }
      buff.flip()
      buff
    }
  }
}

object DiffRenderContext {

//  private[impl] val idents = IntStringMap.ofSize(100)
  private[impl] val diffIdentSetSize = sys.env.getOrElse("LEVSHA_DIFF_IDENT_SET", "1024").toInt
//  private[impl] lazy val diffAttrMapSize = sys.env.getOrElse("LEVSHA_DIFF_ATTRS_MAP", "64").toInt
  private[impl] val idents = StringSet.ofAllocatedSize(diffIdentSetSize)

  final val DefaultDiffRenderContextBufferSize = 1024 * 64

  def apply[MiscType](
    onMisc: MiscCallback[MiscType] = (_: Id, _: MiscType) => (),
    initialBufferSize: Int = DefaultDiffRenderContextBufferSize,
    savedBuffer: Option[ByteBuffer] = None
  ): DiffRenderContext[MiscType] = {
    new DiffRenderContext[MiscType](onMisc, initialBufferSize, savedBuffer.orNull)
  }

  trait ChangesPerformer {
    def removeAttr(id: FastId, xmlNs: String, name: String): Unit
    def removeStyle(id: FastId, name: String): Unit
    def remove(id: FastId): Unit
    def setAttr(id: FastId, xmlNs: String, name: String, value: String): Unit
    def setStyle(id: FastId, name: String, value: String): Unit
    def createText(id: FastId, text: String): Unit
    def create(id: FastId, xmlNs: String, tag: String): Unit
  }

  object DummyChangesPerformer extends ChangesPerformer {
    def removeAttr(id: FastId, xmlNs: String, name: String): Unit = ()
    def removeStyle(id: FastId, name: String): Unit = ()
    def remove(id: FastId): Unit = ()
    def setAttr(id: FastId, name: String, xmlNs: String, value: String): Unit = ()
    def setStyle(id: FastId, name: String, value: String): Unit = ()
    def createText(id: FastId, text: String): Unit = ()
    def create(id: FastId, tag: String, xmlNs: String): Unit = ()
  }

  type MiscCallback[MiscType] = (Id, MiscType) => _
}
