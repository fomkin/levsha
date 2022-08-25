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
import internal.debox.{IntByteBufferMap, IntStringMap, StringSet}
import levsha.impl.internal.StringHelper

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
    buffer = ByteBuffer.allocate(savedBuffer.capacity() - 16)
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
    requestResize(OpOpenSize + name.length * 4)
    lhs.put(OpOpen.toByte)
    hashStack.open(lhs)
//    lhs.putInt(0) // sum
//    lhs.putInt(0) // end
//    lhs.put(xmlns.code) // code of xmlns
//    lhs.putInt(name.hashCode) // hash of name
//    lhs.put((name.length * 2).toByte) // length in chars of name
//    putToBuffer(lhs, name)
    val memCacheId = xmlns.hashCode ^ name.hashCode
    val memCacheLocal = getLocalHeadersCache
    var memCache = memCacheLocal(memCacheId)
    if (memCache == null) {
      val x = ByteBuffer.allocate(32).order(ByteOrder.nativeOrder())
      x.putInt(0) // sum
      x.putInt(0) // end
      x.put(xmlns.code) // code of xmlns
      x.putInt(name.hashCode) // hash of name
      StringHelper.putToBuffer(x, name, false)
      x.flip()
      memCacheLocal.update(memCacheId, x)
      memCache = x
    }
    lhs.put(memCache)
    memCache.rewind()
  }

  def closeNode(name: String): Unit = {
    closeAttrs()
    idb.decLevel()
    requestResize(OpSize)
    lhs.put(OpClose.toByte)
    hashStack.close(lhs)
  }

  private def setAttrOrStyle(xmlNs: Byte, name: String, value: String, isStyle: Byte): Unit = {
    requestResize(OpAttrSize + (name.length * 2 + value.length * 2) * 2)
    val memCacheId = xmlNs.hashCode ^ name.hashCode
    val memCacheLocal = getLocalHeadersCache
    var memCache = memCacheLocal(memCacheId)
    if (memCache == null) {
      val x = ByteBuffer.allocate(64).order(ByteOrder.nativeOrder())
      x.put(OpAttr.toByte)
      x.put(xmlNs)
      x.putInt(MurmurHash3.stringHash(name))
      StringHelper.putToBuffer(x, name, false)
      x.put(isStyle) // this is not a style
      x.flip()
      memCacheLocal.update(memCacheId, x)
      memCache = x
    }
    lhs.put(memCache)
    memCache.rewind()
    lhs.putInt(MurmurHash3.stringHash(value))
    StringHelper.putToBuffer(lhs, value, true)
//    lhs.put(OpAttr.toByte)
//    lhs.put(xmlNs.code)
//    lhs.putInt(name.hashCode)
//    lhs.put((name.length * 2).toByte) // length in chars of name
//    putToBuffer(lhs, name)
//    lhs.put(0.toByte) // this is not a style
//    lhs.putInt(bytes.length)
//    lhs.put(bytes)
  }

  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit =
    setAttrOrStyle(xmlNs.code, name, value, 0.toByte)

  def setStyle(name: String, value: String): Unit =
    setAttrOrStyle(0.toByte, name, value, 1.toByte)

  def addTextNode(text: String): Unit = {
    closeAttrs()
    idb.incId()
    requestResize(OpTextSize + text.length * 4)
    val p = lhs.position()
    lhs.put(OpText.toByte)
    lhs.putInt(MurmurHash3.stringHash(text)) // string hash
    StringHelper.putToBuffer(lhs, text, true)
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

  def diff(performer: FastChangesPerformer): Unit = {

    while (lhs.hasRemaining()) {
      val opA = getOp(lhs)
      val opB = getOp(rhs)
      if (opA == OpOpen && opB == OpOpen) {
        idb.incId()
        val sumA = readSum(lhs)
        val endA = readEnd(lhs)
        val sumB = readSum(rhs)
        val endB = readEnd(rhs)
        if (sumA != sumB) {
          val aXmlNs = lhs.get
          val aTagHash = lhs.getInt
          val aTagLength = lhs.get
          val aTagOffset = lhs.position()
          lhs.position(aTagOffset + aTagLength)
          val bXmlNs = rhs.get
          val bTagHash = rhs.getInt
          val bTagLength = rhs.get
          val bTagOffset = rhs.position()
          rhs.position(bTagOffset + bTagLength)
          if (aTagHash != bTagHash || aXmlNs != bXmlNs) {
            performer.create(idb, XmlNs.fromCode(aXmlNs), lhs, aTagOffset, aTagLength)
            rhs.position(endB)
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
          lhs.getInt // TODO this is may be removed via adding skipTextBody
          val textLength = lhs.getInt()
          val textOffset = lhs.position()
          lhs.position(textOffset + textLength)
          performer.createText(idb, lhs, textOffset, textLength)
        } else {
          skipText(lhs)
          skipText(rhs)
        }
      } else if (opA == OpText && opB == OpOpen) {
        lhs.getInt
        val aTextLength = lhs.getInt()
        val aTextOffset = lhs.position()
        lhs.position(aTextOffset + aTextLength)
        idb.incId()
        performer.createText(idb, lhs, aTextOffset, aTextLength)
        readSum(rhs)
        rhs.position(readEnd(rhs))
      } else if (opA == OpOpen && opB == OpText) {
        val sumA = readSum(lhs)
        val endA = readEnd(lhs)
        val aXmlNs = lhs.get
        val aTagHash = lhs.getInt
        val aTagLength = lhs.get
        val aTagOffset = lhs.position()
        lhs.position(aTagOffset + aTagLength)
        idb.incId()
        skipText(rhs)
        performer.create(idb, XmlNs.fromCode(aXmlNs), lhs, aTagOffset, aTagLength)
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

  private def getOp(x: ByteBuffer): Byte = {
    if (x.hasRemaining) x.get()
    else OpEnd.toByte
  }

  private def readSum(x: ByteBuffer): Int =
    x.getInt()

  private def readEnd(x: ByteBuffer): Int =
    x.getInt()

  private def skipText(x: ByteBuffer): Unit = {
    x.getInt // hash
    skipString(x)
  }

  private def skipString(x: ByteBuffer): Unit = {
    val len = x.getInt()
    x.position(x.position() + len)
  }

  private def skipAttrText(x: ByteBuffer): Unit = {
    x.getInt() // hash
    skipString(x)
  }

  /** true is further is attr; false if end of list */
  private def checkAttr(x: ByteBuffer): Boolean = {
    getOp(x) == OpAttr
  }

  private def readIsStyle(x: ByteBuffer) =
    x.get() == 1

  private def unOp(x: ByteBuffer) = {
    x.position(x.position() - 1)
  }

  private def createLoop(x: ByteBuffer, performer: FastChangesPerformer): Unit = {
    val startLevel = idb.getLevel
    var continue = true
    while (continue) {
      val op = getOp(x)
      (op: @switch) match {
        case OpClose | OpEnd =>
          if (idb.getLevel == startLevel) continue = false
          else idb.decLevel()
        case OpAttr =>
          idb.decLevelTmp()
          val attrXmlNs = x.get
          val attrNameHash = x.getInt
          val attrNameLength = x.get
          val attrNameOffset = x.position()
          x.position(attrNameOffset + attrNameLength)
          val isStyle = readIsStyle(x)
          val attrValueHash = x.getInt()
          val attrValueLength = x.getInt()
          val attrValueOffset = x.position()
          x.position(attrValueOffset + attrValueLength)
          if (isStyle) performer.setStyle(idb, x, attrNameOffset, attrNameLength, attrValueOffset, attrValueLength)
          else performer.setAttr(idb, XmlNs.fromCode(attrXmlNs), x, attrNameOffset, attrNameLength, attrValueOffset, attrValueLength)
          idb.incLevel()
        case OpText =>
          idb.incId()
          x.getInt() // hash
          val textLength = x.getInt()
          val textOffset = x.position()
          x.position(textOffset + textLength)
          performer.createText(idb, x, textOffset, textLength)
        case OpOpen =>
          idb.incId()
          readSum(x)
          readEnd(x)
          val aXmlNs = x.get
          val aTagHash = x.getInt
          val aTagLength = x.get
          val aTagOffset = x.position()
          x.position(aTagOffset + aTagLength)
          performer.create(idb, XmlNs.fromCode(aXmlNs), x, aTagOffset, aTagLength)
          idb.incLevel()
        case OpLastAttr => // do nothing
      }
    }
  }

  private def deleteLoop(x: ByteBuffer, performer: FastChangesPerformer): Unit = {
    var continue = true
    while (continue) {
      (getOp(x): @switch) match {
        case OpOpen =>
          readSum(x)
          x.position(readEnd(x))
          idb.incId()
          performer.remove(idb)
        case OpText =>
          skipText(x)
          idb.incId()
          performer.remove(idb)
        case OpClose | OpEnd => continue = false
      }
    }
  }

//  private val attrsDiffMap = IntStringMap.ofSize(diffIdentSetSize)

  private def compareAttrs(performer: FastChangesPerformer): Unit = {
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
      // Read tag name from rhs
      val bXmlNs = rhs.get()
      val bNameHash = rhs.getInt()
      val bNameLength = rhs.get()
      val bNameOffset = rhs.position()
      rhs.position(bNameOffset + bNameLength)
      val isStyleB = readIsStyle(rhs)
      var needToRemove = true
      skipAttrText(rhs)
      lhs.position(startPosA)
      while (needToRemove && checkAttr(lhs)) {
        val aXmlNs = lhs.get()
        val aNameHash = lhs.getInt()
        val aNameLength = lhs.get()
        val aNameOffset = lhs.position()
        lhs.position(aNameOffset + aNameLength)
        val isStyleA = readIsStyle(lhs)
        skipAttrText(lhs)
        if (aNameHash == bNameHash && aXmlNs == bXmlNs && isStyleA == isStyleB)
          needToRemove = false
      }
      if (needToRemove) {
        if (isStyleB) performer.removeStyle(idb, rhs, bNameOffset, bNameLength)
        else performer.removeAttr(idb, XmlNs.fromCode(bXmlNs), rhs, bNameOffset, bNameLength)
      }
    }
    // Check the attrs were added
    val endPosB = rhs.position()
    lhs.position(startPosA)
    while (checkAttr(lhs)) {
      val aXmlNs = lhs.get()
      val aNameHash = lhs.getInt()
      val aNameLength = lhs.get()
      val aNameOffset = lhs.position()
      lhs.position(aNameOffset + aNameLength)
      val isStyleA = readIsStyle(lhs)
      val aValueHash = lhs.getInt()
      val aValueLength = lhs.getInt()
      val aValueOffset = lhs.position()
      var needToSet = true
      rhs.position(startPosB)
      while (needToSet && checkAttr(rhs)) {
        val bXmlNs = rhs.get()
        val bNameHash = rhs.getInt()
        val bNameLength = rhs.get()
        val bNameOffset = rhs.position()
        rhs.position(bNameOffset + bNameLength)
        val isStyleB = readIsStyle(rhs)
        val bValueHash = rhs.getInt()
        val valueLenB = rhs.getInt()
        val valuePosB = rhs.position()
        // First condition: name of attributes should be equals
        needToSet = aNameHash != bNameHash || aXmlNs != bXmlNs || isStyleA != isStyleB || aValueHash != bValueHash
        rhs.position(valuePosB + valueLenB)
      }
      if (needToSet) {
        lhs.position(aValueOffset + aValueLength)
        if (isStyleA) performer.setStyle(idb, lhs, aNameOffset, aNameLength, aValueOffset, aValueLength)
        else performer.setAttr(idb, XmlNs.fromCode(aXmlNs), lhs, aNameOffset, aNameLength, aValueOffset, aValueLength)
      } else {
        lhs.position(aValueOffset + aValueLength)
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

    buffer = ByteBuffer.allocate(newSize)
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

  private val headersCache = new ThreadLocal[IntByteBufferMap]()
  private def getLocalHeadersCache = {
    var result = headersCache.get()
    if (result == null) {
      result = IntByteBufferMap.ofSize(2048)
      headersCache.set(result)
    }
    result
  }

  final val DefaultDiffRenderContextBufferSize = 1024 * 64

  def apply[MiscType](
    onMisc: MiscCallback[MiscType] = (_: Id, _: MiscType) => (),
    initialBufferSize: Int = DefaultDiffRenderContextBufferSize,
    savedBuffer: Option[ByteBuffer] = None
  ): DiffRenderContext[MiscType] = {
    new DiffRenderContext[MiscType](onMisc, initialBufferSize, savedBuffer.orNull)
  }

  trait FastChangesPerformer {
    def remove(id: FastId): Unit
    def create(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit
    def createText(id: FastId, source: ByteBuffer, offset: Int, length: Int): Unit
    def removeAttr(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit
    def removeStyle(id: FastId, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit
    def setAttr(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int, valueOffset: Int, valueLength: Int): Unit
    def setStyle(id: FastId, source: ByteBuffer, nameOffset: Int, nameLength: Int, valueOffset: Int, valueLength: Int): Unit
  }

  trait ChangesPerformer extends FastChangesPerformer {
    def removeAttr(id: FastId, xmlNs: String, name: String): Unit
    def removeStyle(id: FastId, name: String): Unit
    def setAttr(id: FastId, xmlNs: String, name: String, value: String): Unit
    def setStyle(id: FastId, name: String, value: String): Unit
    def createText(id: FastId, text: String): Unit
    def create(id: FastId, xmlNs: String, tag: String): Unit

    // Fast impl

    private def stringFromSource(source: ByteBuffer, offset: Int, length: Int) = {
      val sb = new scala.collection.mutable.StringBuilder()
      StringHelper.appendFromSource(source, sb, offset, length)
      sb.result()
    }

    def create(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit =
      create(id, xmlNs.uri, stringFromSource(source, nameOffset, nameLength))
    def removeAttr(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit =
      removeAttr(id, xmlNs.uri, stringFromSource(source, nameOffset, nameLength))
    def removeStyle(id: FastId, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit =
      removeStyle(id, stringFromSource(source, nameOffset, nameLength))
    def setAttr(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int, valueOffset: Int, valueLength: Int): Unit = {
      val name = stringFromSource(source, nameOffset, nameLength)
      val value = stringFromSource(source, valueOffset, valueLength)
      setAttr(id, xmlNs.uri, name, value)
    }
    def setStyle(id: FastId, source: ByteBuffer, nameOffset: Int, nameLength: Int, valueOffset: Int, valueLength: Int): Unit =
      setStyle(id, stringFromSource(source, nameOffset, nameLength), stringFromSource(source, valueOffset, valueLength))
    def createText(id: FastId, source: ByteBuffer, offset: Int, length: Int): Unit =
      createText(id, stringFromSource(source, offset, length))
  }

  object DummyChangesPerformer extends ChangesPerformer {
    def removeAttr(id: FastId, xmlNs: String, name: String): Unit = ()
    def removeStyle(id: FastId, name: String): Unit = ()
    def remove(id: FastId): Unit = ()
    def setAttr(id: FastId, name: String, xmlNs: String, value: String): Unit = ()
    def setStyle(id: FastId, name: String, value: String): Unit = ()
    def createText(id: FastId, text: String): Unit = ()
    def create(id: FastId, tag: String, xmlNs: String): Unit = ()
    // Fast API
    override def create(id: FastId,
                        xmlNs: XmlNs,
                        source: ByteBuffer,
                        nameOffset: Int,
                        nameLength: Int): Unit = ()
    override def removeAttr(id: FastId,
                            xmlNs: XmlNs,
                            source: ByteBuffer,
                            nameOffset: Int,
                            nameLength: Int): Unit = ()
    override def removeStyle(id: FastId,
                             source: ByteBuffer,
                             nameOffset: Int,
                             nameLength: Int): Unit = ()
    override def setAttr(id: FastId,
                         xmlNs: XmlNs,
                         source: ByteBuffer,
                         nameOffset: Int,
                         nameLength: Int,
                         valueOffset: Int,
                         valueLength: Int): Unit = ()
    override def setStyle(id: FastId,
                          source: ByteBuffer,
                          nameOffset: Int,
                          nameLength: Int,
                          valueOffset: Int,
                          valueLength: Int): Unit = ()
    override def createText(id: FastId,
                            source: ByteBuffer,
                            offset: Int,
                            length: Int): Unit = ()
  }

  type MiscCallback[MiscType] = (Id, MiscType) => _
}
