package levsha.impl

import levsha.{FastId, XmlNs}
import levsha.impl.DiffRenderContext.ChangesPerformer
import levsha.impl.internal.StringHelper.appendFromSource

import java.nio.{ByteBuffer, CharBuffer}
import java.nio.charset.StandardCharsets
import scala.collection.mutable

final class TextReportChangesPerformer extends ChangesPerformer {

  final val sb = new mutable.StringBuilder()

  override def removeAttr(id: FastId,
                          xmlNs: String,
                          name: String): Unit = {
    sb.appendAll("remove attribute ")
    sb.appendAll(xmlNs)
    sb.append(':')
    sb.appendAll(name)
    sb.appendAll(" of node ")
    id.mkString(sb)
    sb.append('\n')
  }
  override def removeStyle(id: FastId, name: String): Unit = {
    sb.appendAll("remove style ")
    sb.appendAll(name)
    sb.appendAll(" of node ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def setAttr(id: FastId,
                       xmlNs: String,
                       name: String,
                       value: String): Unit = {
    sb.appendAll("set attribute ")
    sb.appendAll(xmlNs)
    sb.append(':')
    sb.appendAll(name)
    sb.appendAll(" to '")
    sb.appendAll(value)
    sb.appendAll("' for node ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def setStyle(id: FastId, name: String, value: String): Unit = {
    sb.appendAll("set style ")
    sb.appendAll(name)
    sb.appendAll(" to '")
    sb.appendAll(value)
    sb.appendAll("' for node ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def createText(id: FastId, text: String): Unit = {
    sb.appendAll("create text node ")
    sb.appendAll("'")
    sb.appendAll(text)
    sb.appendAll("' at ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def create(id: FastId, xmlNs: String, tag: String): Unit = {
    sb.appendAll("create text node ")
    sb.appendAll(xmlNs)
    sb.append(':')
    sb.appendAll(tag)
    sb.appendAll(" at ")
    id.mkString(sb)
    sb.append('\n')
  }

  // Fast API

  override def remove(id: FastId): Unit = {
    sb.appendAll("remove node at ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def removeStyle(id: FastId,
                           source: ByteBuffer,
                           nameOffset: Int,
                           nameLength: Int): Unit = {
    sb.appendAll("remove style ")
    appendFromSource(source, sb, nameOffset, nameLength)
    sb.appendAll(" of node ")
    id.mkString(sb)
    sb.append('\n')

  }

  override def removeAttr(id: FastId,
                          xmlNs: XmlNs,
                          source: ByteBuffer,
                          nameOffset: Int,
                          nameLength: Int): Unit = {
    sb.appendAll("remove attribute ")
    sb.appendAll(xmlNs.uri)
    sb.append(':')
    appendFromSource(source, sb, nameOffset, nameLength)
    sb.appendAll(" of node ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def setAttr(id: FastId,
                       xmlNs: XmlNs,
                       source: ByteBuffer,
                       nameOffset: Int,
                       nameLength: Int,
                       valueOffset: Int,
                       valueLength: Int): Unit = {
    sb.appendAll("set attribute ")
    sb.appendAll(xmlNs.uri)
    sb.append(':')
    appendFromSource(source, sb, nameOffset, nameLength)
    sb.appendAll(" to '")
    appendFromSource(source, sb, valueOffset, valueLength)
    sb.appendAll("' for node ")
    id.mkString(sb)
    sb.append('\n')

  }

  override def setStyle(id: FastId,
                        source: ByteBuffer,
                        nameOffset: Int,
                        nameLength: Int,
                        valueOffset: Int,
                        valueLength: Int): Unit = {
    sb.appendAll("set style ")
    appendFromSource(source, sb, nameOffset, nameLength)
    sb.appendAll(" to '")
    appendFromSource(source, sb, valueOffset, valueLength)
    sb.appendAll("' for node ")
    id.mkString(sb)
    sb.append('\n')

  }

  override def createText(id: FastId,
                          source: ByteBuffer,
                          offset: Int,
                          length: Int): Unit = {
    sb.appendAll("create text node ")
    sb.appendAll("'")
    appendFromSource(source, sb, offset, length)
    sb.appendAll("' at ")
    id.mkString(sb)
    sb.append('\n')
  }

  override def create(id: FastId, xmlNs: XmlNs, source: ByteBuffer, nameOffset: Int, nameLength: Int): Unit = {
    sb.appendAll("create text node ")
    sb.append(xmlNs.uri)
    sb.append(':')
    appendFromSource(source, sb, nameOffset, nameLength)
    sb.appendAll(" at ")
    id.mkString(sb)
    sb.append('\n')
  }
}
