package levsha.benchmark

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Level, Mode, Scope, Setup, State}
import levsha.impl.{DiffRenderContext, TextReportChangesPerformer}
import levsha.dsl._
import levsha.dsl.html._
import levsha.impl.DiffRenderContext.DummyChangesPerformer

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class DiffRenderContextBenchmark {

  import DiffRenderContextBenchmark._

//  @Benchmark def renderTreeToDRC(state: RenderBenchmarkState): Unit = {
//    a.apply(state.renderContext)
//  }

  @Benchmark
  //@BenchmarkMode(Array(Mode.SampleTime))
  def inferChangesBetweenTwoTrees(state: DiffBenchmarkState): Unit = {
    state.renderContext.swap() // set a on lhs, b on rhs
    state.renderContext.diff(state.changesPerformer) // diff a with b
    state.renderContext.swap() // set b on lhs, a on rhs
  }
}

object DiffRenderContextBenchmark {

  @State(Scope.Thread) class RenderBenchmarkState {

    final val renderContext = DiffRenderContext()

    @Setup(Level.Invocation) def setup(): Unit = {
      renderContext.reset()
    }
  }

  @State(Scope.Thread) class DiffBenchmarkState {

    final val renderContext = DiffRenderContext()
    final val changesPerformer = new TextReportChangesPerformer()
    @Setup(Level.Trial) def setup(): Unit = {
      a(renderContext)
      renderContext.finalizeDocument()
      renderContext.swap()
      b(renderContext)
      renderContext.finalizeDocument()
    }

    @Setup(Level.Invocation) def cleaPerformer() = {
      changesPerformer.sb.clear()
    }
  }

