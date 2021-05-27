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

package levsha.dsl

import levsha.Document
import levsha.RenderContext
import Document.{Attr, Node, Style, Empty}
import scala.quoted.*

object DslOptimizerMacro {

  def optimize[T: Type](node: Expr[Node[T]])(using Quotes): Expr[Node[T]] = {

    import quotes.reflect.*
    import util.*

    val t = Type.of[T]

    def aux(rc: Expr[RenderContext[T]], tree: Expr[Document[T]]): Expr[Any] = tree match {
      // Optimize tag open/close
      case '{(${tagDef}: TagDef).apply[T](${Varargs(children)}:_*)} =>
        val xs = children
          .sortBy {
            case x if x.isExprOf[Style[T]] => -2
            case x if x.isExprOf[Attr[T]] => -1
            case x if x.isExprOf[Node[T]] => 0
            case x => 0
          }
          .map(aux(rc, _))
          .toList
        val block = xs match {
          case Nil => '{}
          case x :: Nil => x
          case _ => Expr.block(xs.dropRight(1), xs.last)
        }
        '{
          $rc.openNode($tagDef.ns, $tagDef.name)
          $block
          $rc.closeNode($tagDef.name)
        }
      // Optimize attributes and styles
      case '{(${styleDef}: StyleDef).@=[T]($styleValue)} =>
        '{$rc.setStyle($styleDef.name, $styleValue)}
      case '{(${attrDef}: AttrDef).:=[T]($attrValue)} =>
        '{$rc.setAttr($attrDef.ns, $attrDef.name, $attrValue)}
      // Optimize converters
      case '{stringToNode($value)} => '{$rc.addTextNode($value)}
      case '{miscToNode[T]($value)} => '{$rc.addMisc($value)}
      // case '{optionToNode[T](($opt: Option[x]).map($f))} =>
      //   f.asTerm match {
      //     case Lambda((arg :: Nil, body)) => 
      //       body.
      //       report.info(s"yay $args $body")  
      //       // '{
      //       //   if ($opt.nonEmpty) {
      //       //     val $x = $opt.get
      //       //     ${aux(rc, ff)}
      //       //   }
      //       // }
      //       f
      //     case _ =>
      //       report.info(s"fuck ${f.show}")  
      //       f
      //   }
//       case converter("optionToNode" | "optionToAttr", q"($opt).map[$b](${f: Function})") =>
//         val argName = f.vparams.head.name
//         val body = aux(f.body)
//         val cleanBody = clearIdents(List("rc", argName.toString), body)
//         q"""
//           val $$opt = $opt
//           if ($$opt.nonEmpty) {
//             val $argName = $$opt.get
//             $cleanBody
//           }
//         """
//       // scala 2.12
//       case converter("seqToNode", q"(${seq: Tree}).map[$_, $_](${f: Function})($_)") => optimizeSeq(seq, f)
//       // scala 2.13
//       case converter("seqToNode", q"${seq: Tree}.map[$_](${f: Function})") => optimizeSeq(seq, f)
      // Optimize empty nodes
      case '{void} => '{}
      case '{Empty} => '{}
      // Optimize control flow
      case '{if ($cond) ($lhs: Document[T]) else ($rhs: Document[T])} =>
        '{if ($cond) ${aux(rc, lhs)} else ${aux(rc, rhs)}}
      case '{when($cond)(($expr: Document[T]))($ev)} =>
        '{if ($cond) ${aux(rc, expr)}}
      // case q"$expr match { case ..$cases }" =>
      //   val optimizedCases = cases map {
      //     case cq"$p => ${b: Tree}" => cq"$p => ${aux(b)}"
      //     case cq"$p if $c => ${b: Tree}" => cq"$p if $c => ${aux(b)}"
      //   }
      //   q"$expr match { case ..$optimizedCases }"
      // Can't optimize
      case _ =>
        '{$tree.apply($rc)}
    }
    
    val optimized = '{
      levsha.Document.Node[T]{ rc =>
        ${aux('{rc}, node)}
      }
    }
    // DEBUG
    //report.info(optimized.show)
    optimized 
  }

}
