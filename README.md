Levsha
=======

Levsha is a fast HTML template engine and Scala eDSL. It works without additional memory allocation. 
Levsha supports changeset inference, which allows to use it as virtual-dom-like middleware. 

Static rendering
----------------

You can use Levsha as a static HTML renderer.

```scala
// build.sbt
libraryDependencies += "com.github.fomkin" %% "levsha-core" % "0.2.0"
```

```scala
// In your code
import levsha.default.dsl._
import levsha.default.renderHtml

val features = Seq("Very fast", "Memory-effective")

val html = renderHtml { implicit rc =>
  'body(
    'div('class /= "title", "Hello, I'm Levsha!"),
    'ul('class /= "list",
      features map { feature =>
        'li('class /= "item", feature)
      }
    )
  )
}

println(html)
```

```html
<body>
  <div class="title">Hello, I'm Levsha!</div>
  <ul class="list">
    <li class="item">Super-fast</li>
    <li class="item">Off-heap</li>
  </ul>
</body>
```

Static Rendering Benchmarks
---------------------------

The benchmarks shows that Levsha is really fast. Unlike Twirl, 
Levsha's performance does not depends on template complexity.

| Test                  | Engine        | Ops/s        |
| --------------------- |:--------------| ------------:|
| simpleHtml            | levsha        | 1336693,499  |
| simpleHtml            | scalatags     |  533740,566  |
| simpleHtml            | twirl         | 5950436,854  |
| withConditionAndLoop  | levsha        | 1299646,768  |
| withConditionAndLoop  | scalatags     | 531345,430   |
| withConditionAndLoop  | twirl         | 239537,158   |
| withVariables         | levsha        | 1140298,804  |
| withVariables         | scalatags     | 483508,457   |
| withVariables         | twirl         | 2146419,329  |

In your sbt shell.

```
bench/jmh:run .StaticRenderingComparision
```

As a virtual DOM
----------------

Levsha can be used as virtual-dom-like middleware. Unlike other popular 
virtual DOM solutions, Levsha doesn't allocate additional memory for construction
new virtual DOM copy. Also it does not allocate memory in changes inferring phase.
Levsha's memory usage is constant 64k.

```scala
// build.sbt
libraryDependencies += "com.github.fomkin" %%% "levsha-dom" % "0.2.0"
```

```scala
// In your code
import org.scalajs.dom._
import levsha.dom.dsl._
import levsha.dom.render
import levsha.dom.event

case class Todo(id: String, text: String, done: Boolean)

def renderTodos(todos: Seq[Todo]): Unit = render(document.body) { implicit rc =>
  'body(
    'div('class /= "title", "Todos"),
    'ul('class /= "list",
      todos map { todo =>
        'li(
          todo match {
            case Todo(_, text, true) => 'strike(text)
            case Todo(_, text, false) => 'span(text)
          },
          event('click) {
            renderTodos(
              todos.updated(
                todos.indexOf(todo),
                todo.copy(done = !todo.done)
              )
            )
          }
        )
      }
    ),
    'input('id /= "todo-input", 'placeholder /= "New ToDo"),
    'button("Submit",
      event('click) {
        val input = document
          .getElementById("todo-input")
          .asInstanceOf[html.Input]
        val inputText = input.value
        // Reset input
        input.value = ""
        val newTodo = Todo(
          id = Random.alphanumeric.take(5).mkString,
          text = inputText,
          done = false
        )
        renderTodos(todos :+ newTodo)
      }
    )
  )
}

val todos = Seq(
  Todo("1", "Start use Levsha", done = false),
  Todo("2", "Lean back and have rest", done = false)
)

renderTodos(todos)
```

Worthy to note
--------------

1. [The Tale of Cross-eyed Lefty from Tula and the Steel Flea](https://en.wikipedia.org/wiki/The_Tale_of_Cross-eyed_Lefty_from_Tula_and_the_Steel_Flea)
