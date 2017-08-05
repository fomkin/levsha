# Levsha

Levsha is a fast HTML template engine and Scala eDSL. It works without additional memory allocation. 
Levsha supports changeset inference, which allows to use it as virtual-dom-like middleware. 

## Static rendering

You can use Levsha as a static HTML renderer.

```scala
// build.sbt
libraryDependencies += "com.github.fomkin" %% "levsha-core" % "0.5.0"
```

```scala
// In your code
import levsha.text.symbolDsl._
import levsha.text.renderHtml

val features = Seq("Very fast", "Memory-effective")

val html = renderHtml {
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

#### Benchmarks

Benchmarks show that Levsha is really fast. Unlike Twirl, 
Levsha's performance does not depend on template complexity.

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

## As a virtual DOM

Levsha can be used as virtual-DOM-like middleware. Unlike other popular 
virtual DOM solutions, Levsha doesn't allocate additional memory for construction
of a new virtual DOM copy. Also it does not allocate memory in changes inferring phase.
Levsha's memory usage is constant.

```scala
// build.sbt
libraryDependencies += "com.github.fomkin" %%% "levsha-dom" % "0.5.0"
```

```scala
// In your code
import org.scalajs.dom._
import levsha.dom.symbolDsl._
import levsha.dom.render
import levsha.dom.event

case class Todo(id: String, text: String, done: Boolean)

def renderTodos(todos: Seq[Todo]): Unit = render(document.body) {
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
## Memory allocation model explanation

As noted below Levsha does not make _additional_
memory allocations. It is possible because every
template, written in Levsha DSL, in compile-time
optimizes into calls of `RenderContext` methods
(unlike other template engines which  
represent their templates as AST on-heap).

For example,

```scala
'div('class /= "content", 
  'h1("Hello world"),
  'p("Lorem ipsum dolor")
)
```

Will be rewritten to

```scala
Node { renderContext =>
  renderContext.openNode(XmlNs.html, "div")
  renderContext.setAttr(XmlNs.html, "class", "content")
  renderContext.openNode(XmlNs.html, "h1")
  renderContext.addTextNode("Hello world")
  renderContext.closeNode("h1")
  renderContext.openNode(XmlNs.html, "p")
  renderContext.addTextNode("Lorem ipsum dolor")
  renderContext.closeNode("p")
  renderContext.closeNode("div")
}
```

In turn, `RenderContext` (namely `DiffRenderContext` implementation)
saves instructions in `ByteBuffer` to infer changes in the future.

Of course, Levsha optimizer does not cover all cases.
When optimization can't be performed Levsha just 
applies current `RenderContext` to the unoptimized node.

```scala
'ul(
  Seq(1, 2, 3, 4, 5, 6, 7).collect { 
    case x if x % 2 == 0 => 'li(x.toString)
  }
)

// ==>

Node { renderContext =>
  renderContext.openNode(XmlNs.html, "ul")
  Seq(1, 2, 3, 4, 5, 6, 7)
    .collect {
      case x if x % 2 == 0 => 
        Node { renderContext =>
          renderContext.openNode(XmlNs.html, "li")
          renderContext.addTextNode(x.toString)
          renderContext.closeNode("li")
        }
    }
    .foreach { childNode =>
      childNode.apply(renderContext)
    }
  renderContext.closeNode("ul")
}
```

When you write your Levsha templates, keep in 
your mind this list of optimizations:

1. Nodes and attrs in branches of `if` expression will be moved to current `RenderContext`
2. Same for cases of pattern matching
3. `xs.map(x => 'div(x))` will be rewritten into a `while` loop
4. `maybeX.map(x => 'div(x))` will be rewritten into an `if` expression
5. `void` will be removed

The third item of this list shows us how to rewrite
previous example so that optimization could be performed.

```scala
'ul(
  Seq(1, 2, 3, 4, 5, 6, 7)
    .filter(x => x % 2 == 0)
    .map { x => 'li(x.toString) }
)

// ==>

Node { renderContext =>
  renderContext.openNode(XmlNs.html, "div")
  val iterator = Seq(1, 2, 3, 4, 5, 6, 7)
    .filter(x => x % 2 == 0)
    .iterator
  while (iterator.hasNext) {
    val x = iterator.next()
    renderContext.openNode(XmlNs.html, "li")
    renderContext.addTextNode(x.toString)
    renderContext.closeNode("li")
  }
  renderContext.closeNode("div")
}
```

If you are not sure about you code, run compiler 
with `levsha.macros.notOptimizedWarnings=true`
parameter. For example `sbt -Dlevsha.macros.notOptimizedWarnings=true`

## Worthy to note

1. [The Tale of Cross-eyed Lefty from Tula and the Steel Flea](https://en.wikipedia.org/wiki/The_Tale_of_Cross-eyed_Lefty_from_Tula_and_the_Steel_Flea)
