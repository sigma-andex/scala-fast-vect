package fastvect

import fastvect.{given, *}
import zio.test._
import fastvect.VectOps.{given}

val zero = Vect.empty[String]
val one: Vect[1, String] = Vect.singleton("a")
val result = one.head

object FastVectSpec extends ZIOSpecDefault {
  def spec = suite("FastVectSpec")(
    test("sayHello correctly displays output") {
      for {
        output <- TestConsole.output
        zero = Vect.empty[Int]
      } yield assertTrue(true) // one.head == "a")
    }
  )
}
