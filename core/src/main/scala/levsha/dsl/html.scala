package levsha.dsl

import levsha.XmlNs.{html => ns}

/**
  * HTML Tags, Attributes, and Styles specification
  */
object html {

  /**
    * https://developer.mozilla.org/en-US/docs/Web/HTML/Element
    */
  object tags {

    /**
      * The HTML <html> element represents the root (top-level element) of an HTML document, so it is also referred to as the root element. All other elements must be descendants of this element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/html
      */
    val html = TagDef(ns, "html")

    // Document metadata
    // -----------------
    // Metadata contains information about the page. This includes information about styles, scripts and data to help software (search engines, browsers, etc.) use and render the page. Metadata for styles and scripts may be defined in the page or link to another file that has the information

    /**
      * The HTML <base> element specifies the base URL to use for all relative URLs in a document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/base
      */
    val base = TagDef(ns, "base")

    /**
      * The HTML <head> element contains machine-readable information (metadata) about the document, like its title, scripts, and style sheets.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/head
      */
    val head = TagDef(ns, "head")

    /**
      * The HTML External Resource Link element (<link>) specifies relationships between the current document and an external resource. This element is most commonly used to link to stylesheets, but is also used to establish site icons (both "favicon" style icons and icons for the home screen and apps on mobile devices) among other things.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/link
      */
    val link = TagDef(ns, "link")

    /**
      * The HTML <meta> element represents metadata that cannot be represented by other HTML meta-related elements, like <base>, <link>, <script>, <style> or <title>.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/meta
      */
    val meta = TagDef(ns, "meta")

    /**
      * The HTML <style> element contains style information for a document, or part of a document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/style
      */
    val style = TagDef(ns, "style")

    /**
      * The HTML Title element (<title>) defines the document's title that is shown in a browser's title bar or a page's tab.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/title
      */
    val title = TagDef(ns, "title")

    // Sectioning root
    // ---------------

    /**
      * The HTML <body> Element represents the content of an HTML document. There can be only one <body> element in a document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/body
      */
    val body = TagDef(ns, "body")

    // Content sectioning
    // ------------------
    // Content sectioning elements allow you to organize the document content into logical pieces. Use the sectioning elements to create a broad outline for your page content, including header and footer navigation, and heading elements to identify sections of content.

    /**
      * The HTML <address> element indicates that the enclosed HTML provides contact information for a person or people, or for an organization.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/address
      */
    val address = TagDef(ns, "address")

    /**
      * The HTML <article> element represents a self-contained composition in a document, page, application, or site, which is intended to be independently distributable or reusable (e.g., in syndication).
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/article
      */
    val article = TagDef(ns, "article")

    /**
      * The HTML <aside> element represents a portion of a document whose content is only indirectly related to the document's main content.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/aside
      */
    val aside = TagDef(ns, "aside")

    /**
      * The HTML <footer> element represents a footer for its nearest sectioning content or sectioning root element. A footer typically contains information about the author of the section, copyright data or links to related documents.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/footer
      */
    val footer = TagDef(ns, "footer")

    /**
      * The HTML <header> element represents introductory content, typically a group of introductory or navigational aids. It may contain some heading elements but also a logo, a search form, an author name, and other elements.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/header
      */
    val header = TagDef(ns, "header")

    /**
      * The HTML <h1>–<h6> elements represent six levels of section headings. <h1> is the highest section level and <h6> is the lowest.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/h1
      */
    val h1 = TagDef(ns, "h1")

    /**
      * The HTML <hgroup> element represents a multi-level heading for a section of a document. It groups a set of <h1>–<h6> elements.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/hgroup
      */
    val hgroup = TagDef(ns, "hgroup")

    /**
      * The HTML <main> element represents the dominant content of the <body> of a document. The main content area consists of content that is directly related to or expands upon the central topic of a document, or the central functionality of an application.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/main
      */
    val main = TagDef(ns, "main")

    /**
      * The HTML <nav> element represents a section of a page whose purpose is to provide navigation links, either within the current document or to other documents. Common examples of navigation sections are menus, tables of contents, and indexes.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/nav
      */
    val nav = TagDef(ns, "nav")

    /**
      * The HTML <section> element represents a standalone section — which doesn't have a more specific semantic element to represent it — contained within an HTML document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/section
      */
    val section = TagDef(ns, "section")

    // Text content
    // ------------

    // Use HTML text content elements to organize blocks or sections of content placed between the opening <body> and closing </body> tags. Important for accessibility and SEO, these elements identify the purpose or structure of that content.

    /**
      * The HTML <blockquote> Element (or HTML Block Quotation Element) indicates that the enclosed text is an extended quotation. Usually, this is rendered visually by indentation (see Notes for how to change it). A URL for the source of the quotation may be given using the cite attribute, while a text representation of the source can be given using the <cite> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/blockquote
      */
    val blockquote = TagDef(ns, "blockquote")

    /**
      * The HTML <dd> element provides the description, definition, or value for the preceding term (<dt>) in a description list (<dl>).
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dd
      */
    val dd = TagDef(ns, "dd")

    /**
      * The obsolete HTML Directory element (<dir>) is used as a container for a directory of files and/or folders, potentially with styles and icons applied by the user agent.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dir
      */
    val dir = TagDef(ns, "dir")

    /**
      * The HTML Content Division element (<div>) is the generic container for flow content. It has no effect on the content or layout until styled using CSS.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/div
      */
    val div = TagDef(ns, "div")

    /**
      * The HTML <dl> element represents a description list. The element encloses a list of groups of terms (specified using the <dt> element) and descriptions (provided by <dd> elements). Common uses for this element are to implement a glossary or to display metadata (a list of key-value pairs).
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dl
      */
    val dl = TagDef(ns, "dl")

    /**
      * The HTML <dt> element specifies a term in a description or definition list, and as such must be used inside a <dl> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dt
      */
    val dt = TagDef(ns, "dt")

    /**
      * The HTML <figcaption> or Figure Caption element represents a caption or legend describing the rest of the contents of its parent <figure> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/figcaption
      */
    val figcaption = TagDef(ns, "figcaption")

    /**
      * The HTML <figure> (Figure With Optional Caption) element represents self-contained content, potentially with an optional caption, which is specified using the (<figcaption>) element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/figure
      */
    val figure = TagDef(ns, "figure")

    /**
      * The HTML <hr> element represents a thematic break between paragraph-level elements:
      * for example, a change of scene in a story, or a shift of topic within a section.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/hr
      */
    val hr = TagDef(ns, "hr")

