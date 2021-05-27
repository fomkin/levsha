package levsha.dsl

import levsha.Document.Node
import scala.quoted.*

trait Optimize:

  /**
   * Optimize template to monolith (if possible) [[levsha.Document.Node]] in compile time.
   * Note this method touched by non-idempotent typechecking bug of Scala compiler.
   * It means, sometimes your code could be broken. Try not to insert non-DSL code
   * into `optimize {}` call.
   *
   * @see https://github.com/fomkin/levsha#memory-allocation-model-explanation
   */
  inline def optimize[T](inline node: Node[T]): Node[T] =
    ${ DslOptimizerMacro.optimize[T]('node) }