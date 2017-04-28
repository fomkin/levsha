Levsha
-------

Fast off-heap eDSL for HTML

```scala
import levsha.default.dsl._
import levsha.default.renderHtml

val features = Seq("Super-fast", "Off-heap")

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
