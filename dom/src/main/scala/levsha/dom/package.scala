package levsha

import levsha.impl.DiffRenderContext
import org.scalajs.dom.Element

import scala.collection.mutable

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
package object dom {

  private[dom] val contexts = mutable.Map.empty[Element,
    (DomChangesPerformer, DiffRenderContext[Nothing])]

  def render(target: Element)(f: RenderContext[Nothing] => RenderUnit): Unit = {
    val (performer, context) = contexts.getOrElseUpdate(target,
      new DomChangesPerformer(target) -> DiffRenderContext[Nothing]())
    context.swap()
    f(context)
    context.diff(performer)
  }
}
