package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class TemplateContext[MiscType] {

  sealed trait RenderUnit
  sealed trait NodeLike extends RenderUnit

  case object Node extends NodeLike
  case object Text extends NodeLike
  case object Misc extends NodeLike
  case object Empty extends NodeLike
  case object Attr extends RenderUnit

  trait RenderContext {
    def openNode(name: String): Unit
    def closeNode(): Node.type
    def setAttr(name: String, value: String): Attr.type
    def addTextNode(text: String): Text.type
    def addMisc(misc: MiscType): Misc.type
  }

  /** Generates HTML */
  class TextRenderContext extends RenderContext {

    private val builder = StringBuilder.newBuilder
    private var nodeName: String = _

    def openNode(name: String): Unit = {
      if (nodeName != null) builder.append('>')
      nodeName = name
      builder.append('<')
      builder.append(name)
      builder.append(' ')
    }

    def closeNode(): Node.type = {
      builder.append('<')
      builder.append('/')
      builder.append(nodeName)
      builder.append('>')
      nodeName = null
      Node
    }

    def setAttr(name: String, value: String): Attr.type = {
      builder.append('"')
      builder.append(name)
      builder.append('"')
      builder.append('=')
      builder.append('"')
      builder.append(value)
      builder.append('"')
      Attr
    }

    def addTextNode(text: String): Text.type = {
      if (nodeName != null) builder.append('>')
      builder.append(text)
      Text
    }

    def addMisc(misc: MiscType): Misc.type = Misc

    /** Creates string from buffer */
    def mkString: String = builder.mkString
  }

  /** This render context does nothing */
  class DummyRenderContext extends RenderContext {
    def openNode(name: String): Unit = {}
    def closeNode(): Node.type = Node
    def setAttr(name: String, value: String): Attr.type = Attr
    def addTextNode(text: String): Text.type = Text
    def addMisc(misc: MiscType): Misc.type = Misc
  }
}
