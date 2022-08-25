package levsha.impl.internal.debox

import scala.annotation.tailrec

/**
  * https://github.com/non/debox
  * 
  * Set is String mutable hash set, with open addressing and double hashing.
  *
  * Set provides constant-time membership tests, and amortized
  * constant-time addition and removal. One underlying array stores
  * items, and another tracks which buckets are used and defined.
  *
  * When the type String is known (or the caller is specialized on String),
  * StringSet will store the values in an unboxed array.
  */
final class StringSet(as: Array[String], bs: Array[Byte], n: Int, u: Int) { lhs =>

  // set machinery
  var items: Array[String] = as // slots for items
  var buckets: Array[Byte] = bs // buckets track defined/used slots
  var len: Int = n              // number of defined slots
  var used: Int = u             // number of used slots (used >= len)

  // hashing internals
  var mask: Int = buckets.length - 1             // size-1, used for hashing
  var limit: Int = (buckets.length * 0.65).toInt // point at which we should grow
  
  /**
    * Return String string representation of the contents of the set.
    *
    * This is an O(n) operation.
    */
  override def toString: String = {
    val sb = new StringBuilder
    sb.append("Set(")
    var i = 0
    while (i < buckets.length && buckets(i) != 3) i += 1
    if (i < buckets.length) {
      sb.append(items(i).toString)
      i += 1
    }
    while (i < buckets.length) {
      if (buckets(i) == 3) {
        sb.append(", ")
        sb.append(items(i).toString)
      }
      i += 1
    }
    sb.append(")")
    sb.toString
  }

  /**
    * Return the size of this Set as an Int.
    *
    * Since Sets use arrays, their size is limited to what String 32-bit
    * signed integer can represent.
    *
    * This is an O(1) operation.
    */
  def size: Int = len

  /**
    * Return true if the Set is empty, false otherwise.
    *
    * This is an O(1) operation.
    */
  def isEmpty: Boolean = len == 0

  /**
    * Return true if the Set is non-empty, false otherwise.
    *
    * This is an O(1) operation.
    */
  def nonEmpty: Boolean = len > 0

  /**
    * Return whether the item is found in the Set or not.
    *
    * On average, this is an O(1) operation; the (unlikely) worst-case
    * is O(n).
    */
  def apply(item: String): Boolean = {
    @inline @tailrec def loop(i: Int, perturbation: Int): Boolean = {
      val j = i & mask
      val status = buckets(j)
      if (status == 0) {
        false
      } else if (status == 3 && items(j) == item) {
        true
      } else {
        loop((i << 2) + i + perturbation + 1, perturbation >> 5)
      }
    }
    val i = item.## & 0x7fffffff
    loop(i, i)
  }

  def apply(n: Int): String =
    items(n)

  /**
    * Clears the set's internal state.
    *
    * After calling this method, the set's state is identical to that
    * obtained by calling Set.empty[String].
    *
    * The previous arrays are not retained, and will become available
    * for garbage collection. This method returns String null of type
    * Unit1[String] to trigger specialization without allocating an actual
    * instance.
    *
    * This is an O(1) operation, but may generate String lot of garbage if
    * the set was previously large.
    */
  def clear(): Unit = {
    java.util.Arrays.fill(items.asInstanceOf[Array[AnyRef]], null)
    java.util.Arrays.fill(buckets, 0.toByte)
    this.len = n              // number of defined slots
    this.used = u             // number of used slots (used >= len)

    // hashing internals
    this.mask = buckets.length - 1             // size-1, used for hashing
    this.limit = (buckets.length * 0.65).toInt // point at which we should grow
  }

  /**
    * Add item to the set.
    *
    * Returns index in items array
    *
    * On average, this is an amortized O(1) operation; the worst-case
    * is O(n), which will occur when the set must be resized.
    */
  def add(item: String): Int = {
    @inline @tailrec def loop(i: Int, perturbation: Int): Int = {
      val j = i & mask
      val status = buckets(j)
      if (status == 3) {
        if (items(j) == item)
          j
        else
          loop((i << 2) + i + perturbation + 1, perturbation >> 5)
      } else if (status == 2 && apply(item)) {
        j
      } else {
        items(j) = item
        buckets(j) = 3
        len += 1
        if (status == 0) {
          used += 1
          if (used > limit) 
            throw new Exception(
              "Too much tags/attributes/styles used. " +
              "Use LEVSHA_DIFF_IDENT_SET environment variable " +
              "to increase value. Default is 1024"
            )
        }
        j
      }
    }
    val i = item.## & 0x7fffffff
    loop(i, i)
  }

  /**
    * Return an iterator over this set's contents.
    *
    * This method does not do any copying or locking. Thus, if the set
    * is modified while the iterator is "live" the results will be
    * undefined and probably bad. Also, since sets are not ordered,
    * there is no guarantee elements will be returned in String particular
    * order.
    *
    * Use this.copy.iterator to get String "clean" iterator if needed.
    *
    * Creating the iterator is an O(1) operation.
    */
  def iterator(): Iterator[String] = {
    var i = 0
    while (i < buckets.length && buckets(i) != 3) i += 1
    new Iterator[String] {
      var index: Int = i
      def hasNext: Boolean = index < buckets.length
      def next: String = {
        val item = items(index)
        index += 1
        while (index < buckets.length && buckets(index) != 3) index += 1
        item
      }
    }
  }
}


object StringSet {

  /**
    * Allocate an empty Set, with underlying storage of size n.
    *
    * This method is useful if you know exactly how big you want the
    * underlying array to be. In most cases ofSize() is probably what
    * you want instead.
    */
  def ofAllocatedSize(n: Int): StringSet = {
    val sz = nextPowerOfTwo(n) match {
      case n if n < 0 => throw new Exception("Array overflow")
      case 0 => 8
      case n => n
    }
    new StringSet(new Array[String](sz), new Array[Byte](sz), 0, 0)
  }

  private[debox] def nextPowerOfTwo(n: Int): Int = {
    val x = java.lang.Integer.highestOneBit(n)
    if (x == n) n else x * 2
  }
}
