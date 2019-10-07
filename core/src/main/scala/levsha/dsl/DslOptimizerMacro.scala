package levsha.dsl

import levsha.dsl.DefaultDsl.{AttrDef, TagDef}

import scala.reflect.api.Trees
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
      case q"$tagDef.apply(..$children)" if tagDef.tpe <:< typeOf[TagDef] =>
        val transformedChildren = children.map(aux)
        q"""
            rc.openNode($tagDef.ns, $tagDef.tagName)
            ..$transformedChildren
            rc.closeNode($tagDef.tagName)
          """
      // Optimize set attribute
      case q"$attrDef.:=($attrValue)" if attrDef.tpe <:< typeOf[AttrDef[_]] =>
        q"rc.setAttr($attrDef.ns, $attrDef.attrName, $attrDef.mkString($attrValue))"
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
      case q"levsha.dsl.DefaultDsl.Html.void" => q"()"
      case q"levsha.Document.Empty" => q"()"
      // Optimize control flow
      case q"if ($cond) ${lhs: Tree} else ${rhs: Tree}" =>
        q"if ($cond) ${aux(lhs)} else ${aux(rhs)}"
      // TODO match
      // Can't optimize
      case expr if expr.tpe <:< typeOf[levsha.Document[_]] => q"$expr.apply(rc)"
      // Skip this code
      case _ => tree
    }
    val xx =q"""
      levsha.Document.Node[$T]{ rc =>
        ${aux(node)}
      }
     """
    println("--------")
    println(xx)
    c.untypecheck(xx)
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
}
