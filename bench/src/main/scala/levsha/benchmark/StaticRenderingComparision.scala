package levsha.benchmark

import org.openjdk.jmh.annotations.{Benchmark, Param, Scope, State}

@State(Scope.Benchmark)
class StaticRenderingComparision {

  import StaticRenderingComparision.engines

//  @Param(Array("levsha", "twirl", "scalatags", "beard"))
  @Param(Array("levsha", "scalatags"))
  var engineName: String = _

  @Benchmark def simpleHtml(): Unit = engines(engineName).simpleHtml()
  @Benchmark def withVariables(): Unit = engines(engineName).withVariables()
  @Benchmark def withConditionAndLoop(): Unit = engines(engineName).withConditionAndLoop()
}

object StaticRenderingComparision {

  trait TemplateEngine {
    def simpleHtml(): Unit
    def withVariables(): Unit
    def withConditionAndLoop(): Unit
  }

  val engines = Map(

    "levsha" -> new TemplateEngine {

      import sharedContent._
      import levsha.default.dsl._
      import levsha.default.renderHtml

      def simpleHtml(): Unit = {
        renderHtml { implicit rc =>
          'body(
            'div('class /= "title", "Hello, I'm Cow!"),
            'ul('class /= "list",
              'li("One"),
              'li("Two"),
              'li("Three"),
              'li("..."),
              'li("Moooo")
            )
          )
        }
      }
      def withVariables(): Unit = {
        renderHtml { implicit rc =>
          'body(
            'div('class /= "title", greeting),
            'ul('class /= className,
              'li("One"),
              'li("Two"),
              'li("Three"),
              'li("..."),
              'li("Moooo")
            )
          )
        }
      }
      def withConditionAndLoop(): Unit = {
        renderHtml { implicit rc =>
          'body(
            'div('class /= "title",
              if (condition) leftBranch
              else rightBranch
            ),
            'ul('class /= className,
              items.map(item => 'li(item))
            )
          )
        }
      }
    },

    "scalatags" -> new TemplateEngine {

      import sharedContent._
      import scalatags.Text.all._

      def simpleHtml(): Unit = {
        val tags = body(
          div(`class` := "title")("Hello, I'm Cow!"),
          ul(`class` := "list")(
            li("One"),
            li("Two"),
            li("Three"),
            li("..."),
            li("Moooo")
          )
        )
        tags.render
      }
      def withVariables(): Unit = {
        val tags = body(
          div(`class` := "title")(greeting),
          ul(`class` := className)(
            li("One"),
            li("Two"),
            li("Three"),
            li("..."),
            li("Moooo")
          )
        )
        tags.render
      }
      def withConditionAndLoop(): Unit = {
        val tags = body(
          div(`class` := "title")(
            if (condition) leftBranch
            else rightBranch
          ),
          ul(`class` := className)(
            items.map(item => li(item))
          )
        )
        tags.render
      }
    }

  )

  // The content should be rendered by template engine
  object sharedContent {

    val greeting = "Hello, I'm Cow!"
    val className = "list"

    val condition = true
    val leftBranch = "Welcome!"
    val rightBranch = "Get off!"
    val items = Seq(
      "One",
      "Two",
      "Three",
      "...",
      "Moooo"
    )
  }
}
