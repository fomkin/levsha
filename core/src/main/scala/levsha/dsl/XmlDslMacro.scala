package levsha.dsl

import fastparse.{all, core}
import macrocompat.bundle

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

@bundle class XmlDslMacro(val c: blackbox.Context) {

  import c.universe._
  import XmlDslMacro.{Position, _}

  import fastparse.all.Parsed

  final val Namespaces: Map[String, c.Tree] = Map(
    "svg" -> q"levsha.XmlNs.svg",
    "html" -> q"levsha.XmlNs.svg",
    "mathml" -> q"levsha.XmlNs.mathml"
  )

  class Compiler[M: c.WeakTypeTag](templateTree: c.Tree, args: Seq[c.Tree]) {

    val mt = weakTypeOf[M]
    val xmlPos = templateTree.pos
    val includes = args.toVector
      .map(x => show(x) -> x)
      .toMap
    val stringParts = {
      val Apply(_, List(Apply(_, rawStringParts))) = templateTree
      rawStringParts.map { case Literal(Constant(s: String)) => s }
    }

    val template = stringParts
      .zipWithIndex
      .foldLeft("") {
        case (_, (x, 0)) => x
        case (acc, (x, i)) =>
          val j = i - 1
          args(j) match {
            case Literal(Constant(z: String)) => s"$acc$z$x"
            case include =>
              val s = show(include)
              val d = include.pos.end - include.pos.start - s.length
              s"$acc@{$s${" " * d}}$x"
          }
      }

    def relativePos(p: Position) = xmlPos
      .withEnd(xmlPos.start + p.end)
      .withPoint(xmlPos.start + p.start)
      .withStart(xmlPos.start + p.start)

    def resolveKey(k: String) = {
      val p = c.typecheck(c.untypecheck(includes(k.trim)))
      println(show(p))
      p
    }

    def resolveNs(ns: Option[Value]) = ns match {
      case Some(Value.Include(pos, key)) =>
        val x = resolveKey(key)
        if (x.tpe =:= typeOf[levsha.XmlNs]) x
        else c.abort(relativePos(pos), s"XmlNs expected but ${x.tpe} found")
      case Some(Value.Literal(pos, value)) =>
        Namespaces.getOrElse(value, c.abort(relativePos(pos), s"invalid xmlns: '$value'"))
      case None => q"levsha.XmlNs.html" // is default namespace
    }

    def resolveString(v: Value) = v match {
      case Value.Literal(_, s) => q"$s"
      case Value.Include(pos, key) =>
        val x = resolveKey(key)
        if (x.tpe =:= typeOf[String]) x
        else c.abort(relativePos(pos), s"String expected but ${x.tpe} found")
    }

    def resolveAttr(attr: Attribute) = attr match {
      case Attribute.Literal(_, QName(_, ns, name), value) =>
        val rns = resolveNs(ns)
        val n = resolveString(name)
        val v = resolveString(value)
        q"levsha.Document.Attr.apply[$mt]{rc=>rc.setAttr($rns,$n,$v)}"
      case Attribute.Include(pos, key) =>
        //          println(pos)
        val x = resolveKey(key)
        if (x.tpe =:= weakTypeOf[levsha.Document.Attr[M]]) x
        else c.abort(relativePos(pos), s"attribute expected but ${x.tpe} found")
    }

    def resolveXml(xml: Xml): Tree = xml match {
      case Xml.CData(_, s) => q"levsha.Document.Node.apply[$mt]{rc=>rc.addTextNode($s)}"
      case Xml.Text(_, s) => q"levsha.Document.Node.apply[$mt]{rc=>rc.addTextNode($s)}"
      case Xml.Comment(_, _) => q"levsha.Document.Node.apply[$mt]{rc=>()}"
      case Xml.Include(pos, key) =>
        val x = resolveKey(key)
        if (x.tpe =:= weakTypeOf[levsha.Document.Node[M]]) x
        else c.abort(relativePos(pos), s"node expected but ${x.tpe} found")
      case Xml.Node(_, QName(_, ns, name), attrs, children) =>
        val rns = resolveNs(ns)
        val n = resolveString(name)
        def applyRc(x: Tree) = q"$x.apply(rc)"
        q"""
            levsha.Document.Node.apply[$mt] { rc =>
              rc.openNode($rns, $n)
              ..${attrs.map(resolveAttr).map(applyRc)}
              ..${children.map(resolveXml).map(applyRc)}
              rc.closeNode($n)
            }
          """
    }
  }

  def xmlStringContextImpl[M: c.WeakTypeTag](args: c.Tree*): c.Tree = {
    val compiler = new Compiler[M](c.prefix.tree, args)
    xmlParser.node.parse(compiler.template) match {
      case Parsed.Success(value, _) => compiler.resolveXml(value)
      case Parsed.Failure(_, index, e) =>
        val tplPos = Position(index, index + 20)
        val pos = compiler.relativePos(tplPos)
        c.abort(pos, s"Syntax error: ${e.traced.expected} expected")
    }
  }

