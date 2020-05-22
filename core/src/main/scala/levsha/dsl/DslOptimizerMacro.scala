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

import levsha.Document.{Attr, Node, Style}

import scala.reflect.macros.blackbox

final class DslOptimizerMacro(val c: blackbox.Context) {

  import c.universe._

  def optimize[T: WeakTypeTag](node: Tree): Tree = {

    val T = weakTypeTag[T]

    def optimizeSeq(seq: Tree, f: Function) = {
      val argName = f.vparams.head.name
      val body = aux(f.body)
      val cleanBody = clearIdents(List(argName.toString), body)
      q"""
        val $$iter = $seq.iterator
        while ($$iter.hasNext) {
          val $argName = $$iter.next()
          $cleanBody
        }
      """
    }

    def aux(tree: Tree): Tree = tree match {
      // Optimize tag open/close
      case q"$tagDef.apply[$_](..$children)" if tagDef.tpe <:< weakTypeOf[TagDef] =>
        val transformedChildren = children
          .sortBy {
            case x if x.tpe <:< weakTypeOf[Style[T]] => -2
            case x if x.tpe <:< weakTypeOf[Attr[T]] => -1
            case x if x.tpe <:< weakTypeOf[Node[T]] => 0
            case x =>
              if(unableToSortTagWarningsEnabled) {
                c.warning(
                  x.pos,
                  "Unable to sort tag content in compile time. Ensure you add attributes and styles first.")
              }
              0
          }
          .map(aux)
        q"""
            rc.openNode($tagDef.ns, $tagDef.name)
            ..$transformedChildren
            rc.closeNode($tagDef.name)
          """
      // Optimize attributes and styles
      case q"$styleDef.@=[$_]($styleValue)" =>
        q"rc.setStyle($styleDef.name, $styleValue)"
      case q"$attrDef.:=[$_]($attrValue)" =>
        q"rc.setAttr($attrDef.ns, $attrDef.name, $attrValue)"
//    case q"$attrDef.:=[$_]($attrValue)" if attrDef.tpe <:< typeOf[AttrDef[_]] =>
//      q"rc.setAttr($attrDef.ns, $attrDef.attrName, $attrDef.mkString($attrValue))"
      // Optimize converters
      case converter("stringToNode", value) => q"rc.addTextNode($value)"
      case converter("miscToNode", value) => q"rc.addMisc($value)"
      case converter("optionToNode" | "optionToAttr", q"($opt).map[$b](${f: Function})") =>
        val argName = f.vparams.head.name
        val body = aux(f.body)
        val cleanBody = clearIdents(List("rc", argName.toString), body)
        q"""
          val $$opt = $opt
          if ($$opt.nonEmpty) {
            val $argName = $$opt.get
            $cleanBody
          }
        """
      // scala 2.12
      case converter("seqToNode", q"(${seq: Tree}).map[$_, $_](${f: Function})($_)") => optimizeSeq(seq, f)
      // scala 2.13
      case converter("seqToNode", q"${seq: Tree}.map[$_](${f: Function})") => optimizeSeq(seq, f)
      // Optimize empty nodes
      case q"levsha.dsl.`package`.void" => q"()"
      case q"levsha.Document.Empty" => q"()"
      // Optimize control flow
      case q"if ($cond) ${lhs: Tree} else ${rhs: Tree}" =>
        q"if ($cond) ${aux(lhs)} else ${aux(rhs)}"
      case q"levsha.dsl.`package`.when[$_, $_]($cond)(${tree: Tree})" =>
        q"if ($cond) ${aux(tree)}"
      case q"$expr match { case ..$cases }" =>
        val optimizedCases = cases map {
          case cq"$p => ${b: Tree}" => cq"$p => ${aux(b)}"
          case cq"$p if $c => ${b: Tree}" => cq"$p if $c => ${aux(b)}"
        }
        q"$expr match { case ..$optimizedCases }"
      // Can't optimize
      case expr if expr.tpe <:< weakTypeOf[levsha.Document[T]] => q"$expr.apply(rc)"
      // Skip this code
      case _ => tree
    }
    c.untypecheck {
      q"""
        levsha.Document.Node[$T]{ rc =>
          ${aux(node)}
        }
      """
    }
  }

  /**
    * Remove context binding from idents.
    * It's necessary when modifying AST
    */
  def clearIdents(names: List[String], tree: Tree): c.Tree = {
    val cleaner = new Transformer {
      override def transform(tree: Tree): Tree = {
        tree match {
          case Ident(TermName(name)) if names.contains(name) => Ident(TermName(name))
          case _ => super.transform(tree)
        }
      }
    }
    cleaner.transform(tree)
  }

  object converter {
    def unapply(tree: Tree): Option[(String, Tree)] = tree match {
      case Apply(Select(_, TermName(fun)), value :: Nil) => Some((fun, value))
      case Apply(TypeApply(Select(_, TermName(fun)), _), value :: Nil) => Some((fun, value))
      case _ => None
    }
  }

  private val unableToSortTagWarningsEnabled = {
    val propName = "levsha.macros.unableToSortTagWarnings"
    sys.props.get(propName)
      .orElse(sys.env.get(propName))
      .fold(false)(x => if (x == "true") true else false)
  }
}