    /**
      * The HTML <li> element is used to represent an item in a list.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/li
      */
    val li = TagDef(ns, "li")

    /**
      * The HTML <ol> element represents an ordered list of items, typically rendered as a numbered list.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ol
      */
    val ol = TagDef(ns, "ol")

    /**
      * The HTML <p> element represents a paragraph.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/p
      */
    val p = TagDef(ns, "p")

    /**
      * The HTML <pre> element represents preformatted text which is to be presented exactly as written in the HTML file.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/pre
      */
    val pre = TagDef(ns, "pre")

    /**
      * The HTML <ul> element represents an unordered list of items, typically rendered as a bulleted list.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ul
      */
    val ul = TagDef(ns, "ul")

    // Inline text semantics
    // ---------------------

    // Use the HTML inline text semantic to define the meaning, structure, or style of a word, line, or any arbitrary piece of text.

    /**
      * The HTML <a> element (or anchor element), along with its href attribute, creates a hyperlink to other web pages, files, locations within the same page, email addresses, or any other URL.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a
      */
    val a = TagDef(ns, "a")

    /**
      * The HTML Abbreviation element (<abbr>) represents an abbreviation or acronym; the optional title attribute can provide an expansion or description for the abbreviation.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/abbr
      */
    val abbr = TagDef(ns, "abbr")

    /**
      * The HTML Bring Attention To element (<b>)  is used to draw the reader's attention to the element's contents, which are not otherwise granted special importance.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/b
      */
    val b = TagDef(ns, "b")

    /**
      * The HTML Bidirectional Isolate element (<bdi>)  tells the browser's bidirectional algorithm to treat the text it contains in isolation from its surrounding text.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/bdi
      */
    val bdi = TagDef(ns, "bdi")

    /**
      * The HTML Bidirectional Text Override element (<bdo>) overrides the current directionality of text, so that the text within is rendered in a different direction.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/bdo
      */
    val bdo = TagDef(ns, "bdo")

    /**
      * The HTML <br> element produces a line break in text (carriage-return). It is useful for writing a poem or an address, where the division of lines is significant.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/br
      */
    val br = TagDef(ns, "br")

    /**
      * The HTML Citation element (<cite>) is used to describe a reference to a cited creative work, and must include the title of that work.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/cite
      */
    val cite = TagDef(ns, "cite")

    /**
      * The HTML <code> element displays its contents styled in a fashion intended to indicate that the text is a short fragment of computer code.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/code
      */
    val code = TagDef(ns, "code")

    /**
      * The HTML <data> element links a given content with a machine-readable translation. If the content is time- or date-related, the <time> element must be used.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/data
      */
    val data = TagDef(ns, "data")

    /**
      * The HTML Definition element (<dfn>) is used to indicate the term being defined within the context of a definition phrase or sentence.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dfn
      */
    val dfn = TagDef(ns, "dfn")

    /**
      * The HTML <em> element marks text that has stress emphasis. The <em> element can be nested, with each level of nesting indicating a greater degree of emphasis.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/em
      */
    val em = TagDef(ns, "em")

    /**
      * The HTML <i> element represents a range of text that is set off from the normal text for some reason. Some examples include technical terms, foreign language phrases, or fictional character thoughts. It is typically displayed in italic type.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/i
      */
    val i = TagDef(ns, "i")

    /**
      * The HTML Keyboard Input element (<kbd>) represents a span of inline text denoting textual user input from a keyboard, voice input, or any other text entry device.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/kbd
      */
    val kbd = TagDef(ns, "kbd")

    /**
      * The HTML Mark Text element (<mark>) represents text which is marked or highlighted for reference or notation purposes, due to the marked passage's relevance or importance in the enclosing context.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/mark
      */
    val mark = TagDef(ns, "mark")

    /**
      * The HTML <q> element indicates that the enclosed text is a short inline quotation. Most modern browsers implement this by surrounding the text in quotation marks.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/q
      */
    val q = TagDef(ns, "q")

    /**
      * The HTML Ruby Base (<rb>) element is used to delimit the base text component of a  <ruby> annotation, i.e. the text that is being annotated.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rb
      */
    val rb = TagDef(ns, "rb")

    /**
      * The HTML Ruby Fallback Parenthesis (<rp>) element is used to provide fall-back parentheses for browsers that do not support display of ruby annotations using the <ruby> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rp
      */
    val rp = TagDef(ns, "rp")

    /**
      * The HTML Ruby Text (<rt>) element specifies the ruby text component of a ruby annotation, which is used to provide pronunciation, translation, or transliteration information for East Asian typography. The <rt> element must always be contained within a <ruby> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rt
      */
    val rt = TagDef(ns, "rt")

    /**
      * The HTML Ruby Text Container (<rtc>) element embraces semantic annotations of characters presented in a ruby of <rb> elements used inside of <ruby> element. <rb> elements can have both pronunciation (<rt>) and semantic (<rtc>) annotations.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/rtc
      */
    val rtc = TagDef(ns, "rtc")

    /**
      * The HTML <ruby> element represents a ruby annotation. Ruby annotations are for showing pronunciation of East Asian characters.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ruby
      */
    val ruby = TagDef(ns, "ruby")

    /**
      * The HTML <s> element renders text with a strikethrough, or a line through it. Use the <s> element to represent things that are no longer relevant or no longer accurate. However, <s> is not appropriate when indicating document edits; for that, use the <del> and <ins> elements, as appropriate.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/s
      */
    val s = TagDef(ns, "s")

    /**
      * The HTML Sample Element (<samp>) is used to enclose inline text which represents sample (or quoted) output from a computer program.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/samp
      */
    val samp = TagDef(ns, "samp")

    /**
      * The HTML <small> element represents side-comments and small print, like copyright and legal text, independent of its styled presentation. By default, it renders text within it one font-size small, such as from small to x-small.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/small
      */
    val small = TagDef(ns, "small")

    /**
      * The HTML <span> element is a generic inline container for phrasing content, which does not inherently represent anything. It can be used to group elements for styling purposes (using the class or id attributes), or because they share attribute values, such as lang.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/span
      */
    val span = TagDef(ns, "span")

    /**
      * The HTML Strong Importance Element (<strong>) indicates that its contents have strong importance, seriousness, or urgency. Browsers typically render the contents in bold type.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/strong
      */
    val strong = TagDef(ns, "strong")

