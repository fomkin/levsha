package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
final class Id(array: Array[Short]) {

  def length: Int = array.length

  def parent: Id = take(length - 1)

  def take(num: Int): Id = {
    val na = new Array[Short](num)
    Array.copy(array, 0, na, 0, num)
    new Id(na)
  }

  override def toString: String = mkString

  def mkString: String = mkString('_')

  def mkString(sep: Char): String = {
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
}

object Id {
  def apply(xs: Short*): Id =
    new Id(xs.toArray)
}