  def attrStringContextImpl[M: c.WeakTypeTag](args: c.Tree*): c.Tree = {
    val compiler = new Compiler[M](c.prefix.tree, args)
    xmlParser.attribute.parse(compiler.template) match {
      case Parsed.Success(value, _) => compiler.resolveAttr(value)
      case Parsed.Failure(_, index, e) =>
        val tplPos = Position(index, index + 20)
        val pos = compiler.relativePos(tplPos)
        c.abort(pos, s"Syntax error: ${e.traced.expected} expected")
    }
  }

}

object XmlDslMacro {

  case class Position(start: Int, end: Int)

  sealed trait Value {
    def mkString: String = this match {
      case Value.Literal(_, value) => value
      case Value.Include(_, value) => s"@{$value}"
    }
  }

  object Value {
    case class Literal(pos: Position, value: String) extends Value
    case class Include(pos: Position, key: String) extends Value
  }

  case class QName(pos: Position, namespace: Option[Value], name: Value) {
    def mkString: String = namespace match {
      case Some(ns) => s"${ns.mkString}:${name.mkString}"
      case None => name.mkString
    }
  }

  sealed trait Attribute

  object Attribute {
    case class Literal(pos: Position, qname: QName, value: Value) extends Attribute
    case class Include(pos: Position, key: String) extends Attribute
  }

  sealed trait Xml

  object Xml {
    case class Node(pos: Position, qname: QName, attributes: Seq[Attribute], children: Seq[Xml]) extends Xml
    case class CData(pos: Position, data: String) extends Xml
    case class Include(pos: Position, key: String) extends Xml
    case class Text(pos: Position, text: String) extends Xml
    case class Comment(pos: Position, text: String) extends Xml
  }

  import fastparse.all._

  object xmlParser {

    def pos[T]: ((Int, T, Int)) => (Position, T) = {
      case (start, value, end) => (Position(start, end), value)
    }

    val `<` = P("<")

    val `@{` = P("@{")

    val `"`: Parser[Unit] = P("\"")

    val space: Parser[Unit] = P(CharIn("\t\r\n ").rep)

    val number: Parser[Unit] = P(CharIn('0' to '9').rep)

    val include: Parser[(Position, String)] = P(Index ~ `@{` ~ (!"}" ~ AnyChar).rep(min=1).! ~ "}" ~ Index).map(pos)

    val identifier: P[Value] = {
      val `included indentifier` = include.map(Value.Include.tupled)
      val `literal` = (Index ~ CharIn('a' to 'z', 'A' to 'Z', '0' to '9', "-_").rep.! ~ Index)
        .map(pos)
        .map(Value.Literal.tupled)
      P(`included indentifier` | `literal`)
    }

    val `qualified name`: Parser[QName] = P(Index ~ ((identifier ~ ":").? ~ identifier) ~ Index)
      .map(pos)
      .map {
        case (p, (ns, n)) =>
          QName(p, ns, n)
      }

    val attribute: Parser[Attribute] = {

      val `attribute value` = P(
        include.map(Value.Include.tupled) |
          (Index ~ `"` ~ (!`"` ~ AnyChar).rep.! ~ `"` ~ Index)
            .map(pos)
            .map(Value.Literal.tupled)
      )

      val literal = P(Index ~ (`qualified name` ~ "=" ~ `attribute value`) ~ Index)
        .map(pos)
        .map {
          case (p, (qn, value)) =>
            Attribute.Literal(p, qn, value)
        }
      
      P(literal | include.map(Attribute.Include.tupled))
    }

    val `node include`: Parser[Xml.Include] = P(include.map(Xml.Include.tupled))

    val text: Parser[Xml.Text] = P(Index ~ (!`<` ~ !`@{` ~/ AnyChar).rep.! ~ Index)
      .map(pos)
      .map(Xml.Text.tupled)

    val cdata: Parser[Xml.CData] = P(Index ~ "<![CDATA[" ~/ (!"]]>" ~ AnyChar).rep.! ~ "]]>" ~ Index)
      .map(pos)
      .map(Xml.CData.tupled)

    val comment: Parser[Xml.Comment] = P(Index ~ "<!--" ~/ (!"-->" ~ AnyChar).rep.! ~ "-->" ~ Index)
      .map(pos)
      .map(Xml.Comment.tupled)

    lazy val node: Parser[Xml.Node] = P {

      val leftTag = P(`<` ~/ `qualified name` ~ (space ~ attribute ~ space).rep ~ ">" )

      def rightTag(name: QName): Parser[Unit]=
        P( "</>" | "</" ~/ name.mkString ~ ">" )

      for {
        (s, (name, attrs)) <- Index ~ leftTag
        (children, e) <- (space ~ !"</" ~ `node child` ~ space).rep ~ rightTag(name) ~ Index
      } yield {
        Xml.Node(Position(s, e), name, attrs, children)
      }
    }

    lazy val `node child`: Parser[Xml] = P(`node include` | comment | cdata | text | node)
  }

}
