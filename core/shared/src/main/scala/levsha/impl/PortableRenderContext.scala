package levsha.impl

import levsha.{IdBuilder, RenderContext, XmlNs}
import levsha.impl.internal.Op._
import levsha.impl.internal.StringHelper
import levsha.impl.internal.debox.IntByteBufferMap

import java.nio.{ByteBuffer, ByteOrder}
import java.util
import scala.annotation.switch
import scala.collection.mutable
import scala.util.hashing.MurmurHash3

import PortableRenderContext.{Result, ApplyResult}

class PortableRenderContext[M](initialBufferSize: Int) extends RenderContext[M] with ApplyResult[M] {

  import PortableRenderContext.getLocalHeadersCache

  protected val idb: IdBuilder = IdBuilder()
  protected var bytecode: ByteBuffer = ByteBuffer
    .allocate(initialBufferSize)
    .order(ByteOrder.nativeOrder())

  private val hashStack = new HashBuilder()
  private var attrsOpened = false
  private val msOffsets = mutable.ArrayBuffer[Int]()
  private val ms = new util.ArrayList[M]()

  def openNode(xmlns: XmlNs, name: String): Unit = {
    closeAttrs()
    attrsOpened = true
    idb.incId()
    idb.incLevel()
    requestResize(OpOpenSize + name.length * 4)
    bytecode.put(OpOpen.toByte)
    hashStack.open(bytecode)
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
    bytecode.put(memCache)
    memCache.rewind()
  }

  def closeNode(name: String): Unit = {
    closeAttrs()
    idb.decLevel()
    requestResize(OpSize)
    bytecode.put(OpClose.toByte)
    hashStack.close(bytecode)
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
    bytecode.put(memCache)
    memCache.rewind()
    bytecode.putInt(MurmurHash3.stringHash(value))
    StringHelper.putToBuffer(bytecode, value, true)
  }

  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit =
    setAttrOrStyle(xmlNs.code, name, value, 0.toByte)

  def setStyle(name: String, value: String): Unit =
    setAttrOrStyle(0.toByte, name, value, 1.toByte)

  def addTextNode(text: String): Unit = {
    closeAttrs()
    idb.incId()
    requestResize(OpTextSize + text.length * 4)
    val p = bytecode.position()
    bytecode.put(OpText.toByte)
    bytecode.putInt(MurmurHash3.stringHash(text)) // string hash
    StringHelper.putToBuffer(bytecode, text, true)
    hashStack.hashText(bytecode, p)
  }

  def addMisc(misc: M): Unit = {
    idb.decLevelTmp()
    val offset = bytecode.position()
    msOffsets += offset
    ms.add(misc)
    idb.incLevel()
  }

  def applyResult(result: Result[M]): Unit = {
    val sizeTotal = result.bytecode.length
    requestResize(sizeTotal)
    val l = result.msOffsets.length
    var i = 0
    var prevSrcOffset = 0
    while (i < l) {
      val m = result.ms(i)
      val offset = result.msOffsets(i)
      val s = offset - prevSrcOffset
      bytecode.put(result.bytecode, prevSrcOffset, s)
      addMisc(m)
      prevSrcOffset = offset
      i += 1
    }
    val s = sizeTotal - prevSrcOffset
    bytecode.put(result.bytecode, prevSrcOffset, s)
  }

  def result(): Result[M] = {
    bytecode.flip()
    val result = new Array[Byte](bytecode.remaining())
    bytecode.get(result)
    Result(
      result,
      ms.toArray().asInstanceOf[Array[M]],
      msOffsets.toArray
    )
  }

  private def closeAttrs(): Unit = {
    if (attrsOpened) {
      hashStack.hashAttrs(bytecode)
      attrsOpened = false
      requestResize(OpSize)
      bytecode.put(OpLastAttr.toByte)
    }
  }

  protected def getNewSize(oldSize: Int, additionalSize: Int, initialSize: Int): Int = {
    if (oldSize > 0) {
      val totalAdditionalSize = oldSize + additionalSize
      var size = oldSize
      while (size < totalAdditionalSize) {
        size = size * 2
      }
      size
    }
    else {
      // Ignore additional size
      initialSize
    }
  }

  protected def requestResize(additionalSize: Int): Unit = {
    if (additionalSize > bytecode.remaining) {
      val newSize = getNewSize(bytecode.capacity, additionalSize, initialBufferSize)
      val oldBytecode = bytecode
      oldBytecode.flip()
      bytecode = ByteBuffer.allocate(newSize)
      bytecode.put(oldBytecode)
    }
  }
}

object PortableRenderContext {

  trait ApplyResult[M] {
    def applyResult(result: Result[M]): Unit
  }

  final case class Result[M](bytecode: Array[Byte], ms: Array[M], msOffsets: Array[Int]) {
    def apply(rc: RenderContext[M]): Unit = rc match {
      case prc: ApplyResult[M] =>
        prc.applyResult(this)
      case _ =>
        var nodeStack = List.empty[String]
        var msIndex = 0
        val msOffsets = this.msOffsets
        val msCount = this.ms.length
        val bytecode = ByteBuffer.wrap(this.bytecode).order(ByteOrder.nativeOrder())
        while (bytecode.hasRemaining) {
          while (msIndex < msCount && bytecode.position() == msOffsets(msIndex)) {
            rc.addMisc(this.ms(msIndex))
            msIndex += 1
          }
          (bytecode.get(): @switch) match {
            case OpOpen =>
              bytecode.getInt() // sum
              bytecode.getInt() // end
              val xmlNs = XmlNs.fromCode(bytecode.get())
              bytecode.getInt // nameSum
              val nameLength = bytecode.get()
              val nameOffset = bytecode.position()
              val name = StringHelper.stringFromSource(bytecode, nameOffset, nameLength)
              bytecode.position(nameOffset + nameLength)
              nodeStack = name :: nodeStack
              rc.openNode(xmlNs, name)
            case OpClose =>
              val name = nodeStack.head
              nodeStack = nodeStack.tail
              rc.closeNode(name)
            case OpAttr =>
              val xmlNs = XmlNs.fromCode(bytecode.get())
              bytecode.getInt // nameSum
              val nameLength = bytecode.get()
              val nameOffset = bytecode.position()
              val name = StringHelper.stringFromSource(bytecode, nameOffset, nameLength)
              bytecode.position(nameOffset + nameLength)
              val isStyle = bytecode.get()
              bytecode.getInt // valueSum
              val valueLength = bytecode.getInt()
              val valueOffset = bytecode.position()
              val value = StringHelper.stringFromSource(bytecode, valueOffset, valueLength)
              bytecode.position(valueOffset + valueLength)
              if (isStyle == 1) {
                rc.setStyle(name, value)
              } else {
                rc.setAttr(xmlNs, name, value)
              }
            case OpText =>
              bytecode.getInt // valueSum
              val valueLength = bytecode.getInt()
              val valueOffset = bytecode.position()
              val value = StringHelper.stringFromSource(bytecode, valueOffset, valueLength)
              bytecode.position(valueOffset + valueLength)
              rc.addTextNode(value)
            case OpLastAttr => ()
            case OpEnd      => ()
          }
        }
    }
  }

  private val headersCache = new ThreadLocal[IntByteBufferMap]()

  private def getLocalHeadersCache = {
    var result = headersCache.get()
    if (result == null) {
      result = IntByteBufferMap.ofSize(2048)
      headersCache.set(result)
    }
    result
  }
}
