package levsha.benchmark

import levsha.impl.DiffRenderContext
import org.openjdk.jmh.annotations.Benchmark

import levsha.default.dsl._

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
class DiffRenderContextBenchmark {

  @Benchmark def inferChangesBetweenTwoTrees(): Unit = {
    implicit val renderContext = DiffRenderContext()

    'span('lang /= "en",'class /= "hello",
      'div('name /= "I",
        'form('class /= "cow",'lang /= "ru",
          'div('lang /= "en",'style /= "margin: 10;")
        )
      ),
      'div(
        'div('class /= "cow"),
        'span('class /= "cow")
      ),
      'form('lang /= "ru",'class /= "lol",
        "d",
        'div('lang /= "ru",'class /= "lol")
      ),
      "B",
      'div('lang /= "ru",'class /= "hello"),
      "ex",
      'div('class /= "cow",'lang /= "ru",
        "t"
      ),
      'div('class /= "hello",
        'div('class /= "cow"),
        "i"
      ),
      'p('class /= "lol",
        "y"
      ),
      "w",
      'ul('name /= "Korolev",
        'span('class /= "cow"),
        "l"
      ),
      'input(),
      "uc",
      "bK",
      "Eb",
      'span('class /= "hello",
        'form(),
        'form('class /= "cow",'lang /= "ru")
      ),
      "hh",
      "Br",
      'button('class /= "lol",
        'span()
      ),
      'div(
        'span('class /= "lol")
      ),
      'input('class /= "hello",
        "y",
        'span('class /= "hello")
      ),
      "i",
      'div('class /= "world",'style /= "margin: 10;"),
      'span(
        'form('class /= "cow"),
        'li('class /= "hello")
      ),
      'li('class /= "lol",
        'span('class /= "cow"),
        'p('class /= "hello")
      ),
      'div('class /= "world",
        'div('class /= "lol",'lang /= "ru"),
        'div('class /= "cow")
      )
    )

    renderContext.swap()

    'span('lang /= "en",'class /= "hello",
      'div('name /= "I",
        'form('class /= "cow",'lang /= "ru",
          'span(
            'p('class /= "world",
              'input('class /= "cow",
                'div()
              ),
              'div(),
              'span('class /= "hello"),
              'li(
                'div('class /= "world")
              ),
              "l",
              'ul('class /= "cow",'lang /= "ru",
                'button('class /= "cow")
              ),
              'div('class /= "world",'name /= "cow"),
              "t"
            ),
            'div('name /= "I",
              "r",
              'input('lang /= "en",'class /= "world"),
              "d",
              'input('lang /= "ru",
                'div('class /= "hello",'name /= "Korolev")
              ),
              "n",
              'li('class /= "cow"),
              'div('class /= "hello"),
              'button('class /= "cow",'lang /= "ru",
                'div('lang /= "ru")
              )
            ),
            'div('class /= "world",
              'form('class /= "lol"),
              'div(
                'ul('class /= "lol")
              ),
              "h",
              "d",
              "m"
            ),
            "s",
            'span(
              "mo",
              'div('class /= "lol"),
              'form('name /= "am",'class /= "hello"),
              'div('lang /= "en",
                'div('class /= "hello"),
                'button('class /= "hello")
              )
            ),
            'span('class /= "lol")
          )
        )
      ),
      'div(
        'div('class /= "cow"),
        'span('class /= "cow")
      ),
      'form('lang /= "ru",'class /= "lol",
        "d",
        'div('lang /= "ru",'class /= "lol")
      ),
      "B",
      'div('lang /= "ru",'class /= "hello"),
      "ex",
      'div('class /= "cow",'lang /= "ru",
        "t",
        'div('style /= "padding: 10;",'class /= "cow",
          "z",
          'span(
            'span('class /= "hello",'lang /= "en")
          ),
          'div(),
          "t",
          'span(),
          'div('name /= "Korolev"),
          'div('name /= "cow",
            'div()
          ),
          'span(),
          'div('class /= "world",
            'input('class /= "hello")
          ),
          'div('class /= "world",
            "v"
          ),
          "M",
          'div('class /= "hello",
            "c"
          ),
          "C",
          'div('class /= "lol",
            'form()
          ),
          'input(
            'span('class /= "world")
          ),
          'div(
            "j"
          ),
          'span('name /= "I",
            "l"
          ),
          'input('class /= "hello",
            'div('lang /= "ru")
          ),
          'form('lang /= "en",
            'button()
          ),
          "q",
          'ul('style /= "padding: 10;"),
          "o",
          "x",
          'form(
            "o"
          ),
          'div('class /= "lol",
            'span('class /= "hello")
          ),
          'span('lang /= "en"),
          "n",
          'div('class /= "world"),
          "i",
          "z",
          'div('class /= "world",
            'span()
          ),
          "X",
          "o",
          'input(),
          'div('name /= "Korolev",
            'li('class /= "lol")
          ),
          'li(
            "S"
          ),
          'div('class /= "hello"),
          'button('class /= "lol",
            "d"
          ),
          'span('name /= "Korolev"),
          "l",
          'li(),
          'span('name /= "am",
            "y"
          ),
          'div(
            'span('class /= "world",'style /= "padding: 10;")
          ),
          'div('class /= "lol"),
          "t"
        )
      ),
      'div('class /= "hello",
        'div('class /= "cow",
          "wyxHdunlDkhaufyddeerwmWyzosirpz"
        ),
        "i"
      ),
      'p('class /= "lol",
        "y"
      ),
      "lrywuybamdsljhkicrjifobupmox",
      'ul('name /= "Korolev",
        'span('class /= "cow")
      ),
      'input(
        'li('class /= "cow",
          "H",
          'div('class /= "lol",'lang /= "en",
            'span('class /= "cow",'name /= "I",
              "u"
            ),
            'form('class /= "hello",'lang /= "en",
              'div('name /= "Korolev")
            ),
            'div(
              "x"
            )
          ),
          'div('class /= "hello"),
          'p('class /= "hello"),
          'span(
            'span('class /= "cow",'name /= "I",'lang /= "ru",
              'div('class /= "world")
            )
          ),
          'div('class /= "world",
            "s",
            'li(
              'li()
            ),
            'span()
          ),
          'ul('class /= "lol",
            'div(
              'ul('class /= "world")
            ),
            "o"
          ),
          'div(
            "g",
            "d"
          ),
          'div('class /= "cow",'name /= "Korolev",
            'ul(
              'div()
            )
          ),
          'p('name /= "I",'lang /= "en",'class /= "cow",
            "nu"
          ),
          'button('class /= "world"),
          'ul(
            'span('class /= "world")
          ),
          'form('lang /= "ru",
            'div(
              'div()
            ),
            "W",
            'span('class /= "hello",
              'div('class /= "lol")
            )
          ),
          'div(
            "z",
            'div('class /= "world"),
            'ul(
              'p('name /= "Korolev",'class /= "world")
            )
          ),
          'div('class /= "world",'lang /= "ru")
        )
      ),
      "uc",
      "bK",
      "Eb",
      'span('class /= "hello",
        'form(),
        'form('lang /= "ru")
      ),
      "hh",
      'div('lang /= "en",'class /= "world",
        'div('class /= "world"),
        'div(
          'ul('class /= "cow",'style /= "margin: 10;")
        ),
        'div('class /= "lol",'name /= "Korolev"),
        'span(
          'div('class /= "lol")
        ),
        'form('lang /= "ru",'class /= "lol"),
        "l",
        'span('class /= "cow",
          'li()
        ),
        "u",
        'p('class /= "world",
          "I"
        ),
        'div(),
        'div(
          'div('class /= "world",'name /= "Korolev")
        ),
        'div('lang /= "en",'class /= "hello",
          "g"
        ),
        'div(
          'span('class /= "cow")
        ),
        'form(),
        'span('class /= "cow",
          'div('class /= "world")
        ),
        'div('lang /= "ru",'class /= "cow"),
        "p",
        "h",
        'span('lang /= "en"),
        'span(),
        'div('class /= "world",'name /= "am",
          'li()
        ),
        "e",
        "q",
        'button('class /= "world",
          "t"
        ),
        'span(),
        "q",
        'li('class /= "cow"),
        'span(
          "u"
        ),
        'ul('name /= "Korolev",'class /= "cow",
          'p('class /= "hello")
        ),
        'span('style /= "padding: 10;",'class /= "world",
          'span('class /= "cow")
        ),
        "y",
        "l",
        'div('class /= "world",
          'button('name /= "I",'style /= "margin: 10;",'class /= "cow")
        ),
        'div('class /= "world",'name /= "Korolev"),
        'form('name /= "Korolev",'class /= "lol",
          'div('class /= "lol",'lang /= "ru")
        ),
        'span('class /= "hello"),
        'div('lang /= "ru",'class /= "world",
          'span()
        ),
        'span('class /= "lol",
          'div('name /= "I")
        ),
        'div('name /= "am",'style /= "margin: 10;",'class /= "hello"),
        'span(
          'button('class /= "lol")
        ),
        'div('name /= "Korolev",
          'form('style /= "padding: 10;",'class /= "world",'name /= "I")
        ),
        "z",
        'li('class /= "cow",'name /= "Korolev"),
        "e"
      ),
      'button('class /= "lol",
        'span()
      ),
      'div(
        'span('class /= "lol")
      ),
      'input('class /= "hello",
        "y",
        'span('class /= "hello")
      ),
      "ehbimpwwbvlujjlGykbmouigijgeqdm",
      'div('class /= "world",'style /= "margin: 10;"),
      'span(
        'form('class /= "cow"),
        'li('class /= "hello")
      ),
      'li('class /= "lol",
        'span('class /= "cow"),
        'p('class /= "hello")
      )
    )

    renderContext.diff(DiffRenderContext.DummyChangesPerformer)
  }
}
