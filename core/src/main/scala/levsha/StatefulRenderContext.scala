package levsha

trait StatefulRenderContext[-M] extends RenderContext[M] {
  def currentContainerId: Id
  def currentId: Id
  def subsequentId: Id
}