    /**
      * The HTML Subscript element (<sub>) specifies inline text which should be displayed as subscript for solely typographical reasons.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/sub
      */
    val sub = TagDef(ns, "sub")

    /**
      * The HTML Superscript element (<sup>) specifies inline text which is to be displayed as superscript for solely typographical reasons.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/sup
      */
    val sup = TagDef(ns, "sup")

    /**
      * The HTML <time> element represents a specific period in time.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/time
      */
    val time = TagDef(ns, "time")

    /**
      * The obsolete HTML Teletype Text element (<tt>) creates inline text which is presented using the user agent's default monospace font face.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tt
      */
    val tt = TagDef(ns, "tt")

    /**
      * The HTML Unarticulated Annotation Element (<u>) represents a span of inline text which should be rendered in a way that indicates that it has a non-textual annotation.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/u
      */
    val u = TagDef(ns, "u")

    /**
      * The HTML Variable element (<var>) represents the name of a variable in a mathematical expression or a programming context.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/var
      */
    val `var` = TagDef(ns, "var")

    /**
      * The HTML <wbr> element represents a word break opportunity—a position within text where the browser may optionally break a line, though its line-breaking rules would not otherwise create a break at that location.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/wbr
      */
    val wbr = TagDef(ns, "wbr")

    // Image and multimedia
    // --------------------

    // HTML supports various multimedia resources such as images, audio, and video.

    /**
      * The HTML <area> element defines a hot-spot region on an image, and optionally associates it with a hypertext link. This element is used only within a <map> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/area
      */
    val area = TagDef(ns, "area")

    /**
      * The HTML <audio> element is used to embed sound content in documents. It may contain one or more audio sources, represented using the src attribute or the <source> element: the browser will choose the most suitable one. It can also be the destination for streamed media, using a MediaStream.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/audio
      */
    val audio = TagDef(ns, "audio")

    /**
      * The HTML <img> element embeds an image into the document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/img
      */
    val img = TagDef(ns, "img")

    /**
      * The HTML <map> element is used with <area> elements to define an image map (a clickable link area).
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/map
      */
    val map = TagDef(ns, "map")

    /**
      * The HTML <track> element is used as a child of the media elements <audio> and <video>. It lets you specify timed text tracks (or time-based data), for example to automatically handle subtitles. The tracks are formatted in WebVTT format (.vtt files) — Web Video Text Tracks or Timed Text Markup Language (TTML).
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/track
      */
    val track = TagDef(ns, "track")

    /**
      * The HTML Video element (<video>) embeds a media player which supports video playback into the document. You can use <video> for audio content as well, but the <audio> element may provide a more appropriate user experience.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/video
      */
    val video = TagDef(ns, "video")

    // Embedded content
    // ----------------

    // In addition to regular multimedia content, HTML can include a variety of other content, even if it's not always easy to interact with.

    /**
      * The obsolete HTML Applet Element (<applet>) embeds a Java applet into the document; this element has been deprecated in favor of <object>.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/applet
      */
    val applet = TagDef(ns, "applet")

    /**
      * The HTML <embed> element embeds external content at the specified point in the document. This content is provided by an external application or other source of interactive content such as a browser plug-in.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/embed
      */
    val embed = TagDef(ns, "embed")

    /**
      * The HTML Inline Frame element (<iframe>) represents a nested browsing context, embedding another HTML page into the current one.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/iframe
      */
    val iframe = TagDef(ns, "iframe")

    /**
      * The <noembed> element is an obsolete, non-standard way to provide alternative, or "fallback", content for browsers that do not support the <embed> element or do not support the type of embedded content an author wishes to use.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/noembed
      */
    val noembed = TagDef(ns, "noembed")

    /**
      * The HTML <object> element represents an external resource, which can be treated as an image, a nested browsing context, or a resource to be handled by a plugin.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/object
      */
    val `object` = TagDef(ns, "object")

    /**
      * The HTML <param> element defines parameters for an <object> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/param
      */
    val param = TagDef(ns, "param")

    /**
      * The HTML <picture> element contains zero or more <source> elements and one <img> element to provide versions of an image for different display/device scenarios.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/picture
      */
    val picture = TagDef(ns, "picture")

    /**
      * The HTML <source> element specifies multiple media resources for the <picture>, the <audio> element, or the <video> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/source
      */
    val source = TagDef(ns, "source")

    // Scripting
    // ---------

    // In order to create dynamic content and Web applications, HTML supports the use of scripting languages, most prominently JavaScript. Certain elements support this capability.

    /**
      * Use the HTML <canvas> element with either the canvas scripting API or the WebGL API to draw graphics and animations.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/canvas
      */
    val canvas = TagDef(ns, "canvas")

    /**
      * The HTML <noscript> element defines a section of HTML to be inserted if a script type on the page is unsupported or if scripting is currently turned off in the browser.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/noscript
      */
    val noscript = TagDef(ns, "noscript")

    /**
      * The HTML <script> element is used to embed or reference executable code; this is typically used to embed or refer to JavaScript code.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/script
      */
    val script = TagDef(ns, "script")

    // Demarcating edits
    // -----------------

    // These elements let you provide indications that specific parts of the text have been altered.

    /**
      * The HTML <del> element represents a range of text that has been deleted from a document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/del
      */
    val del = TagDef(ns, "del")

    /**
      * The HTML <ins> element represents a range of text that has been added to a document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ins
      */
    val ins = TagDef(ns, "ins")

    // Table content
    // -------------

    // The elements here are used to create and handle tabular data.

    /**
      * The HTML Table Caption element (<caption>) specifies the caption (or title) of a table, and if used is always the first child of a <table>.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/caption
      */
    val caption = TagDef(ns, "caption")

    /**
      * The HTML <col> element defines a column within a table and is used for defining common semantics on all common cells. It is generally found within a <colgroup> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/col
      */
    val col = TagDef(ns, "col")

    /**
      * The HTML <colgroup> element defines a group of columns within a table.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/colgroup
      */
    val colgroup = TagDef(ns, "colgroup")

    /**
      * The HTML <table> element represents tabular data — that is, information presented in a two-dimensional table comprised of rows and columns of cells containing data.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/table
      */
    val table = TagDef(ns, "table")

    /**
      * The HTML Table Body element (<tbody>) encapsulates a set of table rows (<tr> elements), indicating that they comprise the body of the table (<table>).
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tbody
      */
    val tbody = TagDef(ns, "tbody")

