package levsha

import levsha.impl.DiffRenderContext.IdCounter

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object IdCounterTest extends utest.TestSuite {

  import utest._

  val tests = this {
    'IdCounter {
      "should increment id on first level" - {
        val counter = IdCounter()
        counter.incId()
        counter.incId()
        counter.incId()
        val res = counter.currentString
        assert(res == "3")
      }
      "should increment id on second level" - {
        val counter = IdCounter()
        counter.incLevel()
        counter.incId()
        counter.incId()
        counter.incId()
        val res = counter.currentString
        assert(res == "0_3")
      }
      "should increment id on third level" - {
        val counter = IdCounter()
        counter.incLevel()
        counter.incLevel()
        counter.incId()
        counter.incId()
        counter.incId()
        val res = counter.currentString
        assert(res == "0_0_3")
      }
      "should successfully decrement level" - {
        val counter = IdCounter()
        counter.incId()
        counter.incLevel()
        counter.incLevel()
        counter.incId()
        counter.incId()
        counter.incId()
        counter.decLevel()
        counter.decLevel()
        val res = counter.currentString
        assert(res == "1")
      }
      "should reset level on decrement" - {
        val counter = IdCounter()
        counter.incLevel()
        counter.incId()
        counter.decLevel()
        counter.incLevel()
        val res = counter.currentString
        assert(res == "0_0")
      }
    }
  }
}

