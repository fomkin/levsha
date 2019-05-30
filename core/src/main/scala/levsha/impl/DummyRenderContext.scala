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

/**
  * This render context does nothing
  */
class DummyRenderContext[-M] extends RenderContext[M] {
  def openNode(xmlns: XmlNs, name: String): Unit = {}
  def closeNode(name: String): Unit = {}
  def setAttr(xmlNs: XmlNs, name: String, value: String): Unit = {}
  def addTextNode(text: String): Unit = {}
  def addMisc(misc: M): Unit = {}
}
