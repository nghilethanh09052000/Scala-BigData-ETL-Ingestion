package example

import org.scalatest.funsuite.AnyFunSuite

class HelloSpec extends AnyFunSuite {
  test("say hello") {
    assert(Hello.greeting == "hello")
  }
}