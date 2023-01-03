# scala-fast-vect 🐆

Fast, type-safe vector libary for Scala. This library is a port of my Purescript library [`purescript-fast-vect`](https://github.com/sigma-andex/purescript-fast-vect) which are both heavily inspired by [Idris](https://www.idris-lang.org/). A vector is list with its size encoded in the type.


## Usage

```scala
import fastvect.{given, *}
import fastvect.VectOps.{given, *}
import fastvect.list.{given, *}

val zero: Vect[0, String] = Vect.empty[String]
val one: Vect[1, String] = Vect.singleton("a")
val h: String = one.head
val as: Vect[100, String] = Vect.replicate[100, String]("a")
val bs: Vect[200, String] = Vect.replicate[200, String]("b")
val cs: Vect[300, String] = as ++ bs
val ds = cs.drop[99]
val es: String = as.index[99]
val l1: String = Vect.singleton("a").last
val fives: Option[Vect[5, String]] =
  Vect.from[5, String](List("a", "b", "c", "d"))
val xs: Vect[3, String] = (Vect.replicate("a"): Vect[2, String]) :+ "b"
val ys: Vect[4, String] = "c" +: xs
val zs = Vect.from[5, Int](List(1,2,3,4))
```
