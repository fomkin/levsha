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

package levsha.impl

import levsha.impl.internal.Op._

/**
  * Generates HTML5 output.
  * @author Alexander Semenov
  */
class Html5RenderContext[MiscType](prettyPrinting: TextPrettyPrintingConfig)
  extends XhtmlRenderContext[MiscType](prettyPrinting) {

  import Html5RenderContext._

  override def closeNode(name: String): Unit = {
    indentation -= 1

    val selfClosing = SelfClosingTags.contains(name)

    if (!selfClosing && (lastOp == OpAttr || lastOp == OpOpen)) {
      builder.append('>')
    } else if (lastOp == OpStyle) {
      builder.append(if (selfClosing) "\"" else "\">")
    } else {
      addIndentation()
    }

    if (selfClosing) {
      builder.append('/')
      builder.append('>')
    } else {
      builder.append('<')
      builder.append('/')
      builder.append(name)
      builder.append('>')
    }

    builder.append(prettyPrinting.lineBreak)
    lastOp = OpClose
  }

}

private[impl] object Html5RenderContext {

  private val SelfClosingTags: Set[String] =
    Set(
      "area",
      "br",
      "col",
      "embed",
      "hr",
      "img",
      "input",
      "link",
      "meta",
      "param",
      "source",
      "track",
      "wbr"
    )

}