  val a = optimize {
    div(
      backgroundColor @= "blue",
      margin @= "10px",
      "y",
      div(lang := "en", clazz := "cow", padding @= "10px"),
      span(
        span(clazz := "lol",
             border @= "1 px solid",
             backgroundColor @= "blue",
             span(clazz := "lol", padding @= "2px", backgroundColor @= "blue", margin @= "2px"))
      ),
      div(clazz := "cow",
          padding @= "2px",
          backgroundColor @= "blue",
          div(name := "cow",
              lang := "ru",
              clazz := "world",
              padding @= "2px",
              backgroundColor @= "blue",
              margin @= "10px")),
      span(clazz := "world",
           margin @= "2px",
           "D",
           input(clazz := "lol", backgroundColor @= "red"),
           div(clazz := "hello")),
      span(border @= "1 px solid", padding @= "5ox"),
      div(lang := "ru", name := "cow", border @= "1 px solid", margin @= "5ox", backgroundColor @= "greed"),
      ul(clazz := "lol", div(clazz := "lol", margin @= "10px", backgroundColor @= "blue", padding @= "10px"), "s", "e"),
      div(name := "I", clazz := "cow", margin @= "5ox", padding @= "5ox"),
      div(clazz := "hello", backgroundColor @= "blue", padding @= "2px", "h"),
      div(name := "am", lang := "en", clazz := "world", padding @= "2px", div()),
      "w",
      div(clazz := "cow", padding @= "2px", backgroundColor @= "blue", margin @= "10px"),
      "tzv",
      div(
        clazz := "world",
        padding @= "5ox",
        div(lang := "ru"),
        span(style := "margin: 10;", clazz := "lol", padding @= "2px", backgroundColor @= "red"),
        div(clazz := "cow", margin @= "10px")
      ),
      div(
        name := "Korolev",
        lang := "en",
        style := "margin: 10;",
        margin @= "10px",
        backgroundColor @= "greed",
        button(padding @= "10px", margin @= "5ox", div(clazz := "world", margin @= "5ox"))
      ),
      "hsj",
      button(
        input(margin @= "5ox", border @= "1 px solid", padding @= "10px"),
        ul(lang := "ru", clazz := "lol", padding @= "10px")
      ),
      ul(clazz := "cow", margin @= "10px", "y"),
      "d",
      "oh",
      "ubo",
      "v",
      div(lang := "en",
          clazz := "world",
          padding @= "10px",
          margin @= "2px",
          span(padding @= "10px", margin @= "5ox"),
          span(lang := "ru", "r")),
      span(lang := "en", clazz := "lol", "n", ul(clazz := "world", margin @= "5ox", backgroundColor @= "red")),
      span(margin @= "2px", "k"),
      button(
        lang := "en",
        clazz := "world",
        padding @= "2px",
        backgroundColor @= "red",
        li(lang := "en", clazz := "lol", margin @= "5ox", padding @= "5ox"),
        div(clazz := "lol", margin @= "2px", border @= "1 px solid"),
        "j"
      ),
      "aSe",
      p(
        name := "cow",
        clazz := "cow",
        border @= "1 px solid",
        padding @= "2px",
        div(style := "margin: 10;", lang := "en", margin @= "5ox", padding @= "10px"),
        div(padding @= "10px"),
        span(lang := "en", margin @= "5ox", padding @= "5ox")
      ),
      form(padding @= "5ox", backgroundColor @= "greed"),
      span(clazz := "cow",
           form(clazz := "hello", lang := "ru", padding @= "10px"),
           form(clazz := "lol", margin @= "10px", padding @= "5ox"),
           div(clazz := "world", padding @= "10px")),
      div(
        clazz := "cow",
        margin @= "2px",
        padding @= "5ox",
        ul(name := "am",
           border @= "1 px solid",
           padding @= "2px",
           div(clazz := "world", style := "margin: 10;", margin @= "2px", backgroundColor @= "red")),
        span(clazz := "hello")
      ),
      ul(
        div(name := "cow", margin @= "5ox", padding @= "10px", backgroundColor @= "greed"),
        "j",
        li(backgroundColor @= "red", margin @= "10px")
      ),
      div(clazz := "world",
          margin @= "5ox",
          backgroundColor @= "red",
          padding @= "2px",
          div(lang := "ru", clazz := "world", margin @= "10px")),
      div(clazz := "world", div(lang := "en", name := "I", clazz := "lol"), form(padding @= "5ox"), "j"),
      "rh",
      div(lang := "ru",
          padding @= "5ox",
          backgroundColor @= "red",
          "w",
          span(clazz := "world", margin @= "10px"),
          span(clazz := "cow", backgroundColor @= "red")),
      div(clazz := "world",
          backgroundColor @= "greed",
          margin @= "2px",
          padding @= "5ox",
          li(clazz := "lol", input(padding @= "5ox", margin @= "2px")),
          div(
            "s"
          )),
      "tig",
      button(clazz := "cow", padding @= "2px", margin @= "5ox"),
      "q",
      div(clazz := "cow", margin @= "5ox", "M"),
      div(lang := "en", style := "margin: 10;"),
      div(clazz := "lol",
          padding @= "10px",
          div(clazz := "lol", lang := "en", padding @= "2px"),
          div(clazz := "hello", padding @= "2px")),
      div(clazz := "hello", padding @= "5ox", "p", "a"),
      div(clazz := "world",
          lang := "ru",
          button(name := "I", lang := "ru", backgroundColor @= "greed", margin @= "10px"),
          "v",
          input(clazz := "cow")),
      "gy",
      div(
        div(backgroundColor @= "greed"),
        span(clazz := "lol", margin @= "10px", padding @= "10px"),
        span(margin @= "10px")
      ),
      span(lang := "en", clazz := "lol", margin @= "5ox", border @= "1 px solid", "q"),
      "dlt",
      input(clazz := "world", style := "margin: 10;", margin @= "10px", backgroundColor @= "greed"),
      div(
        name := "am",
        clazz := "cow",
        border @= "1 px solid",
        span(clazz := "world", name := "I", padding @= "10px", margin @= "10px"),
        li(backgroundColor @= "greed", border @= "1 px solid")
      ),
      p(
        clazz := "cow",
        backgroundColor @= "greed",
        margin @= "5ox",
        div(clazz := "hello", backgroundColor @= "greed", margin @= "10px"),
        p(clazz := "lol", name := "cow"),
        div(clazz := "world", margin @= "10px", padding @= "10px")
      ),
      button(clazz := "cow", "y"),
      "j",
      div(clazz := "lol",
          padding @= "10px",
          backgroundColor @= "blue",
          span(lang := "en", clazz := "lol", border @= "1 px solid")),
      "xr",
      "umr",
      div(backgroundColor @= "blue", padding @= "5ox"),
      "jo",
      "cgo",
      div(name := "am",
          style := "padding: 10;",
          clazz := "lol",
          padding @= "10px",
          ul(clazz := "world"),
          "z",
          li(clazz := "cow", margin @= "2px", backgroundColor @= "red")),
      span(
        backgroundColor @= "greed",
        padding @= "10px",
        "f",
        div(clazz := "hello", margin @= "10px", backgroundColor @= "greed", border @= "1 px solid"),
        span(backgroundColor @= "greed", border @= "1 px solid")
      ),
      li(name := "am", clazz := "hello", div(clazz := "hello", div(clazz := "hello", name := "cow"))),
      div(lang := "en",
          clazz := "cow",
          style := "padding: 10;",
          margin @= "5ox",
          padding @= "10px",
          span(lang := "ru", padding @= "5ox", margin @= "10px", "L")),
      span(padding @= "10px", div(name := "I", lang := "ru", margin @= "2px", "r"), "i"),
      div(clazz := "world", lang := "en", padding @= "10px", margin @= "10px"),
      span(name := "I",
           margin @= "2px",
           backgroundColor @= "greed",
           div(name := "cow", clazz := "lol"),
           div(clazz := "lol"),
           div(clazz := "lol", margin @= "5ox")),
      span(clazz := "lol", padding @= "10px"),
      p(padding @= "10px", "x", span(lang := "ru", clazz := "world", padding @= "2px", margin @= "5ox")),
      span(
        clazz := "hello",
        margin @= "2px",
        padding @= "5ox",
        "K",
        div(name := "Korolev", border @= "1 px solid", backgroundColor @= "greed"),
        li(clazz := "world", backgroundColor @= "greed", padding @= "2px")
      ),
      span(lang := "en",
           border @= "1 px solid",
           backgroundColor @= "red",
           div(clazz := "cow", padding @= "10px"),
           input(style := "margin: 10;", lang := "en", padding @= "5ox")),
      div(clazz := "world", backgroundColor @= "greed", padding @= "2px", "t"),
      div(lang := "ru",
          margin @= "5ox",
          "n",
          div(clazz := "lol", name := "cow", padding @= "10px", backgroundColor @= "blue"),
          "N"),
      span(lang := "en", backgroundColor @= "blue", "a", "c"),
      form(clazz := "world",
           style := "margin: 10;",
           margin @= "2px",
           div(clazz := "cow"),
           div(clazz := "world", margin @= "10px", padding @= "2px"),
           span(clazz := "world", name := "am")),
      button(clazz := "cow",
             margin @= "5ox",
             padding @= "5ox",
             div(
               "b"
             )),
      "TQc",
      div(margin @= "5ox", ul(margin @= "5ox", padding @= "2px"), button(clazz := "cow"), "n")
    )
  }

