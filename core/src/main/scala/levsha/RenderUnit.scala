package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
sealed trait RenderUnit[-RC] {
  def apply(rc: RC): Unit
}

object RenderUnit {

  sealed trait Node[-RC] extends RenderUnit[RC]
  sealed trait Attr[-RC] extends RenderUnit[RC]

  object Node {
    def apply[RC](f: RC => Unit): Node[RC] = new Node[RC] {
      def apply(rc: RC): Unit = f(rc)
    }
  }

  object Attr {
    def apply[RC](f: RC => Unit): Attr[RC] = new Attr[RC] {
      def apply(rc: RC): Unit = f(rc)
    }
  }

  case object Empty extends Node[RenderContext[Nothing]] with Attr[RenderContext[Nothing]] {
    def apply(rc: RenderContext[Nothing]): Unit = ()
  }
}
