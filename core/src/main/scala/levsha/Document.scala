package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
sealed trait Document[+MiscType] {
  def apply(rc: RenderContext[MiscType]): Unit
}

object Document {

  sealed trait Node[+MiscType] extends Document[MiscType]
  sealed trait Attr[+MiscType] extends Document[MiscType]

  object Node {
    def apply[T](f: RenderContext[T] => Unit): Node[T] = new Node[T] {
      def apply(rc: RenderContext[T]): Unit = f(rc)
    }
  }

  object Attr {
    def apply[T](f: RenderContext[T] => Unit): Attr[T] = new Attr[T] {
      def apply(rc: RenderContext[T]): Unit = f(rc)
    }
  }

  case object Empty extends Node[Nothing] with Attr[Nothing] {
    def apply(rc: RenderContext[Nothing]): Unit = ()
  }
}
