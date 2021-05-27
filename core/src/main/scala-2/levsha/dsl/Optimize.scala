package levsha.dsl

import levsha.Document.Node
import scala.language.experimental.macros

trait Optimize {

  /**
   * Optimize template to monolith (if possible) [[levsha.Document.Node]] in compile time.
   * Note this method touched by non-idempotent typechecking bug of Scala compiler.
   * It means, sometimes your code could be broken. Try not to insert non-DSL code
   * into `optimize {}` call.
   *
   * @see https://github.com/scala/bug/issues/5464
   * @see https://github.com/fomkin/levsha#memory-allocation-model-explanation
   */
  def optimize[T](node: Node[T]): Node[T] = macro DslOptimizerMacro.optimize[T]
}