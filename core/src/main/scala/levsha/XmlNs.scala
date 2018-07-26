/*
 * Copyright 2017-2018 Aleksey Fomkin
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

import scala.language.experimental.macros

final case class XmlNs(shortcut: String, uri: String) {
  override val hashCode: Int = uri.hashCode
}

object XmlNs {
  val xlink = XmlNs("xlink", "http://www.w3.org/1999/xlink")
  val html = XmlNs("html", "http://www.w3.org/1999/xhtml")
  val svg = XmlNs("svg", "http://www.w3.org/2000/svg")
  val mathml = XmlNs("mathml", "http://www.w3.org/1998/Math/MathML")
}