    /**
      * The HTML <td> element defines a cell of a table that contains data. It participates in the table model.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/td
      */
    val td = TagDef(ns, "td")

    /**
      * The HTML <tfoot> element defines a set of rows summarizing the columns of the table.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tfoot
      */
    val tfoot = TagDef(ns, "tfoot")

    /**
      * The HTML <th> element defines a cell as header of a group of table cells. The exact nature of this group is defined by the scope and headers attributes.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/th
      */
    val th = TagDef(ns, "th")

    /**
      * The HTML <thead> element defines a set of rows defining the head of the columns of the table.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/thead
      */
    val thead = TagDef(ns, "thead")

    /**
      * The HTML <tr> element defines a row of cells in a table. The row's cells can then be established using a mix of <td> (data cell) and <th> (header cell) elements.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/tr
      */
    val tr = TagDef(ns, "tr")

    // Forms
    // -----

    // HTML provides a number of elements which can be used together to create forms which the user can fill out and submit to the Web site or application. There's a great deal of further information about this available in the HTML forms guide.

    /**
      * The HTML <button> element represents a clickable button, which can be used in forms or anywhere in a document that needs simple, standard button functionality.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/button
      */
    val button = TagDef(ns, "button")

    /**
      * The HTML <datalist> element contains a set of <option> elements that represent the values available for other controls.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/datalist
      */
    val datalist = TagDef(ns, "datalist")

    /**
      * The HTML <fieldset> element is used to group several controls as well as labels (<label>) within a web form.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/fieldset
      */
    val fieldset = TagDef(ns, "fieldset")

    /**
      * The HTML <form> element represents a document section that contains interactive controls for submitting information to a web server.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/form
      */
    val form = TagDef(ns, "form")

    /**
      * The HTML <input> element is used to create interactive controls for web-based forms in order to accept data from the user; a wide variety of types of input data and control widgets are available, depending on the device and user agent.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input
      */
    val input = TagDef(ns, "input")

    /**
      * The HTML <label> element represents a caption for an item in a user interface.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/label
      */
    val label = TagDef(ns, "label")

    /**
      * The HTML <legend> element represents a caption for the content of its parent <fieldset>.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/legend
      */
    val legend = TagDef(ns, "legend")

    /**
      * The HTML <meter> element represents either a scalar value within a known range or a fractional value.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/meter
      */
    val meter = TagDef(ns, "meter")

    /**
      * The HTML <optgroup> element creates a grouping of options within a <select> element.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/optgroup
      */
    val optgroup = TagDef(ns, "optgroup")

    /**
      * The HTML <option> element is used to define an item contained in a <select>, an <optgroup>, or a <datalist> element. As such, <option> can represent menu items in popups and other lists of items in an HTML document.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/option
      */
    val option = TagDef(ns, "option")

    /**
      * The HTML Output element (<output>) is a container element into which a site or app can inject the results of a calculation or the outcome of a user action.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/output
      */
    val output = TagDef(ns, "output")

    /**
      * The HTML <progress> element displays an indicator showing the completion progress of a task, typically displayed as a progress bar.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/progress
      */
    val progress = TagDef(ns, "progress")

    /**
      * The HTML <select> element represents a control that provides a menu of options
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/select
      */
    val select = TagDef(ns, "select")

    /**
      * The HTML <textarea> element represents a multi-line plain-text editing control, useful when you want to allow users to enter a sizeable amount of free-form text, for example a comment on a review or feedback form.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/textarea
      */
    val textarea = TagDef(ns, "textarea")

    // Interactive elements
    // --------------------
    // HTML offers a selection of elements which help to create interactive user interface objects.

    /**
      * The HTML Details Element (<details>) creates a disclosure widget in which information is visible only when the widget is toggled into an "open" state.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/details
      */
    val details = TagDef(ns, "details")

    /**
      * The HTML <dialog> element represents a dialog box or other interactive component, such as an inspector or window.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dialog
      */
    val dialog = TagDef(ns, "dialog")

    /**
      * The HTML <menu> element represents a group of commands that a user can perform or activate. This includes both list menus, which might appear across the top of a screen, as well as context menus, such as those that might appear underneath a button after it has been clicked.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/menu
      */
    val menu = TagDef(ns, "menu")

    /**
      * The HTML <menuitem> element represents a command that a user is able to invoke through a popup menu. This includes context menus, as well as menus that might be attached to a menu button.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/menuitem
      */
    val menuitem = TagDef(ns, "menuitem")

    /**
      * The HTML Disclosure Summary element (<summary>) element specifies a summary, caption, or legend for a <details> element's disclosure box.
      *
      * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/summary
      */
    val summary = TagDef(ns, "summary")
  }

  /**
    * https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes
    */
  object attributes {
    /**
      * List of types the server accepts, typically a file type.
      * Applicable to <form>, <input>
      */
    val accept = AttrDef[String](ns, "accept", identity)
    /**
      * List of supported charsets.
      * Applicable to <form>
      */
    val acceptCharset = AttrDef[String](ns, "accept-charset", identity)

    /**
      * Defines a keyboard shortcut to activate or add focus to the element.
      */
    val accesskey = AttrDef[String](ns, "accesskey", identity)

    /**
      * The URI of a program that processes the information submitted via the form.
      * Applicable to <form>
      */
    val action = AttrDef[String](ns, "action", identity)

    /**
      * Specifies the horizontal alignment of the element.
      * Applicable to <applet>, <caption>, <col>, <colgroup>, <hr>, <iframe>, <img>, <table>, <tbody>, <td>, <tfoot> , <th>, <thead>, <tr>
      */
    val align = AttrDef[String](ns, "align", identity)

    /**
      * Specifies a feature-policy for the iframe.
      * Applicable to <iframe>
      */
    val allow = AttrDef[String](ns, "allow", identity)

    /**
      * Alternative text in case an image can't be displayed.
      * Applicable to <applet>, <area>, <img>, <input>
      */
    val alt = AttrDef[String](ns, "alt", identity)

    /**
      * Indicates that the script should be executed asynchronously.
      * Applicable to <script>
      */
    val async = AttrDef[String](ns, "async", identity)

    /**
      * Controls whether and how text input is automatically capitalized as it is entered/edited by the user.
      */
    val autocapitalize = AttrDef[String](ns, "autocapitalize", identity)

    /**
      * Indicates whether controls in this form can by default have their values automatically completed by the browser.
      * Applicable to <form>, <input>, <select>, <textarea>
      */
    val autocomplete = AttrDef[String](ns, "autocomplete", identity)

