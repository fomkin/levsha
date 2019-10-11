package levsha.dsl

import levsha.Document.{Attr, Style}

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
      case q"$tagDef.apply[$_](..$children)" if tagDef.tpe <:< typeOf[TagDef] =>
        val transformedChildren = children
          .sortBy {
            case x if x.tpe =:= weakTypeOf[Style[T]] => -2
            case x if x.tpe =:= weakTypeOf[Attr[T]] => -1
            case _ => 0
          }
          .map(aux)
        q"""
            rc.openNode($tagDef.ns, $tagDef.tagName)
            ..$transformedChildren
            rc.closeNode($tagDef.tagName)
          """
      // Optimize attributes and styles
      case q"$styleDef.@=[$_]($styleValue)" =>
        q"rc.setStyle($styleDef.styleName, $styleValue)"
      case q"$attrDef.:=[$_]($attrValue)" =>
        q"rc.setAttr($attrDef.ns, $attrDef.attrName, $attrValue)"
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
      case q"$expr match { case ..$cases }" =>
        val optimizedCases = cases map {
          case cq"$p => ${b: Tree}" => cq"$p => ${aux(b)}"
          case cq"$p if $c => ${b: Tree}" => cq"$p if $c => ${aux(b)}"
        }
        q"$expr match { case ..$optimizedCases }"
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
