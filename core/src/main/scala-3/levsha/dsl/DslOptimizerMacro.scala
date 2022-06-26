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
import levsha.Document.{Attr, Empty, Node, Style}
import levsha.RenderContext

import scala.quoted.*
import scala.annotation.tailrec

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import DslOptimizerUtils.*

object DslOptimizerMacro:

  def optimize[T: Type](node: Expr[Node[T]])(using Quotes): Expr[Node[T]] =

    import quotes.reflect.*
    import util.*

    cleanupUnableToOptimizeFile // Run once at first start of macro

    @tailrec
    def orderDocument(expr: Expr[Document[T]]): Int = expr match {
      case '{when[T]($cond)($expr)} => orderDocument(expr)
      case '{Empty} => -3
      case '{void} => -3
      case x if x.isExprOf[Style[T]] => -3
      case x if x.isExprOf[Attr[T]] => -2
      case x if x.isExprOf[Node[T]] => -1
      case _ => 0
    }

    def containsUnspecifiedDocument(xs: Seq[Expr[Document[T]]]): Boolean =
      xs.exists(x => orderDocument(x) == 0)

    def logUnableToOptimizeTerm(term: Term): Unit =
      val pos = term.pos
      logUnableToOptimize(
        pos.sourceFile.toString, pos.startLine + 1,
        pos.startColumn + 1, util.Try(pos.sourceCode).getOrElse(None)
      )

    def optimizeNode(rc: Expr[RenderContext[T]], tagDef: Expr[TagDef], children: Seq[Expr[Document[T]]]) =
      val xs = children
        .sortBy { x =>
          val order = orderDocument(x)
          if (order == 0 && unableToSortTagWarningsEnabled)
            report.warning(unableToSortTagWarning, x)
          order
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
    def aux(rc: Expr[RenderContext[T]], targetExpr: Expr[Document[T]]): Expr[Any] = targetExpr match {
      // Optimize tag open/close
      case '{(${tagDef}: TagDef).apply[T](${Varargs(children)}:_*)} =>
        if !containsUnspecifiedDocument(children) | unableToSortForceOptimization then
          optimizeNode(rc, tagDef, children)
        else
          logUnableToOptimizeTerm(targetExpr.asTerm)
          '{$targetExpr.apply($rc)}
      // Optimize attributes and styles
      case '{(${styleDef}: StyleDef).@=[T]($styleValue)} =>
        '{$rc.setStyle($styleDef.name, $styleValue)}
      case '{(${attrDef}: AttrDef).:=[T]($attrValue)} =>
        '{$rc.setAttr($attrDef.ns, $attrDef.name, $attrValue)}
      // Optimize converters
      case '{stringToNode($value)} => '{$rc.addTextNode($value)}
      case '{miscToNode[T]($value)} => '{$rc.addMisc($value)}
      case '{optionToNode[T](($opt: Option[x]).map($f))} =>
          '{
            if ($opt.nonEmpty) {
              val x = $opt.get
              ${aux(rc, Expr.betaReduce('{($f)(x)}))}
            }
          }
      case '{optionToAttr[T](($opt: Option[x]).map($f))} =>
          '{
            if ($opt.nonEmpty) {
              val x = $opt.get
              ${aux(rc, Expr.betaReduce('{($f)(x)}))}
            }
          }
      case '{optionToStyle[T](($opt: Option[x]).map($f))} =>
        '{
          if ($opt.nonEmpty) {
            val x = $opt.get
            ${aux(rc, Expr.betaReduce('{($f)(x)}))}
          }
        }
      case '{seqToNode[T](($seq: Seq[x]).map($f))} =>
          '{
            val i = $seq.iterator
            while (i.hasNext) {
              val x = i.next
              ${aux(rc, Expr.betaReduce('{($f)(x)}))}
            }
          }
      case '{seqToAttr[T](($seq: Seq[x]).map($f))} =>
        '{
          val i = $seq.iterator
          while (i.hasNext) {
            val x = i.next
            ${aux(rc, Expr.betaReduce('{($f)(x)}))}
          }
        }
      case '{seqToStyle[T](($seq: Seq[x]).map($f))} =>
        '{
          val i = $seq.iterator
          while (i.hasNext) {
            val x = i.next
            ${aux(rc, Expr.betaReduce('{($f)(x)}))}
          }
        }
      // Optimize empty nodes
      case '{void} => '{}
      case '{Empty} => '{}
      // Optimize control flow
      case '{if ($cond) ($lhs: Document[T]) else ($rhs: Document[T])} =>
        '{if ($cond) ${aux(rc, lhs)} else ${aux(rc, rhs)}}
      case '{when($cond)(($expr: Document[T]))} =>
        '{if ($cond) ${aux(rc, expr)}}
      // Corner cases
      case _ =>
        def trasformBlock(statements: List[Statement], expr: Term): Term =
          val uExpr = aux(rc, expr.asExprOf[Document[T]])
          Block(statements, uExpr.asTerm)
        def trnasfromCases(cases: List[CaseDef]): List[CaseDef] =
          cases.map {
            case CaseDef(pat, guard, body) =>
              val uBody = aux(rc, body.asExprOf[Document[T]])
              CaseDef(pat, guard, uBody.asTerm)
          }

        targetExpr.asTerm match {
          case Inlined(_, _, Block(statements, expr)) => trasformBlock(statements, expr).asExpr
          case Inlined(_, _, tree @ Match(selector, cases)) =>
            Match
              .copy(tree)(selector, trnasfromCases(cases))
              .asExpr
          case Block(statements, expr) => trasformBlock(statements, expr).asExpr
          case tree @ Match(selector, cases) =>
            Match
            .copy (tree) (selector, trnasfromCases (cases) )
            .asExpr
          // Unable to optimize
          case term =>
            // DEBUG
            // report.info(term.show)
            // ---
            logUnableToOptimizeTerm(term)
            '{$targetExpr.apply($rc)}
        }
    }

    val optimized = '{
      levsha.Document.Node[T]{ rc =>
        ${aux('{rc}, node)}
      }
    }

    // DEBUG
    //report.info(optimized.show)
    // ---
    optimized

end DslOptimizerMacro