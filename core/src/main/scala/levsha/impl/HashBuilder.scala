package levsha.impl

import java.nio.{ByteBuffer}
import scala.util.hashing.MurmurHash3

class HashBuilder(private val posStack: Stack = new Stack(),
                  private var lastOpenPos: Int = 0) {

  def open(target: ByteBuffer): Unit = {
    val pos = target.position()
    lastOpenPos = pos + 8
    posStack.push(pos)
  }

  def hashAttrs(target: ByteBuffer) = if (posStack.nonEmpty) {
    val nodeStart = posStack.peek
    val attrsEnd = target.position()
    val size = attrsEnd - lastOpenPos
    // Hash content of the last tag
    var i = lastOpenPos
    var hash = target.getInt(nodeStart)
    while (i < attrsEnd) {
      hash = MurmurHash3.mix(hash, target.get(i) & 0xFF)
      i = i + 1
    }
    target.putInt(nodeStart, hash)
  }

  def hashText(target: ByteBuffer, textStart: Int) = if (posStack.nonEmpty) {
    val nodeStart = posStack.peek
    val textEnd = target.position()
    val size = textEnd - lastOpenPos
    // Hash content of the last tag
    var i = textStart
    var hash = target.getInt(nodeStart)
    while (i < textEnd) {
      hash = MurmurHash3.mix(hash, target.get(i) & 0xFF)
      i = i + 1
    }
    target.putInt(nodeStart, hash)
  }

  def close(target: ByteBuffer) = {
    val nodeStartPos = posStack.pop()
    val nodeEndPos = target.position()
    val size = nodeEndPos - lastOpenPos
    // Hash content of the last tag
    var hash = target.getInt(nodeStartPos)
    // Save current hash to target
    hash = MurmurHash3.finalizeHash(hash, size)
    target.putInt(nodeStartPos, hash)
    target.putInt(nodeStartPos + 4, nodeEndPos)
    // Updated proper node hash
    if (posStack.nonEmpty) {
      val properHash = target.getInt(posStack.peek)
      target.putInt(posStack.peek, MurmurHash3.mix(properHash, hash))
    }
  }
}

private[impl] class Stack(val array: Array[Int] = new Array(256),
                    var i: Int = 0) {
  def push(v: Int): Unit = {
    array(i) = v
    i = i + 1
  }

  def pop(): Int = {
    i = i - 1
    array(i)
  }

  def peek: Int =
    array(i - 1)

  def nonEmpty: Boolean =
    i > 0
}
/*
ul(0
  li(a), 10
  li(b), 20
  li(c), 30
  li(d), 40
)
*/