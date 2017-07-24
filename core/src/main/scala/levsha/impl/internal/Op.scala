package levsha.impl.internal

/**
  * Op codes
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
private[levsha] object Op {
  final val OpOpen = 1
  final val OpClose = 2
  final val OpAttr = 3
  final val OpText = 4
  final val OpLastAttr = 5
  final val OpEnd = 6

  final val OpSize = 1

  final val OpOpenSize = 9
  final val OpAttrSize = 9
  final val OpTextSize = 5
}