    /**
      * The element should be automatically focused after the page loaded.
      * Applicable to <button>, <input>, <keygen>, <select>, <textarea>
      */
    val autofocus = AttrDef[String](ns, "autofocus", identity)

    /**
      * The audio or video should play as soon as possible.
      * Applicable to <audio>, <video>
      */
    val autoplay = AttrDef[String](ns, "autoplay", identity)

    /**
      * Specifies the URL of an image file.
      * Note: Although browsers and email clients may still support this attribute, it is obsolete. Use CSS background-image instead.
      * Applicable to <body>, <table>, <td>, <th>
      */
    val background = AttrDef[String](ns, "background", identity)

    /**
      * Contains the time range of already buffered media.
      * Applicable to <audio>, <video>
      */
    val buffered = AttrDef[String](ns, "buffered", identity)

    /**
      * A challenge string that is submitted along with the public key.
      * Applicable to <keygen>
      */
    val challenge = AttrDef[String](ns, "challenge", identity)

    /**
      * Declares the character encoding of the page or script.
      * Applicable to <meta>, <script>
      */
    val charset = AttrDef[String](ns, "charset", identity)

    /**
      * Indicates whether the element should be checked on page load.
      * Applicable to <command>, <input>
      */
    val checked = AttrDef[String](ns, "checked", identity)

    /**
      * Contains a URI which points to the source of the quote or change.
      * Applicable to <blockquote>, <del>, <ins>, <q>
      */
    val cite = AttrDef[String](ns, "cite", identity)

    /**
      * Often used with CSS to style elements with common properties.
      */
    val `class` = AttrDef[String](ns, "class", identity)

    /**
      * Often used with CSS to style elements with common properties.
      */
    val clazz = AttrDef[String](ns, "class", identity)

    /**
      * This attribute gives the absolute or relative URL of the directory where applets' .class files referenced by the code attribute are stored.
      * Applicable to <applet>
      */
    val codebase = AttrDef[String](ns, "codebase", identity)

    /**
      * Defines the number of columns in a textarea.
      * Applicable to <textarea>
      */
    val cols = AttrDef[String](ns, "cols", identity)

    /**
      * The colspan attribute defines the number of columns a cell should span.
      * Applicable to <td>, <th>
      */
    val colspan = AttrDef[String](ns, "colspan", identity)

    /**
      * A value associated with http-equiv or name depending on the context.
      * Applicable to <meta>
      */
    val content = AttrDef[String](ns, "content", identity)

    /**
      * Indicates whether the element's content is editable.
      */
    val contenteditable = AttrDef[String](ns, "contenteditable", identity)

    /**
      * Defines the ID of a <menu> element which will serve as the element's context menu.
      * Applicable to Global attribute
      */
    val contextmenu = AttrDef[String](ns, "contextmenu", identity)

    /**
      * Indicates whether the browser should show playback controls to the user.
      * Applicable to <audio>, <video>
      */
    val controls = AttrDef[String](ns, "controls", identity)

    /**
      * A set of values specifying the coordinates of the hot-spot region.
      * Applicable to <area>
      */
    val coords = AttrDef[String](ns, "coords", identity)

    /**
      * How the element handles cross-origin requests
      * Applicable to <audio>, <img>, <link>, <script>, <video>
      */
    val crossorigin = AttrDef[String](ns, "crossorigin", identity)

    /**
      * Specifies the Content Security Policy that an embedded document must agree to enforce upon itself.
      * Applicable to <iframe>
      */
    val csp = AttrDef[String](ns, "csp", identity)

    /**
      * Indicates the date and time associated with the element.
      * Applicable to <del>, <ins>, <time>
      */
    val datetime = AttrDef[String](ns, "datetime", identity)

    /**
      * Indicates the preferred method to decode the image.
      * Applicable to <img>
      */
    val decoding = AttrDef[String](ns, "decoding", identity)

    /**
      * Indicates that the track should be enabled unless the user's preferences indicate something different.
      * Applicable to <track>
      */
    val default = AttrDef[String](ns, "default", identity)

    /**
      * Indicates that the script should be executed after the page has been parsed.
      * Applicable to <script>
      */
    val defer = AttrDef[String](ns, "defer", identity)

    /**
      * Defines the text direction. Allowed values are ltr (Left-To-Right) or rtl (Right-To-Left)
      * Applicable to Global attribute
      */
    val dir = AttrDef[String](ns, "dir", identity)

    /**
      * Applicable to <input>, <textarea>
      */
    val dirname = AttrDef[String](ns, "dirname", identity)

    /**
      * Indicates whether the user can interact with the element.
      * Applicable to <button>, <command>, <fieldset>, <input>, <keygen>, <optgroup>, <option>, <select>, <textarea>
      */
    val disabled = AttrDef[String](ns, "disabled", identity)

    /**
      * Indicates that the hyperlink is to be used for downloading a resource.
      * Applicable to <a>, <area>
      */
    val download = AttrDef[String](ns, "download", identity)

    /**
      * Defines whether the element can be dragged.
      * Applicable to Global attribute
      */
    val draggable = AttrDef[String](ns, "draggable", identity) // TODO bool

    /**
      * Indicates that the element accept the dropping of content on it.
      * Applicable to Global attribute
      */
    val dropzone = AttrDef[String](ns, "dropzone", identity)

    /**
      * Defines the content type of the form date when the method is POST.
      * Applicable to <form>
      */
    val enctype = AttrDef[String](ns, "enctype", identity)

    /**
      * The enterkeyhint specifies what action label (or icon) to present for the enter key on virtual keyboards. The attribute can be used with form controls (such as the value of textarea elements), or in elements in an editing host (e.g., using contenteditable attribute).
      * Applicable to <textarea>, contenteditable
      */
    val enterkeyhint = AttrDef[String](ns, "enterkeyhint", identity)

    /**
      * Describes elements which belongs to this one.
      * Applicable to <label>, <output>
      */
    val `for` = AttrDef[String](ns, "for", identity)

    /**
      * Indicates the form that is the owner of the element.
      * Applicable to <button>, <fieldset>, <input>, <keygen>, <label>, <meter>, <object>, <output>, <progress>, <select>, <textarea>
      */
    val form = AttrDef[String](ns, "form", identity)

    /**
      * Indicates the action of the element, overriding the action defined in the <form>.
      * Applicable to <input>, <button>
      */
    val formaction = AttrDef[String](ns, "formaction", identity)

