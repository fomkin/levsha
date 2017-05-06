levsha.benchmark.Levsha
=======

levsha.benchmark.Levsha is a fast HTML template engine and Scala eDSL, working without memory allocation. levsha.benchmark.Levsha supports changeset inference, which allows to use it as virtual-dom-like middleware. 

Static rendering
----------------

You can use levsha.benchmark.Levsha as a static HTML renderer.
 
```scala
import levsha.default.dsl._
import levsha.default.renderHtml

val features = Seq("Super-fast", "Off-heap")

val html = renderHtml { implicit rc =>
  'body(
    'div('class /= "title", "Hello, I'm levsha.benchmark.Levsha!"),
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
  <div class="title">Hello, I'm levsha.benchmark.Levsha!</div>
  <ul class="list">
    <li class="item">Super-fast</li>
    <li class="item">Off-heap</li>
  </ul>
</body>
```

Worthy to note
--------------

1. [The Tale of Cross-eyed Lefty from Tula and the Steel Flea]((https://en.wikipedia.org/wiki/The_Tale_of_Cross-eyed_Lefty_from_Tula_and_the_Steel_Flea))
