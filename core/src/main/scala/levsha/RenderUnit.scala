package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
sealed trait RenderUnit

object RenderUnit {
  sealed trait NodeLike extends RenderUnit

  case object Node extends NodeLike
  case object Text extends NodeLike
  case object Misc extends NodeLike
  case object Empty extends NodeLike
  case object Attr extends RenderUnit

  type Node = Node.type
  type Text = Text.type
  type Misc = Misc.type
  type Empty = Empty.type
  type Attr = Attr.type
}