    /**
      * If the button/input is a submit button (type="submit"), this attribute sets the encoding type to use during form submission. If this attribute is specified, it overrides the enctype attribute of the button's form owner.
      * Applicable to <button>, <input>
      */
    val formenctype = AttrDef[String](ns, "formenctype", identity)

    /**
      * If the button/input is a submit button (type="submit"), this attribute sets the submission method to use during form submission (GET, POST, etc.). If this attribute is specified, it overrides the method attribute of the button's form owner.
      * Applicable to <button>, <input>
      */
    val formmethod = AttrDef[String](ns, "formmethod", identity)

    /**
      * If the button/input is a submit button (type="submit"), this boolean attribute specifies that the form is not to be validated when it is submitted. If this attribute is specified, it overrides the novalidate attribute of the button's form owner.
      * Applicable to <button>, <input>
      */
    val formnovalidate = AttrDef[String](ns, "formnovalidate", identity)

    /**
      * If the button/input is a submit button (type="submit"), this attribute specifies the browsing context (for example, tab, window, or inline frame) in which to display the response that is received after submitting the form. If this attribute is specified, it overrides the target attribute of the button's form owner.
      * Applicable to <button>, <input>
      */
    val formtarget = AttrDef[String](ns, "formtarget", identity)

    /**
      * IDs of the <th> elements which applies to this element.
      * Applicable to <td>, <th>
      */
    val headers = AttrDef[String](ns, "headers", identity)

    /**
      * Prevents rendering of given element, while keeping child elements, e.g. script elements, active.
      */
    val hidden = AttrDef[String](ns, "hidden", identity) // TODO boolean

    /**
      * Indicates the lower bound of the upper range.
      * Applicable to <meter>
      */
    val high = AttrDef[String](ns, "high", identity)

    /**
      * The URL of a linked resource.
      * Applicable to <a>, <area>, <base>, <link>
      */
    val href = AttrDef[String](ns, "href", identity)

    /**
      * Specifies the language of the linked resource.
      * Applicable to <a>, <area>, <link>
      */
    val hreflang = AttrDef[String](ns, "hreflang", identity)

    /**
      * Defines a pragma directive.
      * Applicable to <meta>
      */
    val `http-equiv` = AttrDef[String](ns, "http-equiv", identity)

    /**
      * Specifies a picture which represents the command.
      * Applicable to <command>
      */
    val icon = AttrDef[String](ns, "icon", identity)

    /**
      * Often used with CSS to style a specific element. The value of this attribute must be unique.
      * Applicable to Global attribute
      */
    val id = AttrDef[String](ns, "id", identity)

    /**
      * Indicates the relative fetch priority for the resource.
      * Applicable to <iframe>, <img>, <link>, <script>
      */
    val importance = AttrDef[String](ns, attrName = "importance", identity)

    /**
      * Specifies a Subresource Integrity value that allows browsers to verify what they fetch.
      * Applicable to <link>, <script>
      */
    val integrity = AttrDef[String](ns, "integrity", identity)


    /**
      * This attribute tells the browser to ignore the actual intrinsic size of the image and pretend it’s the size specified in the attribute.
      * Applicable to <img>
      */
    val intrinsicsize = AttrDef[String](ns, attrName = "intrinsicsize", identity)

    /**
      * Provides a hint as to the type of data that might be entered by the user while editing the element or its contents. The attribute can be used with form controls (such as the value of textarea elements), or in elements in an editing host (e.g., using contenteditable attribute).
      * Applicable to <textarea>, contenteditable
      */
    val inputmode = AttrDef[String](ns, "inputmode", identity)

    /**
      * Indicates that the image is part of a server-side image map.
      * Applicable to <img>
      */
    val ismap = AttrDef[String](ns, "ismap", identity)

    /**
      * attribute
      * Applicable to Global
      */
    val itemprop = AttrDef[String](ns, "itemprop", identity)

    /**
      * Specifies the type of key generated.
      * Applicable to <keygen>
      */
    val keytype = AttrDef[String](ns, "keytype", identity)

    /**
      * Specifies the kind of text track.
      * Applicable to <track>
      */
    val kind = AttrDef[String](ns, "kind", identity)

    /**
      * Specifies a user-readable title of the element.
      * Applicable to <optgroup>, <option>, <track>
      */
    val label = AttrDef[String](ns, "label", identity)

    /**
      * Defines the language used in the element.
      */
    val lang = AttrDef[String](ns, "lang", identity)

    /**
      * Defines the script language used in the element.
      * Applicable to <script>
      */
    val language = AttrDef[String](ns, "language", identity)

    /**
      * Identifies a list of pre-defined options to suggest to the user.
      * Applicable to <input>
      */
    val list = AttrDef[String](ns, "list", identity)

    /**
      * Indicates whether the media should start playing from the start when it's finished.
      * Applicable to <audio>, <bgsound>, <marquee>, <video>
      */
    val loop = AttrDef[String](ns, "loop", identity)

    /**
      * Indicates the upper bound of the lower range.
      * Applicable to <meter>
      */
    val low = AttrDef[String](ns, "low", identity)

    /**
      * Indicates the maximum value allowed.
      * Applicable to <input>, <meter>, <progress>
      */
    val max = AttrDef[String](ns, "max", identity)

    /**
      * Defines the maximum number of characters allowed in the element.
      * Applicable to <input>, <textarea>
      */
    val maxlength = AttrDef[String](ns, "maxlength", identity)

    /**
      * Defines the minimum number of characters allowed in the element.
      * Applicable to <input>, <textarea>
      */
    val minlength = AttrDef[String](ns, "minlength", identity)

    /**
      * Specifies a hint of the media for which the linked resource was designed.
      * Applicable to <a>, <area>, <link>, <source>, <style>
      */
    val media = AttrDef[String](ns, "media", identity)

    /**
      * Defines which HTTP method to use when submitting the form. Can be GET (default) or POST.
      * Applicable to <form>
      */
    val method = AttrDef[String](ns, "method", identity)

    /**
      * Indicates the minimum value allowed.
      * Applicable to <input>, <meter>
      */
    val min = AttrDef[String](ns, "min", identity)

    /**
      * Indicates whether multiple values can be entered in an input of the type email or file.
      * Applicable to <input>, <select>
      */
    val multiple = AttrDef[String](ns, "multiple", identity)

