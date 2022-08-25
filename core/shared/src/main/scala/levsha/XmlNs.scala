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

package levsha
import scala.annotation.switch

sealed abstract class XmlNs(val shortcut: String, val uri: String, val code: Byte)

object XmlNs {

  case object HTML extends XmlNs("html", "http://www.w3.org/1999/xhtml", 0)
  case object SVG extends XmlNs("svg", "http://www.w3.org/2000/svg", 1)
  case object MathML extends XmlNs("mathml", "http://www.w3.org/1998/Math/MathML", 2)
  case object XLink extends XmlNs("xlink", "http://www.w3.org/1999/xlink", 3)

  final val html = HTML
  final val svg = SVG
  final val mathml = MathML
  final val xlink = XLink

  def fromCode(code: Int): XmlNs = (code: @switch) match {
    case 0 => HTML
    case 1 => SVG
    case 2 => MathML
    case 3 => XLink
  }
}
