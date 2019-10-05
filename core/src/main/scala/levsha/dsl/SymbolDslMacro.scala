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

import levsha.Document

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

class SymbolDslMacro(val c: blackbox.Context) extends OptimizerMacro {

  import c.universe._

  def node[MT: c.WeakTypeTag](children: c.Tree*): c.Tree = {

    val ops = {
      children
        // Check one of children has ambiguous Document type
        // Children should be Attr, Node or Empty
        .map { child =>
          // I've encountered with strange behavior in macros.
          // When I give if expression to def macro,
          // 1) Macro invokes twice
          // 2) On first invoke if expression has
          //    unexpected type: if (true) A(1) else A(2) has type X[Int] but I expect A[Int]
          //    where sealed trait X[-T]; case class A[-T](v: T) extends X[T].
          // Reproduced in 2.11.11 and 2.12.2
          // Hope https://issues.scala-lang.org/browse/SI-5464 will not touch this
          val utc = c.untypecheck(child)
          val tc = c.typecheck(utc)
          if (tc.tpe =:= weakTypeOf[Document[MT]])
            c.error(tc.pos, "Should be node, attribute or void")
          // Save both untypechecked tree and typechecked tree.
          // Untypechecked tree can be saved for future use
          // Typechecked tree will be used right no to decide
          // how not transform the tree.
          (utc, tc)
        }
        // Attributes always on top
        .sortBy(_._2.tpe) { (x, y) =>
          if (x =:= weakTypeOf[Document.Attr[MT]]) -1
          else 0
        }
        .flatMap {
          case (tree, _) =>
            Seq(optimize(tree))
        }
    }

    val MT = weakTypeOf[MT]
    val (nodeXmlNs, nodeName) = unfoldQualifiedName(c.prefix.tree)

    q"""
      levsha.Document.Node.apply[$MT] { rc =>
        rc.openNode($nodeXmlNs, $nodeName)
        ..$ops
        rc.closeNode($nodeName)
      }
    """
  }

  def attr[MT: c.WeakTypeTag](value: c.Tree): c.Tree = {
    val MT = weakTypeOf[MT]
    val (xmlNs, attr) = unfoldQualifiedName(c.prefix.tree)

    q"""
      levsha.Document.Attr.apply[$MT] { rc =>
        rc.setAttr($xmlNs, $attr, $value)
      }
    """
  }

  def xmlNsCreateQualifiedName(symbol: c.Tree): c.Tree = {
    val q"$conv(${rawName: Tree})" = c.prefix.tree
    q"levsha.QualifiedName($rawName, $symbol)"
  }

  // Utils

  private def unfoldQualifiedName(tree: Tree): (Tree, String) = tree match {
    case Apply(Select(_, TermName("QualifiedNameOps")), Typed(Apply(_, List(xmlNs, rawName)), _) :: Nil) =>
      (xmlNs, toKebab(rawName))
    case expr @ q"$conv(${rawName: Tree})" =>
      (q"levsha.XmlNs.html", toKebab(rawName))
  }

  /**  Converts symbol 'camelCase to "kebab-case" */
  private def toKebab(tree: Tree): String = tree match {
    case q"scala.Symbol.apply(${value: String})" => value.replaceAll("([A-Z]+)", "-$1").toLowerCase
    case _ => c.abort(tree.pos, s"Expect scala.Symbol but ${tree.tpe} given")
  }
}
