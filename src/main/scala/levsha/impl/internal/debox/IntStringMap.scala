package levsha.impl.internal.debox

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
  final def update(key: Int, value: String): Unit = {
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
        if (used > limit) grow()
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

  /**
    * Aborb the given map's contents into this map.
    *
    * This method does not copy the other map's contents. Thus, this
    * should only be used when there are no saved references to the
    * other map. It is private, and exists primarily to simplify the
    * implementation of certain methods.
    *
    * This is an O(1) operation, although it can potentially generate a
    * lot of garbage (if the map was previously large).
    */
  private[this] def absorb(rhs: IntStringMap): Unit = {
    keys = rhs.keys
    vals = rhs.vals
    buckets = rhs.buckets
    len = rhs.len
    used = rhs.used
    mask = rhs.mask
    limit = rhs.limit
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
      if (status == 0) throw new Exception(key.toString)//KeyNotFoundException(key.toString)
      else if (status == 3 && keys(j) == key) vals(j)
      else loop((i << 2) + i + perturbation + 1, perturbation >> 5)
    }
    val i = key.## & 0x7fffffff
    loop(i, i)
  }

  /**
    * Grow the underlying array to best accomodate the map's size.
    *
    * To preserve hashing access speed, the map's size should never be
    * more than 66% of the underlying array's size. When this size is
    * reached, the map needs to be updated (using this method) to have a
    * larger array.
    *
    * The underlying array's size must always be a multiple of 2, which
    * means this method grows the array's size by 2x (or 4x if the map
    * is very small). This doubling helps amortize the cost of
    * resizing, since as the map gets larger growth will happen less
    * frequently. This method returns a null of type Unit1[A] to
    * trigger specialization without allocating an actual instance.
    *
    * Growing is an O(n) operation, where n is the map's size.
    */
  final def grow(): Unit2[Int, String] = {
    val next = keys.length * (if (keys.length < 10000) 4 else 2)
    val map = IntStringMap.ofAllocatedSize(next)
    var i = 0
    while (i < buckets.length) {
      if (buckets(i) == 3) map(keys(i)) = vals(i)
      i += 1
    }
    absorb(map)
    null
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
