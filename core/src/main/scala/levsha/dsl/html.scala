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

import levsha.Document.Attr
import levsha.XmlNs
import XmlNs.{html => ns}

/**
  * HTML Tags, Attributes, and Styles specification
  */
object html {

  /**
    * When tag:
    * The HTML Title element (<title>) defines the document's title that is shown in a browser's title bar or a page's tab.
    *
    * When attribute:
    * Text to be displayed in a tooltip when hovering over the element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/title
    */
  val title: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "title"
  }

  /**
    * When tag:
    * The HTML <style> element contains style information for a document, or part of a document.
    *
    * When attribute:
    * Defines CSS styles which will override styles previously set.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/style
    */
  val style: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "style"
  }

  /**
    * When tag:
    * The HTML Citation element (<cite>) is used to describe a reference to a cited creative work, and must include the title of that work.
    *
    * When attribute:
    * Contains a URI which points to the source of the quote or change.
    * Applicable to <blockquote>, <del>, <ins>, <q>
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/cite
    */
  val cite: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "cite"
  }

  /**
    * When tag:
    * The HTML <span> element is a generic inline container for phrasing content, which does not inherently represent anything. It can be used to group elements for styling purposes (using the class or id attributes), or because they share attribute values, such as lang.
    *
    * When attribute:
    * Applicable to <col>, <colgroup>
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/span
    */
  val span: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "span"
  }

  /**
    * When tag:
    * The HTML <form> element represents a document section that contains interactive controls for submitting information to a web server.
    *
    * When attribute:
    * Indicates the form that is the owner of the element.
    * Applicable to <button>, <fieldset>, <input>, <keygen>, <label>, <meter>, <object>, <output>, <progress>, <select>, <textarea>
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/form
    */
  val form: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "form"
  }

  /**
    * When tag:
    * The HTML <label> element represents a caption for an item in a user interface.
    *
    * When attribute:
    * Specifies a user-readable title of the element.
    * Applicable to <optgroup>, <option>, <track>
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/label
    */
  val label: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "label"
  }

  /**
    * When tag:
    * The HTML Disclosure Summary element (<summary>) element specifies a summary, caption, or legend for a <details> element's disclosure box.
    *
    * When attribute:
    * Applicable to	<table>
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/summary
    */
  val summary: TagDef with AttrDef = new TagDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "summary"
  }

  /**
    * When attribute:
    * Specifies the URL of an image file.
    * Note: Although browsers and email clients may still support this attribute, it is obsolete. Use CSS background-image instead.
    * Applicable to <body>, <table>, <td>, <th>
    *
    * When style:
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background
    */
  val background: StyleDef with AttrDef = new StyleDef with AttrDef {
    def ns: XmlNs = levsha.XmlNs.html

    def name: String = "background"
  }


  // Tags
  // https://developer.mozilla.org/en-US/docs/Web/HTML/Element

  /**
    * The HTML <html> element represents the root (top-level element) of an HTML document, so it is also referred to as the root element. All other elements must be descendants of this element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/html
    */
  val Html: TagDef = TagDef(ns, "html")

  // Document metadata
  // -----------------
  // Metadata contains information about the page. This includes information about styles, scripts and data to help software (search engines, browsers, etc.) use and render the page. Metadata for styles and scripts may be defined in the page or link to another file that has the information

  /**
    * The HTML <base> element specifies the base URL to use for all relative URLs in a document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/base
    */
  val base: TagDef = TagDef(ns, "base")

  /**
    * The HTML <head> element contains machine-readable information (metadata) about the document, like its title, scripts, and style sheets.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/head
    */
  val head: TagDef = TagDef(ns, "head")

  /**
    * The HTML External Resource Link element (<link>) specifies relationships between the current document and an external resource. This element is most commonly used to link to stylesheets, but is also used to establish site icons (both "favicon" style icons and icons for the home screen and apps on mobile devices) among other things.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/link
    */
  val link: TagDef = TagDef(ns, "link")

  /**
    * The HTML <meta> element represents metadata that cannot be represented by other HTML meta-related elements, like <base>, <link>, <script>, <style> or <title>.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/meta
    */
  val meta: TagDef = TagDef(ns, "meta")

  // Sectioning root
  // ---------------

  /**
    * The HTML <body> Element represents the content of an HTML document. There can be only one <body> element in a document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/body
    */
  val body: TagDef = TagDef(ns, "body")

  // Content sectioning
  // ------------------
  // Content sectioning elements allow you to organize the document content into logical pieces. Use the sectioning elements to create a broad outline for your page content, including header and footer navigation, and heading elements to identify sections of content.

  /**
    * The HTML <address> element indicates that the enclosed HTML provides contact information for a person or people, or for an organization.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/address
    */
  val address: TagDef = TagDef(ns, "address")

  /**
    * The HTML <article> element represents a self-contained composition in a document, page, application, or site, which is intended to be independently distributable or reusable (e.g., in syndication).
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/article
    */
  val article: TagDef = TagDef(ns, "article")

  /**
    * The HTML <aside> element represents a portion of a document whose content is only indirectly related to the document's main content.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/aside
    */
  val aside: TagDef = TagDef(ns, "aside")

  /**
    * The HTML <footer> element represents a footer for its nearest sectioning content or sectioning root element. A footer typically contains information about the author of the section, copyright data or links to related documents.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/footer
    */
  val footer: TagDef = TagDef(ns, "footer")

  /**
    * The HTML <header> element represents introductory content, typically a group of introductory or navigational aids. It may contain some heading elements but also a logo, a search form, an author name, and other elements.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/header
    */
  val header: TagDef = TagDef(ns, "header")

  /**
    * The HTML <h1>–<h6> elements represent six levels of section headings. <h1> is the highest section level and <h6> is the lowest.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/h1
    */
  val h1: TagDef = TagDef(ns, "h1")

  /**
    * The HTML <hgroup> element represents a multi-level heading for a section of a document. It groups a set of <h1>–<h6> elements.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/hgroup
    */
  val hgroup: TagDef = TagDef(ns, "hgroup")

  /**
    * The HTML <main> element represents the dominant content of the <body> of a document. The main content area consists of content that is directly related to or expands upon the central topic of a document, or the central functionality of an application.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/main
    */
  val main: TagDef = TagDef(ns, "main")

  /**
    * The HTML <nav> element represents a section of a page whose purpose is to provide navigation links, either within the current document or to other documents. Common examples of navigation sections are menus, tables of contents, and indexes.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/nav
    */
  val nav: TagDef = TagDef(ns, "nav")

  /**
    * The HTML <section> element represents a standalone section — which doesn't have a more specific semantic element to represent it — contained within an HTML document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/section
    */
  val section: TagDef = TagDef(ns, "section")

  // Text content
  // ------------

  // Use HTML text content elements to organize blocks or sections of content placed between the opening <body> and closing </body> tags. Important for accessibility and SEO, these elements identify the purpose or structure of that content.

  /**
    * The HTML <blockquote> Element (or HTML Block Quotation Element) indicates that the enclosed text is an extended quotation. Usually, this is rendered visually by indentation (see Notes for how to change it). A URL for the source of the quotation may be given using the cite attribute, while a text representation of the source can be given using the <cite> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/blockquote
    */
  val blockquote: TagDef = TagDef(ns, "blockquote")

  /**
    * The HTML <dd> element provides the description, definition, or value for the preceding term (<dt>) in a description list (<dl>).
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dd
    */
  val dd: TagDef = TagDef(ns, "dd")

  /**
    * The HTML Content Division element (<div>) is the generic container for flow content. It has no effect on the content or layout until styled using CSS.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/div
    */
  val div: TagDef = TagDef(ns, "div")

  /**
    * The HTML <dl> element represents a description list. The element encloses a list of groups of terms (specified using the <dt> element) and descriptions (provided by <dd> elements). Common uses for this element are to implement a glossary or to display metadata (a list of key-value pairs).
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dl
    */
  val dl: TagDef = TagDef(ns, "dl")

  /**
    * The HTML <dt> element specifies a term in a description or definition list, and as such must be used inside a <dl> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dt
    */
  val dt: TagDef = TagDef(ns, "dt")

  /**
    * The HTML <figcaption> or Figure Caption element represents a caption or legend describing the rest of the contents of its parent <figure> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/figcaption
    */
  val figcaption: TagDef = TagDef(ns, "figcaption")

  /**
    * The HTML <figure> (Figure With Optional Caption) element represents self-contained content, potentially with an optional caption, which is specified using the (<figcaption>) element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/figure
    */
  val figure: TagDef = TagDef(ns, "figure")

  /**
    * The HTML <hr> element represents a thematic break between paragraph-level elements:
    * for example, a change of scene in a story, or a shift of topic within a section.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/hr
    */
  val hr: TagDef = TagDef(ns, "hr")

  /**
    * The HTML <li> element is used to represent an item in a list.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/li
    */
  val li: TagDef = TagDef(ns, "li")

  /**
    * The HTML <ol> element represents an ordered list of items, typically rendered as a numbered list.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ol
    */
  val ol: TagDef = TagDef(ns, "ol")

  /**
    * The HTML <p> element represents a paragraph.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/p
    */
  val p: TagDef = TagDef(ns, "p")

  /**
    * The HTML <pre> element represents preformatted text which is to be presented exactly as written in the HTML file.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/pre
    */
  val pre: TagDef = TagDef(ns, "pre")

  /**
    * The HTML <ul> element represents an unordered list of items, typically rendered as a bulleted list.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ul
    */
  val ul: TagDef = TagDef(ns, "ul")

  // Inline text semantics
  // ---------------------

  // Use the HTML inline text semantic to define the meaning, structure, or style of a word, line, or any arbitrary piece of text.

  /**
    * The HTML <a> element (or anchor element), along with its href attribute, creates a hyperlink to other web pages, files, locations within the same page, email addresses, or any other URL.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a
    */
  val a: TagDef = TagDef(ns, "a")

  /**
    * The HTML Abbreviation element (<abbr>) represents an abbreviation or acronym; the optional title attribute can provide an expansion or description for the abbreviation.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/abbr
    */
  val abbr: TagDef = TagDef(ns, "abbr")

  /**
    * The HTML Bring Attention To element (<b>)  is used to draw the reader's attention to the element's contents, which are not otherwise granted special importance.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/b
    */
  val b: TagDef = TagDef(ns, "b")

  /**
    * The HTML Bidirectional Isolate element (<bdi>)  tells the browser's bidirectional algorithm to treat the text it contains in isolation from its surrounding text.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/bdi
    */
  val bdi: TagDef = TagDef(ns, "bdi")

  /**
    * The HTML Bidirectional Text Override element (<bdo>) overrides the current directionality of text, so that the text within is rendered in a different direction.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/bdo
    */
  val bdo: TagDef = TagDef(ns, "bdo")

  /**
    * The HTML <br> element produces a line break in text (carriage-return). It is useful for writing a poem or an address, where the division of lines is significant.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/br
    */
  @deprecated("Do not use <br> in Korolev projects. It's lead to virtual dom desynchronizing", "forever")
  val br: TagDef = TagDef(ns, "br")

  /**
    * The HTML <code> element displays its contents styled in a fashion intended to indicate that the text is a short fragment of computer code.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/code
    */
  val code: TagDef = TagDef(ns, "code")

  /**
    * The HTML <data> element links a given content with a machine-readable translation. If the content is time- or date-related, the <time> element must be used.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/data
    */
  val data: TagDef = TagDef(ns, "data")

  /**
    * The HTML Definition element (<dfn>) is used to indicate the term being defined within the context of a definition phrase or sentence.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dfn
    */
  val dfn: TagDef = TagDef(ns, "dfn")

  /**
    * The HTML <em> element marks text that has stress emphasis. The <em> element can be nested, with each level of nesting indicating a greater degree of emphasis.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/em
    */
  val em: TagDef = TagDef(ns, "em")

  /**
    * The HTML <i> element represents a range of text that is set off from the normal text for some reason. Some examples include technical terms, foreign language phrases, or fictional character thoughts. It is typically displayed in italic type.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/i
    */
  val i: TagDef = TagDef(ns, "i")

  /**
    * The HTML Keyboard Input element (<kbd>) represents a span of inline text denoting textual user input from a keyboard, voice input, or any other text entry device.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/kbd
    */
  val kbd: TagDef = TagDef(ns, "kbd")

  /**
    * The HTML Mark Text element (<mark>) represents text which is marked or highlighted for reference or notation purposes, due to the marked passage's relevance or importance in the enclosing context.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/mark
    */
  val mark: TagDef = TagDef(ns, "mark")

  /**
    * The HTML <q> element indicates that the enclosed text is a short inline quotation. Most modern browsers implement this by surrounding the text in quotation marks.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/q
    */
  val q: TagDef = TagDef(ns, "q")

  /**
    * The HTML Ruby Base (<rb>) element is used to delimit the base text component of a  <ruby> annotation, i.e. the text that is being annotated.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rb
    */
  val rb: TagDef = TagDef(ns, "rb")

  /**
    * The HTML Ruby Fallback Parenthesis (<rp>) element is used to provide fall-back parentheses for browsers that do not support display of ruby annotations using the <ruby> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rp
    */
  val rp: TagDef = TagDef(ns, "rp")

  /**
    * The HTML Ruby Text (<rt>) element specifies the ruby text component of a ruby annotation, which is used to provide pronunciation, translation, or transliteration information for East Asian typography. The <rt> element must always be contained within a <ruby> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rt
    */
  val rt: TagDef = TagDef(ns, "rt")

  /**
    * The HTML Ruby Text Container (<rtc>) element embraces semantic annotations of characters presented in a ruby of <rb> elements used inside of <ruby> element. <rb> elements can have both pronunciation (<rt>) and semantic (<rtc>) annotations.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rtc
    */
  val rtc: TagDef = TagDef(ns, "rtc")

  /**
    * The HTML <ruby> element represents a ruby annotation. Ruby annotations are for showing pronunciation of East Asian characters.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ruby
    */
  val ruby: TagDef = TagDef(ns, "ruby")

  /**
    * The HTML <s> element renders text with a strikethrough, or a line through it. Use the <s> element to represent things that are no longer relevant or no longer accurate. However, <s> is not appropriate when indicating document edits; for that, use the <del> and <ins> elements, as appropriate.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/s
    */
  val s: TagDef = TagDef(ns, "s")

  /**
    * The HTML Sample Element (<samp>) is used to enclose inline text which represents sample (or quoted) output from a computer program.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/samp
    */
  val samp: TagDef = TagDef(ns, "samp")

  /**
    * The HTML <small> element represents side-comments and small print, like copyright and legal text, independent of its styled presentation. By default, it renders text within it one font-size small, such as from small to x-small.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/small
    */
  val small: TagDef = TagDef(ns, "small")

  /**
    * The HTML Strong Importance Element (<strong>) indicates that its contents have strong importance, seriousness, or urgency. Browsers typically render the contents in bold type.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/strong
    */
  val strong: TagDef = TagDef(ns, "strong")

  /**
    * The HTML Subscript element (<sub>) specifies inline text which should be displayed as subscript for solely typographical reasons.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/sub
    */
  val sub: TagDef = TagDef(ns, "sub")

  /**
    * The HTML Superscript element (<sup>) specifies inline text which is to be displayed as superscript for solely typographical reasons.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/sup
    */
  val sup: TagDef = TagDef(ns, "sup")

  /**
    * The HTML <time> element represents a specific period in time.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/time
    */
  val time: TagDef = TagDef(ns, "time")

  /**
    * The obsolete HTML Teletype Text element (<tt>) creates inline text which is presented using the user agent's default monospace font face.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tt
    */
  val tt: TagDef = TagDef(ns, "tt")

  /**
    * The HTML Unarticulated Annotation Element (<u>) represents a span of inline text which should be rendered in a way that indicates that it has a non-textual annotation.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/u
    */
  val u: TagDef = TagDef(ns, "u")

  /**
    * The HTML Variable element (<var>) represents the name of a variable in a mathematical expression or a programming context.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/var
    */
  val `var`: TagDef = TagDef(ns, "var")

  /**
    * The HTML <wbr> element represents a word break opportunity—a position within text where the browser may optionally break a line, though its line-breaking rules would not otherwise create a break at that location.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/wbr
    */
  val wbr: TagDef = TagDef(ns, "wbr")

  // Image and multimedia
  // --------------------

  // HTML supports various multimedia resources such as images, audio, and video.

  /**
    * The HTML <area> element defines a hot-spot region on an image, and optionally associates it with a hypertext link. This element is used only within a <map> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/area
    */
  val area: TagDef = TagDef(ns, "area")

  /**
    * The HTML <audio> element is used to embed sound content in documents. It may contain one or more audio sources, represented using the src attribute or the <source> element: the browser will choose the most suitable one. It can also be the destination for streamed media, using a MediaStream.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/audio
    */
  val audio: TagDef = TagDef(ns, "audio")

  /**
    * The HTML <img> element embeds an image into the document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/img
    */
  val img: TagDef = TagDef(ns, "img")

  /**
    * The HTML <map> element is used with <area> elements to define an image map (a clickable link area).
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/map
    */
  val map: TagDef = TagDef(ns, "map")

  /**
    * The HTML <track> element is used as a child of the media elements <audio> and <video>. It lets you specify timed text tracks (or time-based data), for example to automatically handle subtitles. The tracks are formatted in WebVTT format (.vtt files) — Web Video Text Tracks or Timed Text Markup Language (TTML).
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/track
    */
  val track: TagDef = TagDef(ns, "track")

  /**
    * The HTML Video element (<video>) embeds a media player which supports video playback into the document. You can use <video> for audio content as well, but the <audio> element may provide a more appropriate user experience.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/video
    */
  val video: TagDef = TagDef(ns, "video")

  // Embedded content
  // ----------------

  // In addition to regular multimedia content, HTML can include a variety of other content, even if it's not always easy to interact with.

  /**
    * The obsolete HTML Applet Element (<applet>) embeds a Java applet into the document; this element has been deprecated in favor of <object>.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/applet
    */
  val applet: TagDef = TagDef(ns, "applet")

  /**
    * The HTML <embed> element embeds external content at the specified point in the document. This content is provided by an external application or other source of interactive content such as a browser plug-in.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/embed
    */
  val embed: TagDef = TagDef(ns, "embed")

  /**
    * The HTML Inline Frame element (<iframe>) represents a nested browsing context, embedding another HTML page into the current one.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/iframe
    */
  val iframe: TagDef = TagDef(ns, "iframe")

  /**
    * The <noembed> element is an obsolete, non-standard way to provide alternative, or "fallback", content for browsers that do not support the <embed> element or do not support the type of embedded content an author wishes to use.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/noembed
    */
  val noembed: TagDef = TagDef(ns, "noembed")

  /**
    * The HTML <object> element represents an external resource, which can be treated as an image, a nested browsing context, or a resource to be handled by a plugin.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/object
    */
  val `object`: TagDef = TagDef(ns, "object")

  /**
    * The HTML <param> element defines parameters for an <object> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/param
    */
  val param: TagDef = TagDef(ns, "param")

  /**
    * The HTML <picture> element contains zero or more <source> elements and one <img> element to provide versions of an image for different display/device scenarios.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/picture
    */
  val picture: TagDef = TagDef(ns, "picture")

  /**
    * The HTML <source> element specifies multiple media resources for the <picture>, the <audio> element, or the <video> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/source
    */
  val source: TagDef = TagDef(ns, "source")

  // Scripting
  // ---------

  // In order to create dynamic content and Web applications, HTML supports the use of scripting languages, most prominently JavaScript. Certain elements support this capability.

  /**
    * Use the HTML <canvas> element with either the canvas scripting API or the WebGL API to draw graphics and animations.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/canvas
    */
  val canvas: TagDef = TagDef(ns, "canvas")

  /**
    * The HTML <noscript> element defines a section of HTML to be inserted if a script type on the page is unsupported or if scripting is currently turned off in the browser.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/noscript
    */
  val noscript: TagDef = TagDef(ns, "noscript")

  /**
    * The HTML <script> element is used to embed or reference executable code; this is typically used to embed or refer to JavaScript code.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/script
    */
  val script: TagDef = TagDef(ns, "script")

  // Demarcating edits
  // -----------------

  // These elements let you provide indications that specific parts of the text have been altered.

  /**
    * The HTML <del> element represents a range of text that has been deleted from a document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/del
    */
  val del: TagDef = TagDef(ns, "del")

  /**
    * The HTML <ins> element represents a range of text that has been added to a document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ins
    */
  val ins: TagDef = TagDef(ns, "ins")

  // Table content
  // -------------

  // The elements here are used to create and handle tabular data.

  /**
    * The HTML Table Caption element (<caption>) specifies the caption (or title) of a table, and if used is always the first child of a <table>.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/caption
    */
  val caption: TagDef = TagDef(ns, "caption")

  /**
    * The HTML <col> element defines a column within a table and is used for defining common semantics on all common cells. It is generally found within a <colgroup> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/col
    */
  val col: TagDef = TagDef(ns, "col")

  /**
    * The HTML <colgroup> element defines a group of columns within a table.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/colgroup
    */
  val colgroup: TagDef = TagDef(ns, "colgroup")

  /**
    * The HTML <table> element represents tabular data — that is, information presented in a two-dimensional table comprised of rows and columns of cells containing data.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/table
    */
  val table: TagDef = TagDef(ns, "table")

  /**
    * The HTML Table Body element (<tbody>) encapsulates a set of table rows (<tr> elements), indicating that they comprise the body of the table (<table>).
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tbody
    */
  val tbody: TagDef = TagDef(ns, "tbody")

  /**
    * The HTML <td> element defines a cell of a table that contains data. It participates in the table model.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/td
    */
  val td: TagDef = TagDef(ns, "td")

  /**
    * The HTML <tfoot> element defines a set of rows summarizing the columns of the table.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tfoot
    */
  val tfoot: TagDef = TagDef(ns, "tfoot")

  /**
    * The HTML <th> element defines a cell as header of a group of table cells. The exact nature of this group is defined by the scope and headers attributes.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/th
    */
  val th: TagDef = TagDef(ns, "th")

  /**
    * The HTML <thead> element defines a set of rows defining the head of the columns of the table.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/thead
    */
  val thead: TagDef = TagDef(ns, "thead")

  /**
    * The HTML <tr> element defines a row of cells in a table. The row's cells can then be established using a mix of <td> (data cell) and <th> (header cell) elements.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tr
    */
  val tr: TagDef = TagDef(ns, "tr")

  // Forms
  // -----

  // HTML provides a number of elements which can be used together to create forms which the user can fill out and submit to the Web site or application. There's a great deal of further information about this available in the HTML forms guide.

  /**
    * The HTML <button> element represents a clickable button, which can be used in forms or anywhere in a document that needs simple, standard button functionality.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/button
    */
  val button: TagDef = TagDef(ns, "button")

  /**
    * The HTML <datalist> element contains a set of <option> elements that represent the values available for other controls.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/datalist
    */
  val datalist: TagDef = TagDef(ns, "datalist")

  /**
    * The HTML <fieldset> element is used to group several controls as well as labels (<label>) within a web form.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/fieldset
    */
  val fieldset: TagDef = TagDef(ns, "fieldset")

  /**
    * The HTML <input> element is used to create interactive controls for web-based forms in order to accept data from the user; a wide variety of types of input data and control widgets are available, depending on the device and user agent.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input
    */
  val input: TagDef = TagDef(ns, "input")

  /**
    * The HTML <legend> element represents a caption for the content of its parent <fieldset>.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/legend
    */
  val legend: TagDef = TagDef(ns, "legend")

  /**
    * The HTML <meter> element represents either a scalar value within a known range or a fractional value.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/meter
    */
  val meter: TagDef = TagDef(ns, "meter")

  /**
    * The HTML <optgroup> element creates a grouping of options within a <select> element.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/optgroup
    */
  val optgroup: TagDef = TagDef(ns, "optgroup")

  /**
    * The HTML <option> element is used to define an item contained in a <select>, an <optgroup>, or a <datalist> element. As such, <option> can represent menu items in popups and other lists of items in an HTML document.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/option
    */
  val option: TagDef = TagDef(ns, "option")

  /**
    * The HTML Output element (<output>) is a container element into which a site or app can inject the results of a calculation or the outcome of a user action.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/output
    */
  val output: TagDef = TagDef(ns, "output")

  /**
    * The HTML <progress> element displays an indicator showing the completion progress of a task, typically displayed as a progress bar.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/progress
    */
  val progress: TagDef = TagDef(ns, "progress")

  /**
    * The HTML <select> element represents a control that provides a menu of options
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/select
    */
  val select: TagDef = TagDef(ns, "select")

  /**
    * The HTML <textarea> element represents a multi-line plain-text editing control, useful when you want to allow users to enter a sizeable amount of free-form text, for example a comment on a review or feedback form.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/textarea
    */
  val textarea: TagDef = TagDef(ns, "textarea")

  // Interactive elements
  // --------------------
  // HTML offers a selection of elements which help to create interactive user interface objects.

  /**
    * The HTML Details Element (<details>) creates a disclosure widget in which information is visible only when the widget is toggled into an "open" state.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/details
    */
  val details: TagDef = TagDef(ns, "details")

  /**
    * The HTML <dialog> element represents a dialog box or other interactive component, such as an inspector or window.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dialog
    */
  val dialog: TagDef = TagDef(ns, "dialog")

  /**
    * The HTML <menu> element represents a group of commands that a user can perform or activate. This includes both list menus, which might appear across the top of a screen, as well as context menus, such as those that might appear underneath a button after it has been clicked.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/menu
    */
  val menu: TagDef = TagDef(ns, "menu")

  /**
    * The HTML <menuitem> element represents a command that a user is able to invoke through a popup menu. This includes context menus, as well as menus that might be attached to a menu button.
    *
    * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/menuitem
    */
  val menuitem: TagDef = TagDef(ns, "menuitem")

  // Attributes
  // https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes

  /**
    * List of types the server accepts, typically a file type.
    * Applicable to <form>, <input>
    */
  val accept: AttrDef = AttrDef(ns, "accept")
  /**
    * List of supported charsets.
    * Applicable to <form>
    */
  val acceptCharset: AttrDef = AttrDef(ns, "accept-charset")

  /**
    * Defines a keyboard shortcut to activate or add focus to the element.
    */
  val accesskey: AttrDef = AttrDef(ns, "accesskey")

  /**
    * The URI of a program that processes the information submitted via the form.
    * Applicable to <form>
    */
  val action: AttrDef = AttrDef(ns, "action")

  /**
    * Specifies the horizontal alignment of the element.
    * Applicable to <applet>, <caption>, <col>, <colgroup>, <hr>, <iframe>, <img>, <table>, <tbody>, <td>, <tfoot> , <th>, <thead>, <tr>
    */
  val align: AttrDef = AttrDef(ns, "align")

  /**
    * Specifies a feature-policy for the iframe.
    * Applicable to <iframe>
    */
  val allow: AttrDef = AttrDef(ns, "allow")

  /**
    * Alternative text in case an image can't be displayed.
    * Applicable to <applet>, <area>, <img>, <input>
    */
  val alt: AttrDef = AttrDef(ns, "alt")

  /**
    * Indicates that the script should be executed asynchronously.
    * Applicable to <script>
    */
  val async: AttrDef = AttrDef(ns, "async")

  /**
    * Controls whether and how text input is automatically capitalized as it is entered/edited by the user.
    */
  val autocapitalize: AttrDef = AttrDef(ns, "autocapitalize")

  /**
    * Indicates whether controls in this form can by default have their values automatically completed by the browser.
    * Applicable to <form>, <input>, <select>, <textarea>
    */
  val autocomplete: AttrDef = AttrDef(ns, "autocomplete")

  /**
    * The element should be automatically focused after the page loaded.
    * Applicable to <button>, <input>, <keygen>, <select>, <textarea>
    */
  val autofocus: AttrDef = AttrDef(ns, "autofocus")

  /**
    * The audio or video should play as soon as possible.
    * Applicable to <audio>, <video>
    */
  val autoplay: AttrDef = AttrDef(ns, "autoplay")

  /**
    * Contains the time range of already buffered media.
    * Applicable to <audio>, <video>
    */
  val buffered: AttrDef = AttrDef(ns, "buffered")

  /**
    * A challenge string that is submitted along with the public key.
    * Applicable to <keygen>
    */
  val challenge: AttrDef = AttrDef(ns, "challenge")

  /**
    * Declares the character encoding of the page or script.
    * Applicable to <meta>, <script>
    */
  val charset: AttrDef = AttrDef(ns, "charset")

  /**
    * Indicates whether the element should be checked on page load.
    * Applicable to <command>, <input>
    */
  val checked: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "checked", "")
  }

  /**
    * Often used with CSS to style elements with common properties.
    */
  val `class`: AttrDef = AttrDef(ns, "class")

  /**
    * Often used with CSS to style elements with common properties.
    */
  val clazz: AttrDef = AttrDef(ns, "class")

  /**
    * This attribute gives the absolute or relative URL of the directory where applets' .class files referenced by the code attribute are stored.
    * Applicable to <applet>
    */
  val codebase: AttrDef = AttrDef(ns, "codebase")

  /**
    * Defines the number of columns in a textarea.
    * Applicable to <textarea>
    */
  val cols: AttrDef = AttrDef(ns, "cols")

  /**
    * The colspan attribute defines the number of columns a cell should span.
    * Applicable to <td>, <th>
    */
  val colspan: AttrDef = AttrDef(ns, "colspan")

  /**
    * A value associated with http-equiv or name depending on the context.
    * Applicable to <meta>
    */
  val content: AttrDef = AttrDef(ns, "content")

  /**
    * Indicates whether the element's content is editable.
    */
  val contenteditable: AttrDef = AttrDef(ns, "contenteditable")

  /**
    * Defines the ID of a <menu> element which will serve as the element's context menu.
    * Applicable to Global attribute
    */
  val contextmenu: AttrDef = AttrDef(ns, "contextmenu")

  /**
    * Indicates whether the browser should show playback controls to the user.
    * Applicable to <audio>, <video>
    */
  val controls: AttrDef = AttrDef(ns, "controls")

  /**
    * A set of values specifying the coordinates of the hot-spot region.
    * Applicable to <area>
    */
  val coords: AttrDef = AttrDef(ns, "coords")

  /**
    * How the element handles cross-origin requests
    * Applicable to <audio>, <img>, <link>, <script>, <video>
    */
  val crossorigin: AttrDef = AttrDef(ns, "crossorigin")

  /**
    * Specifies the Content Security Policy that an embedded document must agree to enforce upon itself.
    * Applicable to <iframe>
    */
  val csp: AttrDef = AttrDef(ns, "csp")

  /**
    * Indicates the date and time associated with the element.
    * Applicable to <del>, <ins>, <time>
    */
  val datetime: AttrDef = AttrDef(ns, "datetime")

  /**
    * Indicates the preferred method to decode the image.
    * Applicable to <img>
    */
  val decoding: AttrDef = AttrDef(ns, "decoding")

  /**
    * Indicates that the track should be enabled unless the user's preferences indicate something different.
    * Applicable to <track>
    */
  val default: AttrDef = AttrDef(ns, "default")

  /**
    * Indicates that the script should be executed after the page has been parsed.
    * Applicable to <script>
    */
  val defer: AttrDef = AttrDef(ns, "defer")

  /**
    * Defines the text direction. Allowed values are ltr (Left-To-Right) or rtl (Right-To-Left)
    * Applicable to Global attribute
    */
  val dir: AttrDef = AttrDef(ns, "dir")

  /**
    * Applicable to <input>, <textarea>
    */
  val dirname: AttrDef = AttrDef(ns, "dirname")

  /**
    * Indicates whether the user can interact with the element.
    * Applicable to <button>, <command>, <fieldset>, <input>, <keygen>, <optgroup>, <option>, <select>, <textarea>
    */
  val disabled: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "disabled", "")
  }

  /**
    * Indicates that the hyperlink is to be used for downloading a resource.
    * Applicable to <a>, <area>
    */
  val download: AttrDef = AttrDef(ns, "download")

  /**
    * Defines whether the element can be dragged.
    */
  val draggable: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "draggable", "")
  }

  /**
    * Indicates that the element accept the dropping of content on it.
    * Applicable to Global attribute
    */
  val dropzone: AttrDef = AttrDef(ns, "dropzone")

  /**
    * Defines the content type of the form date when the method is POST.
    * Applicable to <form>
    */
  val enctype: AttrDef = AttrDef(ns, "enctype")

  /**
    * The enterkeyhint specifies what action label (or icon) to present for the enter key on virtual keyboards. The attribute can be used with form controls (such as the value of textarea elements), or in elements in an editing host (e.g., using contenteditable attribute).
    * Applicable to <textarea>, contenteditable
    */
  val enterkeyhint: AttrDef = AttrDef(ns, "enterkeyhint")

  /**
    * Describes elements which belongs to this one.
    * Applicable to <label>, <output>
    */
  val `for`: AttrDef = AttrDef(ns, "for")

  /**
    * Indicates the action of the element, overriding the action defined in the <form>.
    * Applicable to <input>, <button>
    */
  val formAction: AttrDef = AttrDef(ns, "formaction")

  /**
    * If the button/input is a submit button (type="submit"), this attribute sets the encoding type to use during form submission. If this attribute is specified, it overrides the enctype attribute of the button's form owner.
    * Applicable to <button>, <input>
    */
  val formEncType: AttrDef = AttrDef(ns, "formenctype")

  /**
    * If the button/input is a submit button (type="submit"), this attribute sets the submission method to use during form submission (GET, POST, etc.). If this attribute is specified, it overrides the method attribute of the button's form owner.
    * Applicable to <button>, <input>
    */
  val formMethod: AttrDef = AttrDef(ns, "formmethod")

  /**
    * If the button/input is a submit button (type="submit"), this boolean attribute specifies that the form is not to be validated when it is submitted. If this attribute is specified, it overrides the novalidate attribute of the button's form owner.
    * Applicable to <button>, <input>
    */
  val formNoValidate: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "formnovalidate", "")
  }

  /**
    * If the button/input is a submit button (type="submit"), this attribute specifies the browsing context (for example, tab, window, or inline frame) in which to display the response that is received after submitting the form. If this attribute is specified, it overrides the target attribute of the button's form owner.
    * Applicable to <button>, <input>
    */
  val formTarget: AttrDef = AttrDef(ns, "formtarget")

  /**
    * IDs of the <th> elements which applies to this element.
    * Applicable to <td>, <th>
    */
  val headers: AttrDef = AttrDef(ns, "headers")

  /**
    * Prevents rendering of given element, while keeping child elements, e.g. script elements, active.
    */
  val hidden: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "hidden", "")
  }

  /**
    * Indicates the lower bound of the upper range.
    * Applicable to <meter>
    */
  val high: AttrDef = AttrDef(ns, "high")

  /**
    * The URL of a linked resource.
    * Applicable to <a>, <area>, <base>, <link>
    */
  val href: AttrDef = AttrDef(ns, "href")

  /**
    * Specifies the language of the linked resource.
    * Applicable to <a>, <area>, <link>
    */
  val hreflang: AttrDef = AttrDef(ns, "hreflang")

  /**
    * Defines a pragma directive.
    * Applicable to <meta>
    */
  val `http-equiv`: AttrDef = AttrDef(ns, "http-equiv")

  /**
    * Specifies a picture which represents the command.
    * Applicable to <command>
    */
  val icon: AttrDef = AttrDef(ns, "icon")

  /**
    * Often used with CSS to style a specific element. The value of this attribute must be unique.
    * Applicable to Global attribute
    */
  val id: AttrDef = AttrDef(ns, "id")

  /**
    * Indicates the relative fetch priority for the resource.
    * Applicable to <iframe>, <img>, <link>, <script>
    */
  val importance: AttrDef = AttrDef(ns, "importance")

  /**
    * Specifies a Subresource Integrity value that allows browsers to verify what they fetch.
    * Applicable to <link>, <script>
    */
  val integrity: AttrDef = AttrDef(ns, "integrity")


  /**
    * This attribute tells the browser to ignore the actual intrinsic size of the image and pretend it’s the size specified in the attribute.
    * Applicable to <img>
    */
  val intrinsicsize: AttrDef = AttrDef(ns, "intrinsicsize")

  /**
    * Provides a hint as to the type of data that might be entered by the user while editing the element or its contents. The attribute can be used with form controls (such as the value of textarea elements), or in elements in an editing host (e.g., using contenteditable attribute).
    * Applicable to <textarea>, contenteditable
    */
  val inputmode: AttrDef = AttrDef(ns, "inputmode")

  /**
    * Indicates that the image is part of a server-side image map.
    * Applicable to <img>
    */
  val ismap: AttrDef = AttrDef(ns, "ismap")

  /**
    * attribute
    * Applicable to Global
    */
  val itemprop: AttrDef = AttrDef(ns, "itemprop")

  /**
    * Specifies the type of key generated.
    * Applicable to <keygen>
    */
  val keytype: AttrDef = AttrDef(ns, "keytype")

  /**
    * Specifies the kind of text track.
    * Applicable to <track>
    */
  val kind: AttrDef = AttrDef(ns, "kind")

  /**
    * Defines the language used in the element.
    */
  val lang: AttrDef = AttrDef(ns, "lang")

  /**
    * Defines the script language used in the element.
    * Applicable to <script>
    */
  val language: AttrDef = AttrDef(ns, "language")

  /**
    * Identifies a list of pre-defined options to suggest to the user.
    * Applicable to <input>
    */
  val list: AttrDef = AttrDef(ns, "list")

  /**
    * Indicates whether the media should start playing from the start when it's finished.
    * Applicable to <audio>, <bgsound>, <marquee>, <video>
    */
  val loop: AttrDef = AttrDef(ns, "loop")

  /**
    * Indicates the upper bound of the lower range.
    * Applicable to <meter>
    */
  val low: AttrDef = AttrDef(ns, "low")

  /**
    * Indicates the maximum value allowed.
    * Applicable to <input>, <meter>, <progress>
    */
  val max: AttrDef = AttrDef(ns, "max")

  /**
    * Defines the maximum number of characters allowed in the element.
    * Applicable to <input>, <textarea>
    */
  val maxlength: AttrDef = AttrDef(ns, "maxlength")

  /**
    * Defines the minimum number of characters allowed in the element.
    * Applicable to <input>, <textarea>
    */
  val minlength: AttrDef = AttrDef(ns, "minlength")

  /**
    * Specifies a hint of the media for which the linked resource was designed.
    * Applicable to <a>, <area>, <link>, <source>, <style>
    */
  val media: AttrDef = AttrDef(ns, "media")

  /**
    * Defines which HTTP method to use when submitting the form. Can be GET (default) or POST.
    * Applicable to <form>
    */
  val method: AttrDef = AttrDef(ns, "method")

  /**
    * Indicates the minimum value allowed.
    * Applicable to <input>, <meter>
    */
  val min: AttrDef = AttrDef(ns, "min")

  /**
    * Indicates whether multiple values can be entered in an input of the type email or file.
    * Applicable to <input>, <select>
    */
  val multiple: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "multiple", "")
  }

  /**
    * Indicates whether the audio will be initially silenced on page load.
    * Applicable to <audio>, <video>
    */
  val muted: AttrDef = AttrDef(ns, "muted")

  /**
    * Name of the element. For example used by the server to identify the fields in form submits.
    * Applicable to <button>, <form>, <fieldset>, <iframe>, <input>, <keygen>, <object>, <output>, <select>, <textarea>, <map>, <meta>, <param>
    */
  val name: AttrDef = AttrDef(ns, "name")

  /**
    * This attribute indicates that the form shouldn't be validated when submitted.
    * Applicable to <form>
    */
  val novalidate: AttrDef = AttrDef(ns, "novalidate")

  /**
    * Indicates whether the details will be shown on page load.
    * Applicable to <details>
    */
  val open: AttrDef = AttrDef(ns, "open")

  /**
    * Indicates the optimal numeric value.
    * Applicable to <meter>
    */
  val optimum: AttrDef = AttrDef(ns, "optimum")

  /**
    * Defines a regular expression which the element's value will be validated against.
    * Applicable to <input>
    */
  val pattern: AttrDef = AttrDef(ns, "pattern")

  /**
    * The ping attribute specifies a space-separated list of URLs to be notified if a user follows the hyperlink.
    * Applicable to <a>, <area>
    */
  val ping: AttrDef = AttrDef(ns, "ping")

  /**
    * Provides a hint to the user of what can be entered in the field.
    * Applicable to <input>, <textarea>
    */
  val placeholder: AttrDef = AttrDef(ns, "placeholder")

  /**
    * A URL indicating a poster frame to show until the user plays or seeks.
    * Applicable to <video>
    */
  val poster: AttrDef = AttrDef(ns, "poster")

  /**
    * Indicates whether the whole resource, parts of it or nothing should be preloaded.
    * Applicable to <audio>, <video>
    */
  val preload: AttrDef = AttrDef(ns, "preload")

  /**
    * Applicable to <command>
    */
  val radiogroup: AttrDef = AttrDef(ns, "radiogroup")

  /**
    * Indicates whether the element can be edited.
    * Applicable to <input>, <textarea>
    */
  val readonly: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "readonly", "")
  }

  /**
    * Specifies which referrer is sent when fetching the resource.
    * Applicable to <a>, <area>, <iframe>, <img>, <link>, <script>
    */
  val referrerpolicy: AttrDef = AttrDef(ns, "referrerpolicy")

  /**
    * Specifies the relationship of the target object to the link object.
    * Applicable to <a>, <area>, <link>
    */
  val rel: AttrDef = AttrDef(ns, "rel")

  /**
    * Indicates whether this element is required to fill out or not.
    * Applicable to <input>, <select>, <textarea>
    */
  val required: AttrDef = AttrDef(ns, "required")

  /**
    * Indicates whether the list should be displayed in a descending order instead of a ascending.
    * Applicable to <ol>
    */
  val reversed: AttrDef = AttrDef(ns, "reversed")

  /**
    * Defines the number of rows in a text area.
    * Applicable to <textarea>
    */
  val rows: AttrDef = AttrDef(ns, "rows")

  /**
    * Defines the number of rows a table cell should span over.
    * Applicable to <td>, <th>
    */
  val rowspan: AttrDef = AttrDef(ns, "rowspan")

  /**
    * Stops a document loaded in an iframe from using certain features (such as submitting forms or opening new windows).
    * Applicable to <iframe>
    */
  val sandbox: AttrDef = AttrDef(ns, "sandbox")

  /**
    * Defines the cells that the header test (defined in the th element) relates to.
    * Applicable to <th>
    */
  val scope: AttrDef = AttrDef(ns, "scope")

  /**
    * Applicable to <style>
    */
  val scoped: AttrDef = AttrDef(ns, "scoped")

  /**
    * Defines a value which will be selected on page load.
    * Applicable to <option>
    */
  val selected: Attr[Nothing] = Attr[Nothing] { rc =>
    rc.setAttr(ns, "selected", "")
  }

  /**
    * Applicable to <a>, <area>
    */
  val shape: AttrDef = AttrDef(ns, "shape")

  /**
    * Defines the width of the element (in pixels). If the element's type attribute is text or password then it's the number of characters.
    * Applicable to <input>, <select>
    */
  val size: AttrDef = AttrDef(ns, "size")

  /**
    * Applicable to <link>, <img>, <source>
    */
  val sizes: AttrDef = AttrDef(ns, "sizes")

  /**
    * Assigns a slot in a shadow DOM shadow tree to an element.
    * Applicable to Global attribute
    */
  val slot: AttrDef = AttrDef(ns, "slot")

  /**
    * Indicates whether spell checking is allowed for the element.
    * Applicable to Global attribute
    */
  val spellcheck: AttrDef = AttrDef(ns, "spellcheck")

  /**
    * The URL of the embeddable content.
    * Applicable to <audio>, <embed>, <iframe>, <img>, <input>, <script>, <source>, <track>, <video>
    */
  val src: AttrDef = AttrDef(ns, "src")

  /**
    * Applicable to <iframe>
    */
  val srcdoc: AttrDef = AttrDef(ns, "srcdoc")

  /**
    * Applicable to <track>
    */
  val srclang: AttrDef = AttrDef(ns, "srclang")

  /**
    * One or more responsive image candidates.
    * Applicable to <img>, <source>
    */
  val srcset: AttrDef = AttrDef(ns, "srcset")

  /**
    * Defines the first number if other than 1.
    * Applicable to <ol>
    */
  val start: AttrDef = AttrDef(ns, "start")

  /**
    * Applicable to <input>
    */
  val step: AttrDef = AttrDef(ns, "step")

  /**
    * Overrides the browser's default tab order and follows the one specified instead.
    * Applicable to Global attribute
    */
  val tabindex: AttrDef = AttrDef(ns, "tabindex")

  /**
    * Applicable to <a>, <area>, <base>, <form>
    */
  val target: AttrDef = AttrDef(ns, "target")

  /**
    * Specify whether an element ’s attribute values and the values of its Text node children are to be translated when the page is localized, or whether to leave them unchanged.
    * Applicable to Global attribute
    */
  val translate: AttrDef = AttrDef(ns, "translate")

  /**
    * Defines the type of the element.
    * Applicable to <button>, <input>, <command>, <embed>, <object>, <script>, <source>, <style>, <menu>
    */
  val `type`: AttrDef = AttrDef(ns, "type")

  /**
    * <object>
    * Applicable to <img>, <input>,
    */
  val usemap: AttrDef = AttrDef(ns, "usemap")

  /**
    * Defines a default value which will be displayed in the element on page load.
    * Applicable to <button>, <data>, <input>, <li>, <meter>, <option>, <progress>, <param>
    */
  val value: AttrDef = AttrDef(ns, "value")

  /**
    * Indicates whether the text should be wrapped.
    * Applicable to <textarea>
    */
  val wrap: AttrDef = AttrDef(ns, "wrap")


  // Common CSS Properties Reference
  // https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Properties_Reference

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-attachment
    */
  val backgroundAttachment: StyleDef = StyleDef("background-attachment")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-color
    */
  val backgroundColor: StyleDef = StyleDef("background-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-image
    */
  val backgroundImage: StyleDef = StyleDef("background-image")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-position
    */
  val backgroundPosition: StyleDef = StyleDef("background-position")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-repeat
    */
  val backgroundRepeat: StyleDef = StyleDef("background-repeat")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border
    */
  val border: StyleDef = StyleDef("border")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom
    */
  val borderBottom: StyleDef = StyleDef("border-bottom")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom-color
    */
  val borderBottomColor: StyleDef = StyleDef("border-bottom-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom-style
    */
  val borderBottomStyle: StyleDef = StyleDef("border-bottom-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom-width
    */
  val borderBottomWidth: StyleDef = StyleDef("border-bottom-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-color
    */
  val borderColor: StyleDef = StyleDef("border-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left
    */
  val borderLeft: StyleDef = StyleDef("border-left")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left-color
    */
  val borderLeftColor: StyleDef = StyleDef("border-left-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left-style
    */
  val borderLeftStyle: StyleDef = StyleDef("border-left-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left-width
    */
  val borderLeftWidth: StyleDef = StyleDef("border-left-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right
    */
  val borderRight: StyleDef = StyleDef("border-right")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right-color
    */
  val borderRightColor: StyleDef = StyleDef("border-right-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right-style
    */
  val borderRightStyle: StyleDef = StyleDef("border-right-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right-width
    */
  val borderRightWidth: StyleDef = StyleDef("border-right-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-style
    */
  val borderStyle: StyleDef = StyleDef("border-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top
    */
  val borderTop: StyleDef = StyleDef("border-top")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top-color
    */
  val borderTopColor: StyleDef = StyleDef("border-top-color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top-style
    */
  val borderTopStyle: StyleDef = StyleDef("border-top-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top-width
    */
  val borderTopWidth: StyleDef = StyleDef("border-top-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-width
    */
  val borderWidth: StyleDef = StyleDef("border-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/clear
    */
  val clear: StyleDef = StyleDef("clear")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/clip
    */
  val clip: StyleDef = StyleDef("clip")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/color
    */
  val color: StyleDef = StyleDef("color")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/cursor
    */
  val cursor: StyleDef = StyleDef("cursor")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/display
    */
  val display: StyleDef = StyleDef("display")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/filter
    */
  val filter: StyleDef = StyleDef("filter")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/float
    */
  val cssFloat: StyleDef = StyleDef("float")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font
    */
  val font: StyleDef = StyleDef("font")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-family
    */
  val fontFamily: StyleDef = StyleDef("font-family")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-size
    */
  val fontSize: StyleDef = StyleDef("font-size")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-variant
    */
  val fontVariant: StyleDef = StyleDef("font-variant")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-weight
    */
  val fontWeight: StyleDef = StyleDef("font-weight")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/height
    */
  val height: StyleDef = StyleDef("height")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/left
    */
  val left: StyleDef = StyleDef("left")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/letter-spacing
    */
  val letterSpacing: StyleDef = StyleDef("letter-spacing")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/line-height
    */
  val lineHeight: StyleDef = StyleDef("line-height")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style
    */
  val listStyle: StyleDef = StyleDef("list-style")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-image
    */
  val listStyleImage: StyleDef = StyleDef("list-style-image")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-position
    */
  val listStylePosition: StyleDef = StyleDef("list-style-position")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-type
    */
  val listStyleType: StyleDef = StyleDef("list-style-type")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin
    */
  val margin: StyleDef = StyleDef("margin")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-bottom
    */
  val marginBottom: StyleDef = StyleDef("margin-bottom")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-left
    */
  val marginLeft: StyleDef = StyleDef("margin-left")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-right
    */
  val marginRight: StyleDef = StyleDef("margin-right")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-top
    */
  val marginTop: StyleDef = StyleDef("margin-top")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/overflow
    */
  val overflow: StyleDef = StyleDef("overflow")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding
    */
  val padding: StyleDef = StyleDef("padding")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-bottom
    */
  val paddingBottom: StyleDef = StyleDef("padding-bottom")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-left
    */
  val paddingLeft: StyleDef = StyleDef("padding-left")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-right
    */
  val paddingRight: StyleDef = StyleDef("padding-right")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-top
    */
  val paddingTop: StyleDef = StyleDef("padding-top")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/page-break-after
    */
  val pageBreakAfter: StyleDef = StyleDef("page-break-after")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/page-break-before
    */
  val pageBreakBefore: StyleDef = StyleDef("page-break-before")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/position
    */
  val position: StyleDef = StyleDef("position")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/stroke-dasharray
    */
  val strokeDasharray: StyleDef = StyleDef("stroke-dasharray")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/stroke-dashoffset
    */
  val strokeDashoffset: StyleDef = StyleDef("stroke-dashoffset")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/stroke-width
    */
  val strokeWidth: StyleDef = StyleDef("stroke-width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-align
    */
  val textAlign: StyleDef = StyleDef("text-align")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-decoration
    */
  val textDecoration: StyleDef = StyleDef("text-decoration")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-indent
    */
  val textIndent: StyleDef = StyleDef("text-indent")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-transform
    */
  val textTransform: StyleDef = StyleDef("text-transform")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/top
    */
  val top: StyleDef = StyleDef("top")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/vertical-align
    */
  val verticalAlign: StyleDef = StyleDef("vertical-align")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/visibility
    */
  val visibility: StyleDef = StyleDef("visibility")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/width
    */
  val width: StyleDef = StyleDef("width")

  /**
    * @see https://developer.mozilla.org/en-US/docs/Web/CSS/z-index
    */
  val zIndex: StyleDef = StyleDef("z-index")
}
