package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
object IdBuilderTest extends utest.TestSuite {

  import utest._

  val tests = this {
    test("IdCounter") - {
      test("should increment id on first level") - {
        val counter = IdBuilder()
        counter.incId()
        counter.incId()
        counter.incId()
        val res = counter.mkString
        assert(res == "3")
      }
      test("should increment id on second level") - {
        val counter = IdBuilder()
        counter.incLevel()
        counter.incId()
        counter.incId()
        counter.incId()
        val res = counter.mkString
        assert(res == "0_3")
      }
      test("should increment id on third level") - {
        val counter = IdBuilder()
        counter.incLevel()
        counter.incLevel()
        counter.incId()
        counter.incId()
        counter.incId()
        val res = counter.mkString
        assert(res == "0_0_3")
      }
      test("should successfully decrement level") - {
        val counter = IdBuilder()
        counter.incId()
        counter.incLevel()
        counter.incLevel()
        counter.incId()
        counter.incId()
        counter.incId()
        counter.decLevel()
        counter.decLevel()
        val res = counter.mkString
        assert(res == "1")
      }
      test("should reset level on decrement") - {
        val counter = IdBuilder()
        counter.incLevel()
        counter.incId()
        counter.decLevel()
        counter.incLevel()
        val res = counter.mkString
        assert(res == "0_0")
      }
    }
  }
}

