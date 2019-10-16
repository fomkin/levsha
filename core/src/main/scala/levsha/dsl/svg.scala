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

package levsha.dsl

import levsha.XmlNs
import XmlNs.{svg => svgNs, html => htmlNs}

/**
  * https://developer.mozilla.org/en-US/docs/Web/SVG
  */
object svg {

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/filter
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/filter
    */
  val filter: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = svgNs

    def name: String = "filter"
  }

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/mask
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/mask
    */
  val mask: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = svgNs

    def name: String = "mask"
  }

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/path
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/path
    */
  val path: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = svgNs

    def name: String = "path"
  }

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/style
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/style
    */
  val style: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = svgNs

    def name: String = "style"
  }


  // Elements
  // https://developer.mozilla.org/en-US/docs/Web/SVG/Element

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/svg
    */
  val Svg: TagDef = TagDef(svgNs, "svg")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/a
    */
  val a: TagDef = TagDef(svgNs, "a")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/clipPath
    */
  val clipPath: TagDef = TagDef(svgNs, "clipPath")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/clip-profile
    */
  val clipProfile: TagDef = TagDef(svgNs, "clip-profile")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/animate
    */
  val animate: TagDef = TagDef(svgNs, "animate")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/animateMotion
    */
  val animateMotion: TagDef = TagDef(svgNs, "animateMotion")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/animateTransform
    */
  val animateTransform: TagDef = TagDef(svgNs, "animateTransform")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/circle
    */
  val circle: TagDef = TagDef(svgNs, "circle")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/defs
    */
  val defs: TagDef = TagDef(svgNs, "defs")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/desc
    */
  val desc: TagDef = TagDef(svgNs, "desc")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/discard
    */
  val discard: TagDef = TagDef(svgNs, "discard")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/ellipse
    */
  val ellipse: TagDef = TagDef(svgNs, "ellipse")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feBlend
    */
  val feBlend: TagDef = TagDef(svgNs, "feBlend")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feColorMatrix
    */
  val feColorMatrix: TagDef = TagDef(svgNs, "feColorMatrix")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feComponentTransfer
    */
  val feComponentTransfer: TagDef = TagDef(svgNs, "feComponentTransfer")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feComposite
    */
  val feComposite: TagDef = TagDef(svgNs, "feComposite")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feConvolveMatrix
    */
  val feConvolveMatrix: TagDef = TagDef(svgNs, "feConvolveMatrix")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feDiffuseLighting
    */
  val feDiffuseLighting: TagDef = TagDef(svgNs, "feDiffuseLighting")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feDisplacementMap
    */
  val feDisplacementMap: TagDef = TagDef(svgNs, "feDisplacementMap")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feDistantLight
    */
  val feDistantLight: TagDef = TagDef(svgNs, "feDistantLight")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feDropShadow
    */
  val feDropShadow: TagDef = TagDef(svgNs, "feDropShadow")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feFlood
    */
  val feFlood: TagDef = TagDef(svgNs, "feFlood")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feFuncA
    */
  val feFuncA: TagDef = TagDef(svgNs, "feFuncA")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feFuncB
    */
  val feFuncB: TagDef = TagDef(svgNs, "feFuncB")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feFuncG
    */
  val feFuncG: TagDef = TagDef(svgNs, "feFuncG")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feFuncR
    */
  val feFuncR: TagDef = TagDef(svgNs, "feFuncR")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feGaussianBlur
    */
  val feGaussianBlur: TagDef = TagDef(svgNs, "feGaussianBlur")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feImage
    */
  val feImage: TagDef = TagDef(svgNs, "feImage")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feMerge
    */
  val feMerge: TagDef = TagDef(svgNs, "feMerge")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feMergeNode
    */
  val feMergeNode: TagDef = TagDef(svgNs, "feMergeNode")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feMorphology
    */
  val feMorphology: TagDef = TagDef(svgNs, "feMorphology")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feOffset
    */
  val feOffset: TagDef = TagDef(svgNs, "feOffset")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/fePointLight
    */
  val fePointLight: TagDef = TagDef(svgNs, "fePointLight")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feSpecularLighting
    */
  val feSpecularLighting: TagDef = TagDef(svgNs, "feSpecularLighting")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feSpotLight
    */
  val feSpotLight: TagDef = TagDef(svgNs, "feSpotLight")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feTile
    */
  val feTile: TagDef = TagDef(svgNs, "feTile")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/feTurbulence
    */
  val feTurbulence: TagDef = TagDef(svgNs, "feTurbulence")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/foreignObject
    */
  val foreignObject: TagDef = TagDef(svgNs, "foreignObject")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/g
    */
  val g: TagDef = TagDef(svgNs, "g")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/hatch
    */
  val hatch: TagDef = TagDef(svgNs, "hatch")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/hatchpath
    */
  val hatchpath: TagDef = TagDef(svgNs, "hatchpath")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/image
    */
  val image: TagDef = TagDef(svgNs, "image")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/line
    */
  val line: TagDef = TagDef(svgNs, "line")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/linearGradient
    */
  val linearGradient: TagDef = TagDef(svgNs, "linearGradient")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/marker
    */
  val marker: TagDef = TagDef(svgNs, "marker")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/mesh
    */
  val mesh: TagDef = TagDef(svgNs, "mesh")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/meshgradient
    */
  val meshgradient: TagDef = TagDef(svgNs, "meshgradient")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/meshpatch
    */
  val meshpatch: TagDef = TagDef(svgNs, "meshpatch")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/meshrow
    */
  val meshrow: TagDef = TagDef(svgNs, "meshrow")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/metadata
    */
  val metadata: TagDef = TagDef(svgNs, "metadata")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/mpath
    */
  val mpath: TagDef = TagDef(svgNs, "mpath")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/pattern
    */
  val pattern: TagDef = TagDef(svgNs, "pattern")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/polygon
    */
  val polygon: TagDef = TagDef(svgNs, "polygon")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/polyline
    */
  val polyline: TagDef = TagDef(svgNs, "polyline")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/radialGradient
    */
  val radialGradient: TagDef = TagDef(svgNs, "radialGradient")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/rect
    */
  val rect: TagDef = TagDef(svgNs, "rect")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/script
    */
  val script: TagDef = TagDef(svgNs, "script")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/set
    */
  val set: TagDef = TagDef(svgNs, "set")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/solidcolor
    */
  val solidcolor: TagDef = TagDef(svgNs, "solidcolor")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/stop
    */
  val stop: TagDef = TagDef(svgNs, "stop")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/switch
    */
  val switch: TagDef = TagDef(svgNs, "switch")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/symbol
    */
  val symbol: TagDef = TagDef(svgNs, "symbol")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/text
    */
  val text: TagDef = TagDef(svgNs, "text")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/textPath
    */
  val textPath: TagDef = TagDef(svgNs, "textPath")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/title
    */
  val title: TagDef = TagDef(svgNs, "title")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/tspan
    */
  val tspan: TagDef = TagDef(svgNs, "tspan")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/unknown
    */
  val unknown: TagDef = TagDef(svgNs, "unknown")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/use
    */
  val use: TagDef = TagDef(svgNs, "use")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Element/view
    */
  val view: TagDef = TagDef(svgNs, "view")

  // Attributes
  // https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute
  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/accent-height
    */
  val accentHeight: AttrDef = AttrDef(htmlNs, "accent-height")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/accumulate
    */
  val accumulate: AttrDef = AttrDef(htmlNs, "accumulate")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/additive
    */
  val additive: AttrDef = AttrDef(htmlNs, "additive")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/alignment-baseline
    */
  val alignmentBaseline: AttrDef = AttrDef(htmlNs, "alignment-baseline")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/allowReorder
    */
  val allowReorder: AttrDef = AttrDef(htmlNs, "allowReorder")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/alphabetic
    */
  val alphabetic: AttrDef = AttrDef(htmlNs, "alphabetic")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/amplitude
    */
  val amplitude: AttrDef = AttrDef(htmlNs, "amplitude")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/arabic-form
    */
  val arabicForm: AttrDef = AttrDef(htmlNs, "arabic-form")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/ascent
    */
  val ascent: AttrDef = AttrDef(htmlNs, "ascent")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/attributeName
    */
  val attributeName: AttrDef = AttrDef(htmlNs, "attributeName")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/attributeType
    */
  val attributeType: AttrDef = AttrDef(htmlNs, "attributeType")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/autoReverse
    */
  val autoReverse: AttrDef = AttrDef(htmlNs, "autoReverse")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/azimuth
    */
  val azimuth: AttrDef = AttrDef(htmlNs, "azimuth")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/baseFrequency
    */
  val baseFrequency: AttrDef = AttrDef(htmlNs, "baseFrequency")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/baseline-shift
    */
  val baselineShift: AttrDef = AttrDef(htmlNs, "baseline-shift")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/baseProfile
    */
  val baseProfile: AttrDef = AttrDef(htmlNs, "baseProfile")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/bbox
    */
  val bbox: AttrDef = AttrDef(htmlNs, "bbox")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/begin
    */
  val begin: AttrDef = AttrDef(htmlNs, "begin")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/bias
    */
  val bias: AttrDef = AttrDef(htmlNs, "bias")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/by
    */
  val by: AttrDef = AttrDef(htmlNs, "by")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/calcMode
    */
  val calcMode: AttrDef = AttrDef(htmlNs, "calcMode")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/cap-height
    */
  val capHeight: AttrDef = AttrDef(htmlNs, "cap-height")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/class
    */
  val `class`: AttrDef = AttrDef(htmlNs, "class")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/clip
    */
  val clip: AttrDef = AttrDef(htmlNs, "clip")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/clipPathUnits
    */
  val clipPathUnits: AttrDef = AttrDef(htmlNs, "clipPathUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/clip-rule
    */
  val clipRule: AttrDef = AttrDef(htmlNs, "clip-rule")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/color
    */
  val color: AttrDef = AttrDef(htmlNs, "color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/color-interpolation
    */
  val colorInterpolation: AttrDef = AttrDef(htmlNs, "color-interpolation")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/color-interpolation-filters
    */
  val colorInterpolationFilters: AttrDef = AttrDef(htmlNs, "color-interpolation-filters")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/color-rendering
    */
  val colorRendering: AttrDef = AttrDef(htmlNs, "color-rendering")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/contentScriptType
    */
  val contentScriptType: AttrDef = AttrDef(htmlNs, "contentScriptType")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/contentStyleType
    */
  val contentStyleType: AttrDef = AttrDef(htmlNs, "contentStyleType")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/cursor
    */
  val cursor: AttrDef = AttrDef(htmlNs, "cursor")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/cx
    */
  val cx: AttrDef = AttrDef(htmlNs, "cx")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/cy
    */
  val cy: AttrDef = AttrDef(htmlNs, "cy")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/d
    */
  val d: AttrDef = AttrDef(htmlNs, "d")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/decelerate
    */
  val decelerate: AttrDef = AttrDef(htmlNs, "decelerate")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/descent
    */
  val descent: AttrDef = AttrDef(htmlNs, "descent")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/diffuseConstant
    */
  val diffuseConstant: AttrDef = AttrDef(htmlNs, "diffuseConstant")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/direction
    */
  val direction: AttrDef = AttrDef(htmlNs, "direction")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/display
    */
  val display: AttrDef = AttrDef(htmlNs, "display")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/divisor
    */
  val divisor: AttrDef = AttrDef(htmlNs, "divisor")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/dominant-baseline
    */
  val dominantBaseline: AttrDef = AttrDef(htmlNs, "dominant-baseline")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/dur
    */
  val dur: AttrDef = AttrDef(htmlNs, "dur")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/dx
    */
  val dx: AttrDef = AttrDef(htmlNs, "dx")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/dy
    */
  val dy: AttrDef = AttrDef(htmlNs, "dy")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/edgeMode
    */
  val edgeMode: AttrDef = AttrDef(htmlNs, "edgeMode")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/elevation
    */
  val elevation: AttrDef = AttrDef(htmlNs, "elevation")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/enable-background
    */
  val enableBackground: AttrDef = AttrDef(htmlNs, "enable-background")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/end
    */
  val end: AttrDef = AttrDef(htmlNs, "end")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/exponent
    */
  val exponent: AttrDef = AttrDef(htmlNs, "exponent")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/externalResourcesRequired
    */
  val externalResourcesRequired: AttrDef = AttrDef(htmlNs, "externalResourcesRequired")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fill
    */
  val fill: AttrDef = AttrDef(htmlNs, "fill")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fill-opacity
    */
  val fillOpacity: AttrDef = AttrDef(htmlNs, "fill-opacity")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fill-rule
    */
  val fillRule: AttrDef = AttrDef(htmlNs, "fill-rule")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/filterRes
    */
  val filterRes: AttrDef = AttrDef(htmlNs, "filterRes")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/filterUnits
    */
  val filterUnits: AttrDef = AttrDef(htmlNs, "filterUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/flood-color
    */
  val floodColor: AttrDef = AttrDef(htmlNs, "flood-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/flood-opacity
    */
  val floodOpacity: AttrDef = AttrDef(htmlNs, "flood-opacity")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-family
    */
  val fontFamily: AttrDef = AttrDef(htmlNs, "font-family")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-size
    */
  val fontSize: AttrDef = AttrDef(htmlNs, "font-size")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-size-adjust
    */
  val fontSizeAdjust: AttrDef = AttrDef(htmlNs, "font-size-adjust")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-stretch
    */
  val fontStretch: AttrDef = AttrDef(htmlNs, "font-stretch")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-style
    */
  val fontStyle: AttrDef = AttrDef(htmlNs, "font-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-variant
    */
  val fontVariant: AttrDef = AttrDef(htmlNs, "font-variant")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/font-weight
    */
  val fontWeight: AttrDef = AttrDef(htmlNs, "font-weight")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/format
    */
  val format: AttrDef = AttrDef(htmlNs, "format")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/from
    */
  val from: AttrDef = AttrDef(htmlNs, "from")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fr
    */
  val fr: AttrDef = AttrDef(htmlNs, "fr")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fx
    */
  val fx: AttrDef = AttrDef(htmlNs, "fx")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fy
    */
  val fy: AttrDef = AttrDef(htmlNs, "fy")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/g1
    */
  val g1: AttrDef = AttrDef(htmlNs, "g1")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/g2
    */
  val g2: AttrDef = AttrDef(htmlNs, "g2")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/glyph-name
    */
  val glyphName: AttrDef = AttrDef(htmlNs, "glyph-name")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/glyph-orientation-horizontal
    */
  val glyphOrientationHorizontal: AttrDef = AttrDef(htmlNs, "glyph-orientation-horizontal")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/glyph-orientation-vertical
    */
  val glyphOrientationVertical: AttrDef = AttrDef(htmlNs, "glyph-orientation-vertical")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/glyphRef
    */
  val glyphRef: AttrDef = AttrDef(htmlNs, "glyphRef")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/gradientTransform
    */
  val gradientTransform: AttrDef = AttrDef(htmlNs, "gradientTransform")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/gradientUnits
    */
  val gradientUnits: AttrDef = AttrDef(htmlNs, "gradientUnits")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/hanging
    */
  val hanging: AttrDef = AttrDef(htmlNs, "hanging")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/height
    */
  val height: AttrDef = AttrDef(htmlNs, "height")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/href
    */
  val href: AttrDef = AttrDef(htmlNs, "href")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/hreflang
    */
  val hreflang: AttrDef = AttrDef(htmlNs, "hreflang")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/horiz-adv-x
    */
  val horizAdvX: AttrDef = AttrDef(htmlNs, "horiz-adv-x")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/horiz-origin-x
    */
  val horizOriginX: AttrDef = AttrDef(htmlNs, "horiz-origin-x")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/id
    */
  val id: AttrDef = AttrDef(htmlNs, "id")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/ideographic
    */
  val ideographic: AttrDef = AttrDef(htmlNs, "ideographic")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/image-rendering
    */
  val imageRendering: AttrDef = AttrDef(htmlNs, "image-rendering")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/in
    */
  val in: AttrDef = AttrDef(htmlNs, "in")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/in2
    */
  val in2: AttrDef = AttrDef(htmlNs, "in2")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/intercept
    */
  val intercept: AttrDef = AttrDef(htmlNs, "intercept")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/k
    */
  val k: AttrDef = AttrDef(htmlNs, "k")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/k1
    */
  val k1: AttrDef = AttrDef(htmlNs, "k1")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/k2
    */
  val k2: AttrDef = AttrDef(htmlNs, "k2")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/k3
    */
  val k3: AttrDef = AttrDef(htmlNs, "k3")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/k4
    */
  val k4: AttrDef = AttrDef(htmlNs, "k4")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/kernelMatrix
    */
  val kernelMatrix: AttrDef = AttrDef(htmlNs, "kernelMatrix")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/kernelUnitLength
    */
  val kernelUnitLength: AttrDef = AttrDef(htmlNs, "kernelUnitLength")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/kerning
    */
  val kerning: AttrDef = AttrDef(htmlNs, "kerning")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/keyPoints
    */
  val keyPoints: AttrDef = AttrDef(htmlNs, "keyPoints")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/keySplines
    */
  val keySplines: AttrDef = AttrDef(htmlNs, "keySplines")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/keyTimes
    */
  val keyTimes: AttrDef = AttrDef(htmlNs, "keyTimes")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/lang
    */
  val lang: AttrDef = AttrDef(htmlNs, "lang")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/lengthAdjust
    */
  val lengthAdjust: AttrDef = AttrDef(htmlNs, "lengthAdjust")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/letter-spacing
    */
  val letterSpacing: AttrDef = AttrDef(htmlNs, "letter-spacing")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/lighting-color
    */
  val lightingColor: AttrDef = AttrDef(htmlNs, "lighting-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/limitingConeAngle
    */
  val limitingConeAngle: AttrDef = AttrDef(htmlNs, "limitingConeAngle")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/local
    */
  val local: AttrDef = AttrDef(htmlNs, "local")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/marker-end
    */
  val markerEnd: AttrDef = AttrDef(htmlNs, "marker-end")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/marker-mid
    */
  val markerMid: AttrDef = AttrDef(htmlNs, "marker-mid")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/marker-start
    */
  val markerStart: AttrDef = AttrDef(htmlNs, "marker-start")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/markerHeight
    */
  val markerHeight: AttrDef = AttrDef(htmlNs, "markerHeight")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/markerUnits
    */
  val markerUnits: AttrDef = AttrDef(htmlNs, "markerUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/markerWidth
    */
  val markerWidth: AttrDef = AttrDef(htmlNs, "markerWidth")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/maskContentUnits
    */
  val maskContentUnits: AttrDef = AttrDef(htmlNs, "maskContentUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/maskUnits
    */
  val maskUnits: AttrDef = AttrDef(htmlNs, "maskUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/mathematical
    */
  val mathematical: AttrDef = AttrDef(htmlNs, "mathematical")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/max
    */
  val max: AttrDef = AttrDef(htmlNs, "max")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/media
    */
  val media: AttrDef = AttrDef(htmlNs, "media")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/method
    */
  val method: AttrDef = AttrDef(htmlNs, "method")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/min
    */
  val min: AttrDef = AttrDef(htmlNs, "min")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/mode
    */
  val mode: AttrDef = AttrDef(htmlNs, "mode")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/name
    */
  val name: AttrDef = AttrDef(htmlNs, "name")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/numOctaves
    */
  val numOctaves: AttrDef = AttrDef(htmlNs, "numOctaves")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/offset
    */
  val offset: AttrDef = AttrDef(htmlNs, "offset")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/opacity
    */
  val opacity: AttrDef = AttrDef(htmlNs, "opacity")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/operator
    */
  val operator: AttrDef = AttrDef(htmlNs, "operator")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/order
    */
  val order: AttrDef = AttrDef(htmlNs, "order")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/orient
    */
  val orient: AttrDef = AttrDef(htmlNs, "orient")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/orientation
    */
  val orientation: AttrDef = AttrDef(htmlNs, "orientation")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/origin
    */
  val origin: AttrDef = AttrDef(htmlNs, "origin")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/overflow
    */
  val overflow: AttrDef = AttrDef(htmlNs, "overflow")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/overline-position
    */
  val overlinePosition: AttrDef = AttrDef(htmlNs, "overline-position")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/overline-thickness
    */
  val overlineThickness: AttrDef = AttrDef(htmlNs, "overline-thickness")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/panose-1
    */
  val panose1: AttrDef = AttrDef(htmlNs, "panose-1")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/paint-order
    */
  val paintOrder: AttrDef = AttrDef(htmlNs, "paint-order")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/pathLength
    */
  val pathLength: AttrDef = AttrDef(htmlNs, "pathLength")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/patternContentUnits
    */
  val patternContentUnits: AttrDef = AttrDef(htmlNs, "patternContentUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/patternTransform
    */
  val patternTransform: AttrDef = AttrDef(htmlNs, "patternTransform")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/patternUnits
    */
  val patternUnits: AttrDef = AttrDef(htmlNs, "patternUnits")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/ping
    */
  val ping: AttrDef = AttrDef(htmlNs, "ping")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/pointer-events
    */
  val pointerEvents: AttrDef = AttrDef(htmlNs, "pointer-events")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/points
    */
  val points: AttrDef = AttrDef(htmlNs, "points")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/pointsAtX
    */
  val pointsAtX: AttrDef = AttrDef(htmlNs, "pointsAtX")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/pointsAtY
    */
  val pointsAtY: AttrDef = AttrDef(htmlNs, "pointsAtY")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/pointsAtZ
    */
  val pointsAtZ: AttrDef = AttrDef(htmlNs, "pointsAtZ")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/preserveAlpha
    */
  val preserveAlpha: AttrDef = AttrDef(htmlNs, "preserveAlpha")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/preserveAspectRatio
    */
  val preserveAspectRatio: AttrDef = AttrDef(htmlNs, "preserveAspectRatio")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/primitiveUnits
    */
  val primitiveUnits: AttrDef = AttrDef(htmlNs, "primitiveUnits")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/r
    */
  val r: AttrDef = AttrDef(htmlNs, "r")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/radius
    */
  val radius: AttrDef = AttrDef(htmlNs, "radius")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/referrerPolicy
    */
  val referrerPolicy: AttrDef = AttrDef(htmlNs, "referrerPolicy")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/refX
    */
  val refX: AttrDef = AttrDef(htmlNs, "refX")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/refY
    */
  val refY: AttrDef = AttrDef(htmlNs, "refY")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/rel
    */
  val rel: AttrDef = AttrDef(htmlNs, "rel")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/rendering-intent
    */
  val renderingIntent: AttrDef = AttrDef(htmlNs, "rendering-intent")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/repeatCount
    */
  val repeatCount: AttrDef = AttrDef(htmlNs, "repeatCount")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/repeatDur
    */
  val repeatDur: AttrDef = AttrDef(htmlNs, "repeatDur")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/requiredExtensions
    */
  val requiredExtensions: AttrDef = AttrDef(htmlNs, "requiredExtensions")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/requiredFeatures
    */
  val requiredFeatures: AttrDef = AttrDef(htmlNs, "requiredFeatures")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/restart
    */
  val restart: AttrDef = AttrDef(htmlNs, "restart")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/result
    */
  val result: AttrDef = AttrDef(htmlNs, "result")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/rotate
    */
  val rotate: AttrDef = AttrDef(htmlNs, "rotate")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/rx
    */
  val rx: AttrDef = AttrDef(htmlNs, "rx")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/ry
    */
  val ry: AttrDef = AttrDef(htmlNs, "ry")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/scale
    */
  val scale: AttrDef = AttrDef(htmlNs, "scale")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/seed
    */
  val seed: AttrDef = AttrDef(htmlNs, "seed")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/shape-rendering
    */
  val shapeRendering: AttrDef = AttrDef(htmlNs, "shape-rendering")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/slope
    */
  val slope: AttrDef = AttrDef(htmlNs, "slope")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/spacing
    */
  val spacing: AttrDef = AttrDef(htmlNs, "spacing")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/specularConstant
    */
  val specularConstant: AttrDef = AttrDef(htmlNs, "specularConstant")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/specularExponent
    */
  val specularExponent: AttrDef = AttrDef(htmlNs, "specularExponent")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/speed
    */
  val speed: AttrDef = AttrDef(htmlNs, "speed")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/spreadMethod
    */
  val spreadMethod: AttrDef = AttrDef(htmlNs, "spreadMethod")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/startOffset
    */
  val startOffset: AttrDef = AttrDef(htmlNs, "startOffset")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stdDeviation
    */
  val stdDeviation: AttrDef = AttrDef(htmlNs, "stdDeviation")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stemh
    */
  val stemh: AttrDef = AttrDef(htmlNs, "stemh")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stemv
    */
  val stemv: AttrDef = AttrDef(htmlNs, "stemv")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stitchTiles
    */
  val stitchTiles: AttrDef = AttrDef(htmlNs, "stitchTiles")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stop-color
    */
  val stopColor: AttrDef = AttrDef(htmlNs, "stop-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stop-opacity
    */
  val stopOpacity: AttrDef = AttrDef(htmlNs, "stop-opacity")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/strikethrough-position
    */
  val strikethroughPosition: AttrDef = AttrDef(htmlNs, "strikethrough-position")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/strikethrough-thickness
    */
  val strikethroughThickness: AttrDef = AttrDef(htmlNs, "strikethrough-thickness")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/string
    */
  val string: AttrDef = AttrDef(htmlNs, "string")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke
    */
  val stroke: AttrDef = AttrDef(htmlNs, "stroke")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-dasharray
    */
  val strokeDasharray: AttrDef = AttrDef(htmlNs, "stroke-dasharray")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-dashoffset
    */
  val strokeDashoffset: AttrDef = AttrDef(htmlNs, "stroke-dashoffset")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-linecap
    */
  val strokeLinecap: AttrDef = AttrDef(htmlNs, "stroke-linecap")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-linejoin
    */
  val strokeLinejoin: AttrDef = AttrDef(htmlNs, "stroke-linejoin")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-miterlimit
    */
  val strokeMiterlimit: AttrDef = AttrDef(htmlNs, "stroke-miterlimit")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-opacity
    */
  val strokeOpacity: AttrDef = AttrDef(htmlNs, "stroke-opacity")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-width
    */
  val strokeWidth: AttrDef = AttrDef(htmlNs, "stroke-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/surfaceScale
    */
  val surfaceScale: AttrDef = AttrDef(htmlNs, "surfaceScale")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/systemLanguage
    */
  val systemLanguage: AttrDef = AttrDef(htmlNs, "systemLanguage")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/tabindex
    */
  val tabindex: AttrDef = AttrDef(htmlNs, "tabindex")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/tableValues
    */
  val tableValues: AttrDef = AttrDef(htmlNs, "tableValues")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/target
    */
  val target: AttrDef = AttrDef(htmlNs, "target")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/targetX
    */
  val targetX: AttrDef = AttrDef(htmlNs, "targetX")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/targetY
    */
  val targetY: AttrDef = AttrDef(htmlNs, "targetY")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/text-anchor
    */
  val textAnchor: AttrDef = AttrDef(htmlNs, "text-anchor")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/text-decoration
    */
  val textDecoration: AttrDef = AttrDef(htmlNs, "text-decoration")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/text-rendering
    */
  val textRendering: AttrDef = AttrDef(htmlNs, "text-rendering")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/textLength
    */
  val textLength: AttrDef = AttrDef(htmlNs, "textLength")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/to
    */
  val to: AttrDef = AttrDef(htmlNs, "to")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/transform
    */
  val transform: AttrDef = AttrDef(htmlNs, "transform")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/type
    */
  val `type`: AttrDef = AttrDef(htmlNs, "type")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/u1
    */
  val u1: AttrDef = AttrDef(htmlNs, "u1")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/u2
    */
  val u2: AttrDef = AttrDef(htmlNs, "u2")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/underline-position
    */
  val underlinePosition: AttrDef = AttrDef(htmlNs, "underline-position")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/underline-thickness
    */
  val underlineThickness: AttrDef = AttrDef(htmlNs, "underline-thickness")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/unicode
    */
  val unicode: AttrDef = AttrDef(htmlNs, "unicode")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/unicode-bidi
    */
  val unicodeBidi: AttrDef = AttrDef(htmlNs, "unicode-bidi")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/unicode-range
    */
  val unicodeRange: AttrDef = AttrDef(htmlNs, "unicode-range")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/units-per-em
    */
  val unitsPerEm: AttrDef = AttrDef(htmlNs, "units-per-em")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/v-alphabetic
    */
  val vAlphabetic: AttrDef = AttrDef(htmlNs, "v-alphabetic")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/v-hanging
    */
  val vHanging: AttrDef = AttrDef(htmlNs, "v-hanging")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/v-ideographic
    */
  val vIdeographic: AttrDef = AttrDef(htmlNs, "v-ideographic")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/v-mathematical
    */
  val vMathematical: AttrDef = AttrDef(htmlNs, "v-mathematical")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/values
    */
  val values: AttrDef = AttrDef(htmlNs, "values")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/vector-effect
    */
  val vectorEffect: AttrDef = AttrDef(htmlNs, "vector-effect")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/version
    */
  val version: AttrDef = AttrDef(htmlNs, "version")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/vert-adv-y
    */
  val vertAdvY: AttrDef = AttrDef(htmlNs, "vert-adv-y")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/vert-origin-x
    */
  val vertOriginX: AttrDef = AttrDef(htmlNs, "vert-origin-x")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/vert-origin-y
    */
  val vertOriginY: AttrDef = AttrDef(htmlNs, "vert-origin-y")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/viewBox
    */
  val viewBox: AttrDef = AttrDef(htmlNs, "viewBox")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/viewTarget
    */
  val viewTarget: AttrDef = AttrDef(htmlNs, "viewTarget")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/visibility
    */
  val visibility: AttrDef = AttrDef(htmlNs, "visibility")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/width
    */
  val width: AttrDef = AttrDef(htmlNs, "width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/widths
    */
  val widths: AttrDef = AttrDef(htmlNs, "widths")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/word-spacing
    */
  val wordSpacing: AttrDef = AttrDef(htmlNs, "word-spacing")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/writing-mode
    */
  val writingMode: AttrDef = AttrDef(htmlNs, "writing-mode")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/x
    */
  val x: AttrDef = AttrDef(htmlNs, "x")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/x-height
    */
  val xHeight: AttrDef = AttrDef(htmlNs, "x-height")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/x1
    */
  val x1: AttrDef = AttrDef(htmlNs, "x1")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/x2
    */
  val x2: AttrDef = AttrDef(htmlNs, "x2")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/xChannelSelector
    */
  val xChannelSelector: AttrDef = AttrDef(htmlNs, "xChannelSelector")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/y
    */
  val y: AttrDef = AttrDef(htmlNs, "y")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/y1
    */
  val y1: AttrDef = AttrDef(htmlNs, "y1")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/y2
    */
  val y2: AttrDef = AttrDef(htmlNs, "y2")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/yChannelSelector
    */
  val yChannelSelector: AttrDef = AttrDef(htmlNs, "yChannelSelector")


  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/z
    */
  val z: AttrDef = AttrDef(htmlNs, "z")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/zoomAndPan
    */
  val zoomAndPan: AttrDef = AttrDef(htmlNs, "zoomAndPan")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/clip-path
    */
  val `clip-path`: AttrDef = AttrDef(htmlNs, "clip-path")

}
