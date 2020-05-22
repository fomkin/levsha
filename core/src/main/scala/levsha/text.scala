/*
 * Copyright 2017-2019 Aleksey Fomkin
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

package levsha

import levsha.dsl.SymbolDsl
import levsha.impl.{TextPrettyPrintingConfig, XhtmlRenderContext}

/**
  * Default template context and dsl
 *
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object text {

  val symbolDsl = new SymbolDsl[Nothing]()

  /**
    * Provides Text render context to make html string
    * @example
    * {{{
    * val html = renderHtml {
    *   'div('class /= "title", "Hello, I'm Levsha!")
    * }
    * }}}
    */
  def renderHtml(f: Document.Node[Nothing],
                 prettyPrinting: TextPrettyPrintingConfig = TextPrettyPrintingConfig.default): String = {
    val renderContext = new XhtmlRenderContext(prettyPrinting)
    f(renderContext)
    renderContext.mkString
  }
}
