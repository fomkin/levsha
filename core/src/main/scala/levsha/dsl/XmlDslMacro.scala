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

import fastparse.{all, core}
import macrocompat.bundle

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

@bundle class XmlDslMacro(val c: blackbox.Context) extends OptimizerMacro {

  import XmlDslMacro.{Position, _}
  import c.universe._
  import fastparse.all.Parsed

  final val Namespaces: Map[String, c.Tree] = Map(
    "xlink" -> q"levsha.XmlNs.xlink",
    "svg" -> q"levsha.XmlNs.svg",
    "html" -> q"levsha.XmlNs.svg",
    "mathml" -> q"levsha.XmlNs.mathml"
  )

  class Compiler[M: c.WeakTypeTag](templateTree: c.Tree, args: Seq[c.Tree]) {

    private val MT = weakTypeOf[M]
    private val xmlPos = templateTree.pos
    private val includes = args.toVector
    private val includesIndex = includes
      .zipWithIndex
      .map { case (tree, i) => show(tree) -> i}
      .toMap
    private val stringParts = {
      val Apply(_, List(Apply(_, rawStringParts))) = templateTree
      rawStringParts.map { case Literal(Constant(s: String)) => s }
    }

    val template: String = stringParts
      .zipWithIndex
      .foldLeft("") {
        case (_, (x, 0)) => x
        case (acc, (x, i)) =>
          val j = i - 1
          val include = includes(j)
          val s = includesIndex(show(include)).toString
          val d = include.pos.end - include.pos.start - s.length
          s"$acc@{$s${" " * d}}$x"
      }

    def relativePos(p: Position) = xmlPos
      .withEnd(xmlPos.start + p.end)
      .withPoint(xmlPos.start + p.start)
      .withStart(xmlPos.start + p.start)

    def resolveKey(k: String): c.Tree = {
      includes(k.trim.toInt)
    }

    def resolveNs(ns: Option[Value]) = ns match {
      case Some(Value.Composite(pos, _)) =>
        c.abort(relativePos(pos), s"Composite namespaces are not supported")
      case Some(Value.Include(pos, key)) =>
        resolveKey(key) match {
          case x if x.tpe =:= typeOf[levsha.XmlNs] => x
          case extractConverter("xmlNsToNode", x) if x.tpe =:= typeOf[levsha.XmlNs] => x
          case x => c.abort(relativePos(pos), s"XmlNs expected but ${x.tpe} found")
        }
      case Some(Value.Literal(pos, value)) =>
        Namespaces.getOrElse(value, c.abort(relativePos(pos), s"invalid xmlns: '$value'"))
      case None => q"levsha.XmlNs.html" // is default namespace
    }

    def resolveString(v: Value): Tree = v match {
      case Value.Literal(_, s) => q"$s"
      case Value.Include(pos, key) =>
        resolveKey(key) match {
          case x if x.tpe =:= typeOf[String] => x
          case extractConverter("stringToNode", x) if x.tpe =:= typeOf[String] => x
          case x => c.abort(relativePos(pos), s"String expected but ${x.tpe} found")
        }
      case Value.Composite(_, xs) =>
        xs.map(resolveString).reduce((a, b) => q"$a + $b")
    }

    def resolveAttr(attr: Attribute): Tree = attr match {
      case Attribute.Literal(_, QName(_, ns, name), value) =>
        val rns = resolveNs(ns)
        val n = resolveString(name)
        val v = resolveString(value)
        q"levsha.Document.Attr.apply[$MT](rc => rc.setAttr($rns,$n,$v))"
      case Attribute.Include(pos, key) =>
        val x = resolveKey(key)
        if (x.tpe =:= weakTypeOf[levsha.Document.Attr[M]]) x
        else c.abort(relativePos(pos), s"Attribute expected but ${x.tpe} found")
    }