  val b = optimize {
    div(
      clazz := "world",
      name := "Korolev",
      padding @= "2px",
      span(
        lang := "en",
        margin @= "5ox",
        padding @= "2px",
        div(clazz := "lol",
            padding @= "2px",
            backgroundColor @= "blue",
            form(clazz := "cow", backgroundColor @= "red")),
        button(clazz := "world", padding @= "10px")
      ),
      li(lang := "ru", style := "padding: 10;", clazz := "hello", border @= "1 px solid", "s", "j"),
      span(style := "padding: 10;", margin @= "5ox"),
      span(clazz := "lol",
           backgroundColor @= "blue",
           margin @= "2px",
           span(lang := "ru", clazz := "world", padding @= "2px", margin @= "2px", "j")),
      div(
        clazz := "hello",
        backgroundColor @= "red",
        padding @= "5ox",
        border @= "1 px solid",
        div(clazz := "lol", lang := "ru", padding @= "2px", backgroundColor @= "blue"),
        input(border @= "1 px solid", margin @= "2px", div(backgroundColor @= "red"))
      ),
      div(clazz := "lol", "k", "j", span(clazz := "cow", margin @= "5ox", padding @= "2px")),
      "f",
      div(lang := "ru", clazz := "world", p(style := "padding: 10;", clazz := "cow", backgroundColor @= "red")),
      "jjd",
      div(clazz := "cow", margin @= "2px", backgroundColor @= "red"),
      div(lang := "ru", name := "am", clazz := "cow", border @= "1 px solid", padding @= "2px"),
      "rk",
      "po",
      "Au",
      ul(backgroundColor @= "red",
         padding @= "2px",
         div(backgroundColor @= "greed", span(clazz := "cow", padding @= "10px")),
         "i"),
      p(lang := "ru", "u", span(clazz := "lol", div(clazz := "hello"))),
      "qre",
      "f",
      div(clazz := "hello",
          name := "Korolev",
          lang := "en",
          "M",
          input(clazz := "lol"),
          input(value := "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."),
          div(name := "am", clazz := "lol", padding @= "5ox", backgroundColor @= "blue")),
      "tq",
      div(lang := "ru", clazz := "lol"),
      div(lang := "ru", clazz := "hello"),
      "erx",
      div(
        name := "cow",
        clazz := "lol",
        margin @= "5ox",
        backgroundColor @= "greed",
        padding @= "2px",
        "z",
        div(margin @= "2px",
            backgroundColor @= "greed",
            padding @= "10px",
            div(clazz := "world", lang := "en", padding @= "10px", margin @= "2px"))
      ),
      div(margin @= "5ox",
          p(clazz := "cow", name := "am", padding @= "5ox", div(padding @= "5ox", margin @= "5ox")),
          div(margin @= "5ox", "j")),
      "iS",
      span(lang := "en", backgroundColor @= "greed"),
      span(clazz := "world", name := "Korolev", margin @= "5ox", span(clazz := "world", margin @= "5ox"), "s"),
      "hh",
      span(name := "I", clazz := "cow", "k"),
      li(
        name := "I",
        margin @= "2px",
        padding @= "10px",
        span(lang := "ru", clazz := "hello"),
        div(clazz := "world", name := "cow"),
        li(style := "padding: 10;", clazz := "hello", padding @= "2px")
      ),
      div(clazz := "cow", padding @= "2px", margin @= "10px", p(), "l"),
      "pzp",
      "oaj",
      span(
        margin @= "2px",
        padding @= "10px",
        div(clazz := "world", lang := "en", margin @= "5ox", border @= "1 px solid"),
        div(backgroundColor @= "blue", border @= "1 px solid"),
        form(style := "padding: 10;", clazz := "world", padding @= "5ox", margin @= "5ox")
      ),
      div(
        name := "Korolev",
        clazz := "cow",
        lang := "en",
        margin @= "2px",
        span(name := "am", clazz := "world", padding @= "2px", backgroundColor @= "blue", margin @= "2px", "z"),
        span(border @= "1 px solid")
      ),
      div(clazz := "lol",
          span(clazz := "world", margin @= "2px", p(clazz := "world", padding @= "2px", border @= "1 px solid"))),
      span(clazz := "world",
           margin @= "10px",
           "M",
           span(clazz := "world", style := "margin: 10;", margin @= "10px", form(lang := "en", clazz := "cow"))),
      form(
        style := "margin: 10;",
        clazz := "cow",
        padding @= "5ox",
        span(clazz := "world", padding @= "5ox", p(name := "Korolev")),
        span(clazz := "hello", style := "margin: 10;", backgroundColor @= "red", margin @= "10px", padding @= "10px")
      ),
      span(clazz := "world",
           style := "margin: 10;",
           lang := "en",
           border @= "1 px solid",
           backgroundColor @= "blue",
           "e"),
      ul(clazz := "lol", margin @= "10px", padding @= "2px"),
      div(lang := "ru", clazz := "lol", div(clazz := "lol", margin @= "5ox", border @= "1 px solid", "h"), div()),
      div(
        padding @= "2px",
        backgroundColor @= "red",
        input(clazz := "cow", padding @= "2px", margin @= "5ox", backgroundColor @= "greed"),
        li(),
        button(clazz := "hello", padding @= "10px")
      ),
      input(clazz := "world", name := "cow", border @= "1 px solid", padding @= "10px", margin @= "5ox", "t"),
      div(lang := "ru", clazz := "lol", padding @= "10px", "s", span(), div(margin @= "2px", padding @= "10px")),
      ul(clazz := "lol", padding @= "5ox", border @= "1 px solid"),
      p(clazz := "world",
        margin @= "2px",
        padding @= "2px",
        div(clazz := "lol", name := "I", ul(clazz := "hello", lang := "en", margin @= "2px", padding @= "10px"))),
      "j",
      div(clazz := "world", backgroundColor @= "red", margin @= "10px"),
      span(margin @= "5ox", ul(clazz := "world", border @= "1 px solid", margin @= "2px")),
      "cj",
      div(padding @= "5ox",
          backgroundColor @= "blue",
          input(clazz := "lol"),
          p(name := "am", clazz := "cow", border @= "1 px solid", backgroundColor @= "red")),
      "h",
      "ty",
      "qsf",
      input(lang := "ru",
            clazz := "lol",
            border @= "1 px solid",
            padding @= "5ox",
            div(margin @= "2px", padding @= "2px")),
      li(clazz := "world", backgroundColor @= "red", "o"),
      div(padding @= "10px",
          div(clazz := "cow", padding @= "5ox", backgroundColor @= "blue"),
          span(clazz := "world"),
          span(padding @= "5ox")),
      ul(margin @= "2px", backgroundColor @= "blue"),
      "tqS",
      input(
        "h"
      ),
      "bc",
      div(
        padding @= "2px",
        backgroundColor @= "blue",
        margin @= "10px",
        span(clazz := "lol", backgroundColor @= "blue", padding @= "10px"),
        input(lang := "en", clazz := "lol", margin @= "5ox"),
        "u"
      ),
      div(),
      span(
        clazz := "world",
        padding @= "2px",
        backgroundColor @= "red",
        ul(clazz := "world",
           style := "margin: 10;",
           lang := "ru",
           border @= "1 px solid",
           margin @= "5ox",
           span(lang := "ru", padding @= "5ox", margin @= "5ox"))
      ),
      div(clazz := "hello", name := "cow", backgroundColor @= "red", padding @= "5ox"),
      input(
        border @= "1 px solid",
        "v",
        div(clazz := "world", lang := "en", margin @= "5ox", backgroundColor @= "blue"),
        ul(clazz := "cow", padding @= "5ox", margin @= "5ox", backgroundColor @= "blue")
      )
    )
  }

}