    /**
      * Indicates whether the audio will be initially silenced on page load.
      * Applicable to <audio>, <video>
      */
    val muted = AttrDef[String](ns, "muted", identity)

    /**
      * Name of the element. For example used by the server to identify the fields in form submits.
      * Applicable to <button>, <form>, <fieldset>, <iframe>, <input>, <keygen>, <object>, <output>, <select>, <textarea>, <map>, <meta>, <param>
      */
    val name = AttrDef[String](ns, "name", identity)

    /**
      * This attribute indicates that the form shouldn't be validated when submitted.
      * Applicable to <form>
      */
    val novalidate = AttrDef[String](ns, "novalidate", identity)

    /**
      * Indicates whether the details will be shown on page load.
      * Applicable to <details>
      */
    val open = AttrDef[String](ns, "open", identity)

    /**
      * Indicates the optimal numeric value.
      * Applicable to <meter>
      */
    val optimum = AttrDef[String](ns, "optimum", identity)

    /**
      * Defines a regular expression which the element's value will be validated against.
      * Applicable to <input>
      */
    val pattern = AttrDef[String](ns, "pattern", identity)

    /**
      * The ping attribute specifies a space-separated list of URLs to be notified if a user follows the hyperlink.
      * Applicable to <a>, <area>
      */
    val ping = AttrDef[String](ns, "ping", identity)

    /**
      * Provides a hint to the user of what can be entered in the field.
      * Applicable to <input>, <textarea>
      */
    val placeholder = AttrDef[String](ns, "placeholder", identity)

    /**
      * A URL indicating a poster frame to show until the user plays or seeks.
      * Applicable to <video>
      */
    val poster = AttrDef[String](ns, "poster", identity)

    /**
      * Indicates whether the whole resource, parts of it or nothing should be preloaded.
      * Applicable to <audio>, <video>
      */
    val preload = AttrDef[String](ns, "preload", identity)

    /**
      * Applicable to <command>
      */
    val radiogroup = AttrDef[String](ns, "radiogroup", identity)

    /**
      * Indicates whether the element can be edited.
      * Applicable to <input>, <textarea>
      */
    val readonly = AttrDef[String](ns, "readonly", identity)

    /**
      * Specifies which referrer is sent when fetching the resource.
      * Applicable to <a>, <area>, <iframe>, <img>, <link>, <script>
      */
    val referrerpolicy = AttrDef[String](ns, "referrerpolicy", identity)

    /**
      * Specifies the relationship of the target object to the link object.
      * Applicable to <a>, <area>, <link>
      */
    val rel = AttrDef[String](ns, "rel", identity)

    /**
      * Indicates whether this element is required to fill out or not.
      * Applicable to <input>, <select>, <textarea>
      */
    val required = AttrDef[String](ns, "required", identity)

    /**
      * Indicates whether the list should be displayed in a descending order instead of a ascending.
      * Applicable to <ol>
      */
    val reversed = AttrDef[String](ns, "reversed", identity)

    /**
      * Defines the number of rows in a text area.
      * Applicable to <textarea>
      */
    val rows = AttrDef[String](ns, "rows", identity)

    /**
      * Defines the number of rows a table cell should span over.
      * Applicable to <td>, <th>
      */
    val rowspan = AttrDef[String](ns, "rowspan", identity)

    /**
      * Stops a document loaded in an iframe from using certain features (such as submitting forms or opening new windows).
      * Applicable to <iframe>
      */
    val sandbox = AttrDef[String](ns, "sandbox", identity)

    /**
      * Defines the cells that the header test (defined in the th element) relates to.
      * Applicable to <th>
      */
    val scope = AttrDef[String](ns, "scope", identity)

    /**
      * Applicable to <style>
      */
    val scoped = AttrDef[String](ns, "scoped", identity)

    /**
      * Defines a value which will be selected on page load.
      * Applicable to <option>
      */
    val selected = AttrDef[String](ns, "selected", identity)

    /**
      * Applicable to <a>, <area>
      */
    val shape = AttrDef[String](ns, "shape", identity)

    /**
      * Defines the width of the element (in pixels). If the element's type attribute is text or password then it's the number of characters.
      * Applicable to <input>, <select>
      */
    val size = AttrDef[String](ns, "size", identity)

    /**
      * Applicable to <link>, <img>, <source>
      */
    val sizes = AttrDef[String](ns, "sizes", identity)

    /**
      * Assigns a slot in a shadow DOM shadow tree to an element.
      * Applicable to Global attribute
      */
    val slot = AttrDef[String](ns, "slot", identity)

    /**
      * <colgroup>
      * Applicable to <col>,
      */
    val span = AttrDef[String](ns, "span", identity)

    /**
      * Indicates whether spell checking is allowed for the element.
      * Applicable to Global attribute
      */
    val spellcheck = AttrDef[String](ns, "spellcheck", identity)

    /**
      * The URL of the embeddable content.
      * Applicable to <audio>, <embed>, <iframe>, <img>, <input>, <script>, <source>, <track>, <video>
      */
    val src = AttrDef[String](ns, "src", identity)

    /**
      * Applicable to <iframe>
      */
    val srcdoc = AttrDef[String](ns, "srcdoc", identity)

    /**
      * Applicable to <track>
      */
    val srclang = AttrDef[String](ns, "srclang", identity)

    /**
      * One or more responsive image candidates.
      * Applicable to <img>, <source>
      */
    val srcset = AttrDef[String](ns, "srcset", identity)

    /**
      * Defines the first number if other than 1.
      * Applicable to <ol>
      */
    val start = AttrDef[String](ns, "start", identity)

    /**
      * Applicable to <input>
      */
    val step = AttrDef[String](ns, "step", identity)

    /**
      * Defines CSS styles which will override styles previously set.
      */
    val style = AttrDef[String](ns, "style", identity)

    /**
      * Applicable to	<table>
      */
    val summary = AttrDef[String](ns, "summary", identity)

    /**
      * Overrides the browser's default tab order and follows the one specified instead.
      * Applicable to Global attribute
      */
    val tabindex = AttrDef[String](ns, "tabindex", identity)

    /**
      * Applicable to <a>, <area>, <base>, <form>
      */
    val target = AttrDef[String](ns, "target", identity)

    /**
      * Text to be displayed in a tooltip when hovering over the element.
      * Applicable to Global attribute
      */
    val title = AttrDef[String](ns, "title", identity)

