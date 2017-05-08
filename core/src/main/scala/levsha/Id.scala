package levsha

import java.util

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
final class Id(private val array: Array[Short]) { lhs =>

  def level: Int = array.length

  def parent: Option[Id] = if (level == 1) None else Some(take(level - 1))

  def take(num: Int): Id = {
    val na = new Array[Short](num)
    Array.copy(array, 0, na, 0, num)
    new Id(na)
  }

  def mkString: String = mkString('_')

  def mkString(sep: Char): String = if (array.length == 0) {
    ""
  } else {
    val sb = StringBuilder.newBuilder
    var i = 0
    var continue = true
    while (continue) {
      sb.append(array(i))
      i += 1
      if (i >= array.length) continue = false
      else sb.append(sep)
    }
    sb.mkString
  }

  def toList: List[Short] = array.toList

  override def toString: String = mkString

  override def equals(obj: Any): Boolean = obj match {
    case rhs: Id => util.Arrays.equals(lhs.array, rhs.array)
    case _ => false
  }

  override def hashCode(): Int = util.Arrays.hashCode(array)
}

object Id {
  def apply(xs: Short*): Id =
    new Id(xs.toArray)

  def apply(s: String, sep: Char = '_'): Id =
    new Id(s.split(sep).map(_.toShort))
}
