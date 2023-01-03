package fastvect

import scala.compiletime.constValue
import scala.compiletime.ops.int.{S, +, >, >=, -}

import scala.annotation.targetName
import scala.annotation.implicitNotFound

trait VectConstruction[V, M[N <: Int, E]]:
  extension (vect: V)
    def empty[E]: M[0, E]
    def singleton[E](elem: E): M[1, E]
    def replicate[N <: Int, E](elem: E)(using N: ValueOf[N]): M[N, E]

// object VectOps:
trait VectOps[Vect[N <: Int, E]]:
  extension [T](elem: T)
    @targetName("prepend")
    infix def +:[N <: Int](vect: Vect[N, T]): Vect[N + 1, T]
  extension [N <: Int, T](left: Vect[N, T])

    def head(using proof: N > 0 =:= true): T
    def last(using proof: N > 0 =:= true, I: ValueOf[N - 1]): T
    def index[I <: Int](using proof: N > I =:= true, I: ValueOf[I]): T

    @targetName("concat")
    infix def ++[M <: Int](right: Vect[M, T]): Vect[N + M, T]
    @targetName("append")
    infix def :+(elem: T): Vect[N + 1, T]
    def take[M <: Int](using
        proof: N >= M =:= true,
        M: ValueOf[M]
    ): Vect[M, T]
    def drop[M <: Int](using
        proof: N >= M =:= true,
        M: ValueOf[M]
    ): Vect[N - M, T]

    def size(using N: ValueOf[N]): Int = N.value
