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

package levsha.impl

import levsha.{RenderContext, XmlNs}
import Op._

/**
  * Generates HTML
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
abstract class AbstractTextRenderContext[MiscType] extends RenderContext[MiscType] {

  def prettyPrinting: TextPrettyPrintingConfig

  protected var lastOp: Int = OpClose
  protected var indentation: Int = 0

  val builder: StringBuilder = new StringBuilder()

  protected def addIndentation(): Unit =
    if (prettyPrinting.enableAutoIndent) {
      var i = 0
      while (i < indentation) {
        var j = 0
        while (j < prettyPrinting.indentationSize) {
          builder.append(prettyPrinting.indentationChar)
          j += 1
        }
        i += 1
      }
    }

  def openNode(xmlns: XmlNs, name: String): Unit = {
    beforeOpenNode()
    addIndentation()
    builder.append('<')
    builder.append(name)
    lastOp = OpOpen
    indentation += 1
  }

  private def beforeOpenNode() = {
    if (lastOp != OpClose && lastOp != OpText) {
      if (lastOp == OpStyle) {
        builder.append('"')
      }
      builder.append('>')
      builder.append(prettyPrinting.lineBreak)
    }
  }

  def closeNode(name: String): Unit = {
    indentation -= 1
    if (lastOp == OpAttr || lastOp == OpOpen) {
      builder.append('>')
    } else if (lastOp == OpStyle) {
      builder.append("\">")
    } else {
      addIndentation()
    }
    builder.append('<')
    builder.append('/')
    builder.append(name)
    builder.append('>')
    builder.append(prettyPrinting.lineBreak)
    lastOp = OpClose
  }

  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit = {
    if (lastOp == OpStyle) {
      builder.append('"')
    }
    builder.append(' ')
    builder.append(name)
    builder.append('=')
    builder.append('"')
    builder.append(value)
    builder.append('"')
    lastOp = OpAttr
  }

  def setStyle(name: String, value: String): Unit = {
    if (lastOp != OpStyle) {
      builder.append(" style=\"")
    }
    builder.append(name)
    builder.append(":")
    builder.append(value)
    builder.append(";")
    lastOp = OpStyle
  }

  def addTextNode(text: String): Unit = {
    beforeOpenNode()
    addIndentation()
    builder.append(text)
    builder.append(prettyPrinting.lineBreak)
    lastOp = OpText
  }

  def addMisc(misc: MiscType): Unit = {}

  /** Creates string from buffer */
  def mkString: String = builder.mkString
}
