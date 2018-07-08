package levsha.dsl

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait Optimizer {

  val c: blackbox.Context

  import c.universe._

  def optimize(tree: c.Tree): c.Tree = tree match {
    // Basic optimizations
    case Block(body, extractOpenNode(xs)) => q"..$body; ..$xs"
    case Select(_, TermName("void")) => EmptyTree
    case Typed(q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($ns, $k, $v) }", _) => q"rc.setAttr($ns, $k, $v)"
    case q"levsha.Document.Attr.apply[$t] { rc => rc.setAttr($ns, $k, $v) }" => q"rc.setAttr($ns, $k, $v)"
    case extractOpenNode(xs) => q"..$xs"
    case extractConverter("stringToNode", value) => q"rc.addTextNode($value)"
    case extractConverter("miscToNode", value) => q"rc.addMisc($value)"
    // Advanced optimizations
    case extractConverter("seqToNode", q"($seq).map[$b, $that](${f: Function})($bf)") =>
      val argName = f.vparams.head.name
      q"""
          val $$iter = $seq.iterator
          while ($$iter.hasNext) {
            val $argName = $$iter.next()
            ${optimize(cleanIdents(List("rc", argName.toString), f.body))}
          }
        """
    case extractConverter("optionToNode" | "optionToAttr", q"($opt).map[$b](${f: Function})") =>
      val argName = f.vparams.head.name
      q"""
          val $$opt = $opt
          if ($$opt.nonEmpty) {
            val $argName = $$opt.get
            ${optimize(cleanIdents(List("rc", argName.toString), f.body))}
          }
        """
    // Control flow optimization
    case q"if ($cond) ${left: Tree} else ${right: Tree}" =>
      q"if ($cond) ${optimize(left)} else ${optimize(right)}"
    case q"$skip match { case ..$cases }" =>
      val optimizedCases = cases map {
        case cq"$p => ${b: Tree}" => cq"$p => ${optimize(b)}"
        case cq"$p if $c => ${b: Tree}" => cq"$p if $c => ${optimize(b)}"
      }
      q"$skip match { case ..$optimizedCases }"
    // Can't optimize
    case _ =>
      if (notOptimizedWarningsEnabled)
        c.warning(tree.pos, "Can't optimize")
      q"$tree.apply(rc)"
  }

  private object extractConverter {
    def unapply(tree: Tree): Option[(String, Tree)] = tree match {
      case Apply(Select(_, TermName(fun)), value :: Nil) => Some((fun, value))
      case _ => None
    }
  }

  private object extractOpenNode {
    def unapply(tree: Tree): Option[Seq[Tree]] = tree match {
      case q"levsha.Document.Node.apply[$t] { rc => ..${ops: Seq[Tree]} }" =>
        Some(ops.map(cleanIdents(List("rc"), _)))
      case Typed(q"levsha.Document.Node.apply[$t] { rc => ..${ops: Seq[Tree]} }", _) =>
        Some(ops.map(cleanIdents(List("rc"), _)))
      case _ => None
    }
  }

  // Remove context binding from idents
  private def cleanIdents(names: List[String], tree: Tree) = {
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

  private val notOptimizedWarningsEnabled = {
    val propName = "levsha.macros.notOptimizedWarnings"
    sys.props.get(propName)
      .orElse(sys.env.get(propName))
      .fold(false)(x => if (x == "true") true else false)
  }
}