    /**
      * Specify whether an element ’s attribute values and the values of its Text node children are to be translated when the page is localized, or whether to leave them unchanged.
      * Applicable to Global attribute
      */
    val translate = AttrDef[String](ns, "translate", identity)

    /**
      * Defines the type of the element.
      * Applicable to <button>, <input>, <command>, <embed>, <object>, <script>, <source>, <style>, <menu>
      */
    val `type` = AttrDef[String](ns, "type", identity)

    /**
      * <object>
      * Applicable to <img>, <input>,
      */
    val usemap = AttrDef[String](ns, "usemap", identity)

    /**
      * Defines a default value which will be displayed in the element on page load.
      * Applicable to <button>, <data>, <input>, <li>, <meter>, <option>, <progress>, <param>
      */
    val value = AttrDef[String](ns, "value", identity)

    /**
      * Indicates whether the text should be wrapped.
      * Applicable to <textarea>
      */
    val wrap = AttrDef[String](ns, "wrap", identity)
  }

  /**
    * Common CSS Properties Reference
    * https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Properties_Reference
    */
  object styles {

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background
      */
    val background = StyleDef[String]("background", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-attachment
      */
    val backgroundAttachment = StyleDef[String]("background-attachment", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-color
      */
    val backgroundColor = StyleDef[String]("background-color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-image
      */
    val backgroundImage = StyleDef[String]("background-image", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-position
      */
    val backgroundPosition = StyleDef[String]("background-position", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/background-repeat
      */
    val backgroundRepeat = StyleDef[String]("background-repeat", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border
      */
    val border = StyleDef[String]("border", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom
      */
    val borderBottom = StyleDef[String]("border-bottom", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom-color
      */
    val borderBottomColor = StyleDef[String]("border-bottom-color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom-style
      */
    val borderBottomStyle = StyleDef[String]("border-bottom-style", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-bottom-width
      */
    val borderBottomWidth = StyleDef[String]("border-bottom-width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-color
      */
    val borderColor = StyleDef[String]("border-color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left
      */
    val borderLeft = StyleDef[String]("border-left", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left-color
      */
    val borderLeftColor = StyleDef[String]("border-left-color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left-style
      */
    val borderLeftStyle = StyleDef[String]("border-left-style", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-left-width
      */
    val borderLeftWidth = StyleDef[String]("border-left-width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right
      */
    val borderRight = StyleDef[String]("border-right", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right-color
      */
    val borderRightColor = StyleDef[String]("border-right-color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right-style
      */
    val borderRightStyle = StyleDef[String]("border-right-style", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-right-width
      */
    val borderRightWidth = StyleDef[String]("border-right-width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-style
      */
    val borderStyle = StyleDef[String]("border-style", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top
      */
    val borderTop = StyleDef[String]("border-top", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top-color
      */
    val borderTopColor = StyleDef[String]("border-top-color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top-style
      */
    val borderTopStyle = StyleDef[String]("border-top-style", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-top-width
      */
    val borderTopWidth = StyleDef[String]("border-top-width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/border-width
      */
    val borderWidth = StyleDef[String]("border-width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/clear
      */
    val clear = StyleDef[String]("clear", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/clip
      */
    val clip = StyleDef[String]("clip", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/color
      */
    val color = StyleDef[String]("color", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/cursor
      */
    val cursor = StyleDef[String]("cursor", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/display
      */
    val display = StyleDef[String]("display", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/filter
      */
    val filter = StyleDef[String]("filter", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/float
      */
    val cssFloat = StyleDef[String]("float", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font
      */
    val font = StyleDef[String]("font", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-family
      */
    val fontFamily = StyleDef[String]("font-family", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-size
      */
    val fontSize = StyleDef[String]("font-size", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-variant
      */
    val fontVariant = StyleDef[String]("font-variant", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/font-weight
      */
    val fontWeight = StyleDef[String]("font-weight", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/height
      */
    val height = StyleDef[String]("height", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/left
      */
    val left = StyleDef[String]("left", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/letter-spacing
      */
    val letterSpacing = StyleDef[String]("letter-spacing", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/line-height
      */
    val lineHeight = StyleDef[String]("line-height", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style
      */
    val listStyle = StyleDef[String]("list-style", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-image
      */
    val listStyleImage = StyleDef[String]("list-style-image", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-position
      */
    val listStylePosition = StyleDef[String]("list-style-position", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-type
      */
    val listStyleType = StyleDef[String]("list-style-type", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin
      */
    val margin = StyleDef[String]("margin", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-bottom
      */
    val marginBottom = StyleDef[String]("margin-bottom", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-left
      */
    val marginLeft = StyleDef[String]("margin-left", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-right
      */
    val marginRight = StyleDef[String]("margin-right", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/margin-top
      */
    val marginTop = StyleDef[String]("margin-top", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/overflow
      */
    val overflow = StyleDef[String]("overflow", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding
      */
    val padding = StyleDef[String]("padding", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-bottom
      */
    val paddingBottom = StyleDef[String]("padding-bottom", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-left
      */
    val paddingLeft = StyleDef[String]("padding-left", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-right
      */
    val paddingRight = StyleDef[String]("padding-right", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/padding-top
      */
    val paddingTop = StyleDef[String]("padding-top", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/page-break-after
      */
    val pageBreakAfter = StyleDef[String]("page-break-after", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/page-break-before
      */
    val pageBreakBefore = StyleDef[String]("page-break-before", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/position
      */
    val position = StyleDef[String]("position", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/stroke-dasharray
      */
    val strokeDasharray = StyleDef[String]("stroke-dasharray", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/stroke-dashoffset
      */
    val strokeDashoffset = StyleDef[String]("stroke-dashoffset", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/stroke-width
      */
    val strokeWidth = StyleDef[String]("stroke-width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-align
      */
    val textAlign = StyleDef[String]("text-align", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-decoration
      */
    val textDecoration = StyleDef[String]("text-decoration", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-indent
      */
    val textIndent = StyleDef[String]("text-indent", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/text-transform
      */
    val textTransform = StyleDef[String]("text-transform", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/top
      */
    val top = StyleDef[String]("top", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/vertical-align
      */
    val verticalAlign = StyleDef[String]("vertical-align", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/visibility
      */
    val visibility = StyleDef[String]("visibility", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/width
      */
    val width = StyleDef[String]("width", identity)

    /**
      * @see https://developer.mozilla.org/en-US/docs/Web/CSS/z-index
      */
    val zIndex = StyleDef[String]("z-index", identity)
  }

}