    def resolveXml(xml: Xml): Tree = xml match {
      case Xml.CData(_, s) => q"levsha.Document.Node.apply[$MT] { rc => rc.addTextNode($s) }"
      case Xml.Text(_, s) => q"levsha.Document.Node.apply[$MT]{ rc => rc.addTextNode($s) }"
      case Xml.Comment(_, _) => EmptyTree
      case Xml.Include(pos, key) =>
        val x = resolveKey(key)
        if (x.tpe =:= weakTypeOf[levsha.Document.Node[M]]) x
        else c.abort(relativePos(pos), s"Node expected but ${x.tpe} found")
      case Xml.Node(_, QName(_, ns, name), attrs, children) =>
        val rns = resolveNs(ns)
        val n = resolveString(name)
        val ops = (attrs.map(resolveAttr) ++ children.map(resolveXml))
          .filter(_.nonEmpty)
          .map(c.untypecheck _ andThen optimize)
        q"""
          levsha.Document.Node.apply[$MT] { rc =>
            rc.openNode($rns, $n)
            ..$ops
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
    def pos: Position
    def mkString: String = this match {
      case Value.Literal(_, value) => value
      case Value.Include(_, value) => s"@{$value}"
      case Value.Composite(_, xs) => xs.map(_.mkString).mkString
    }
  }

  object Value {
    case class Literal(pos: Position, value: String) extends Value
    case class Include(pos: Position, key: String) extends Value
    case class Composite(pos: Position, xs: Seq[Value]) extends Value
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

    val `<`: Parser[Unit] = P("<")

    val `@{`: Parser[Unit] = P("@{")

    val `"`: Parser[Unit] = P("\"")

    val space: Parser[Unit] = P(CharIn("\t\r\n ").rep)

    val `${...}`: Parser[(Position, String)] = P(Index ~ `@{` ~ (!"}" ~ AnyChar).rep(min=1).! ~ "}" ~ Index).map(pos)

    def value(chars: Parser[Unit]): Parser[Value] = {
      val literal = P(Index ~ (!`@{` ~ chars).rep(min = 1).! ~ Index).map(pos)
      P(`${...}`.map(Value.Include.tupled) | literal.map(Value.Literal.tupled))
        .rep(min = 1)
        .map(_.toList)
        .map {
          case (x: Value.Literal) :: Nil => x
          case (x: Value.Include) :: Nil => x
          case xs if xs.forall(_.isInstanceOf[Value.Literal]) =>
            Value.Literal(Position(xs.head.pos.start, xs.last.pos.end), xs.mkString)
          case xs =>
            Value.Composite(Position(xs.head.pos.start, xs.last.pos.end), xs)
        }
        
    }

    val identifier: P[Value] = value(CharIn('a' to 'z', 'A' to 'Z', '0' to '9', "-_"))

    val `qualified name`: Parser[QName] = P(Index ~ ((identifier ~ ":").? ~ identifier) ~ Index)
      .map(pos)
      .map {
        case (p, (ns, n)) =>
          QName(p, ns, n)
      }

    val attribute: Parser[Attribute] = P {

      val `attribute value` = P(
        `${...}`.map(Value.Include.tupled)
          | (`"` ~ value(!`"` ~ AnyChar) ~ `"`)
      )

      val literal = P(Index ~ (`qualified name` ~ "=" ~ `attribute value`) ~ Index)
        .map(pos)
        .map {
          case (p, (qn, value)) =>
            Attribute.Literal(p, qn, value)
        }

      literal | `${...}`.map(Attribute.Include.tupled)
    }

    val `node include`: Parser[Xml.Include] = P(`${...}`.map(Xml.Include.tupled))

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

      val leftTag = P(`<` ~ `qualified name` ~ (space ~ attribute ~ space).rep ~ ">" )

      def rightTag(name: QName): Parser[Unit]=
        P( "</>" | "</" ~ name.mkString ~ ">" )

      for {
        (s, (name, attrs)) <- Index ~ leftTag
        (children, e) <- (!"</" ~ `node child`).rep ~ rightTag(name) ~ Index
      } yield {
        Xml.Node(Position(s, e), name, attrs, children)
      }
    }

    lazy val `node child`: Parser[Xml] = P(`node include` | comment | cdata | node | text)
  }

}
