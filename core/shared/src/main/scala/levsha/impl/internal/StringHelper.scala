package levsha.impl.internal

import java.nio.ByteBuffer
import scala.collection.mutable
import java.lang.reflect.Field
import java.nio.charset.StandardCharsets

trait StringHelper {
  def stringFromSource(source: ByteBuffer, offset: Int, length: Int): String
  def appendFromSource(source: ByteBuffer, target: mutable.StringBuilder, offset: Int, length: Int): Unit
  def putToBuffer(target: ByteBuffer, source: String, big: Boolean): Unit
}

object StringHelper extends StringHelperInstancePlatformSpecific

// Currently all platforms works with DefaultStringHelper
// Maybe will be changed in future
trait StringHelperInstancePlatformSpecific extends DefaultStringHelper

trait DefaultStringHelper extends StringHelper {

  def stringFromSource(source: ByteBuffer, offset: Int, length: Int): String = {
    val sb = new scala.collection.mutable.StringBuilder()
    appendFromSource(source, sb, offset, length)
    sb.result()
  }

  def appendFromSource(source: ByteBuffer, target: mutable.StringBuilder, offset: Int, length: Int): Unit = {
    if (length > 0) {
      var i = 0
      while (i < length) {
        val c = source.getChar(offset + i)
        target.append(c)
        i += 2
      }
    }
  }

  def putToBuffer(target: ByteBuffer, source: String, big: Boolean): Unit = {
    val length = source.length
    val byteLength = length * 2
    if (big) target.putInt(byteLength)
    else target.put(byteLength.toByte)
    if (length > 0) {
      var i = 0
      while (i < length) {
        target.putChar(source(i))
        i += 1
      }
    }
  }
}
