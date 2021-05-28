/*
 * Copyright 2017-2020 Aleksey Fomkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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