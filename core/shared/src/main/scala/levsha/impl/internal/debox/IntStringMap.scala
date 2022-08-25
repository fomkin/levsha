package levsha.impl.internal.debox

import java.util

import scala.annotation.tailrec
import scala.{specialized => sp}
import IntStringMap.Unit2

/**
  * Cropped copy of debox.Map.
  * https://raw.githubusercontent.com/non/debox/master/src/main/scala/debox/Map.scala
  */
final class IntStringMap protected[debox] (ks: Array[Int], vs: Array[String], bs: Array[Byte], n: Int, u: Int) { lhs =>

  // map internals
  var keys: Array[Int] = ks       // slots for keys
  var vals: Array[String] = vs       // slots for values
  var buckets: Array[Byte] = bs // buckets track defined/used slots
  var len: Int = n              // number of defined slots
  var used: Int = u             // number of used slots (used >= len)

  // hashing internals
  var mask: Int = keys.length - 1             // size - 1, used for hashing
  var limit: Int = (keys.length * 0.65).toInt // point at which we should grow

  /**
    * This method stores associates value with key.
    *
    * If a previous value was associated with the key, it is
    * overwritten.
    *
    * This method is usually invoked as map(key) = value, but can also
    * be invoked as map.update(key, value).
    *
    * On average, this is an amortized O(1) operation; the worst-case
    * is O(n), which will happen when the map needs to be resized.
    */
  final def update(key: Int, value: String): Unit = this.synchronized {
    @inline @tailrec
    def loop(i: Int, perturbation: Int, freeBlock: Int): Unit = {
      val j = i & mask
      val status = buckets(j)
      if (status == 0) {
        val writeTo = if (freeBlock == -1) j else freeBlock
        keys(writeTo) = key
        vals(writeTo) = value
        buckets(writeTo) = 3
        len += 1
        used += 1
        if (used > limit) {
          throw new Exception(
            "Too much attributes/styles used. " +
            "Use LEVSHA_DIFF_ATTRS_MAP environment variable " +
            "to increase value. Default is 64"
          )
        }
      } else if (status == 2) {
        loop((i << 2) + i + perturbation + 1, perturbation >> 5, j)
      } else if (keys(j) == key) {
        vals(j) = value
      } else {
        loop((i << 2) + i + perturbation + 1, perturbation >> 5, freeBlock)
      }
    }
    val i = key.## & 0x7fffffff
    loop(i, i, -1)
  }

  def clear(): Unit = {
    util.Arrays.fill(keys, 0)
    util.Arrays.fill(vals.asInstanceOf[Array[AnyRef]], null)
    util.Arrays.fill(buckets, 0.toByte)

    len = n
    used = u
    mask = keys.length - 1
    limit = (keys.length * 0.65).toInt
  }

  /**
    * Return the key's current value in the map, throwing an exception
    * if the key is not found.
    *
    * On average, this is an O(1) operation; the (unlikely) worst-case
    * is O(n).
    */
  final def apply(key: Int): String = {
    @inline @tailrec def loop(i: Int, perturbation: Int): String = {
      val j = i & mask
      val status = buckets(j)
      if (status == 0) null
      else if (status == 3 && keys(j) == key) vals(j)
      else loop((i << 2) + i + perturbation + 1, perturbation >> 5)
    }
    val i = key.## & 0x7fffffff
    loop(i, i)
  }
}

object IntStringMap {

  class Unit2[@sp A, @sp B]

  /**
    * Create a Map that can hold n unique keys without resizing itself.
    *
    * Note that the internal representation will allocate more space
    * than requested to satisfy the requirements of internal
    * alignment. Map uses arrays whose lengths are powers of two, and
    * needs at least 33% of the map free to enable good hashing
    * performance.
    *
    * Example: Map.ofSize[Int, String](100).
    */
  def ofSize(n: Int): IntStringMap =
    ofAllocatedSize(n / 2 * 3)

  /**
    * Allocate an empty Map, with underlying storage of size n.
    *
    * This method is useful if you know exactly how big you want the
    * underlying array to be. In most cases ofSize() is probably what
    * you want instead.
    */
  private[debox] def ofAllocatedSize(n: Int) = {
    val sz = nextPowerOfTwo(n) match {
      case n if n < 0 => throw new Exception(s"DeboxOverflowError $n")//throw DeboxOverflowError(n)
      case 0 => 8
      case n => n
    }
    new IntStringMap(new Array(sz), new Array(sz), new Array[Byte](sz), 0, 0)
  }

  private[debox] def nextPowerOfTwo(n: Int): Int = {
    val x = java.lang.Integer.highestOneBit(n)
    if (x == n) n else x * 2
  }
}
