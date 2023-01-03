package fastvect

import scala.compiletime.constValue
import scala.compiletime.ops.int.{S, +, >, >=, -}

import scala.annotation.targetName
import scala.annotation.implicitNotFound

opaque type VectM[M[_], N <: Int, T] = M[T]

opaque type Vect[N <: Int, T] = VectM[List, N, T]

object Vect:
  private[fastvect] def apply[N <: Int, T](underlying: List[T]): Vect[N, T] =
    underlying

end Vect

trait VectConstructorImpl[M[_]]:
  extension (vect: Vect.type)
    def empty[E]: Vect[0, E]
    def singleton[E](elem: E): Vect[1, E]
    def replicate[N <: Int, E](elem: E)(using N: ValueOf[N]): Vect[N, E]
    def from[N <: Int, T](underlying: M[T])(using
        N: ValueOf[N]
    ): Option[Vect[N, T]]

extension [N <: Int, T](vect: Vect[N, T]) {
  private[fastvect] def value: List[T] = vect
}

trait VectImpl[M[_]]:
  extension [T](elem: T) def prependImpl(vect: M[T]): M[T]
  extension [T](vect: M[T])
    def headImpl: T
    def lastImpl: T
    def applyImpl(n: Int): T
    def concatImpl(other: M[T]): M[T]
    def appendImpl(elem: T): M[T]
    def takeImpl(n: Int): M[T]
    def dropImpl(n: Int): M[T]
end VectImpl

object VectOps:
  trait VectOps:
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

  given VectOpsInstance(using
      vectImpl: VectImpl[List],
      vectConstructorImpl: VectConstructorImpl[List]
  ): VectOps with

    extension [T](elem: T)
      @targetName("prepend")
      override infix def +:[N <: Int](vect: Vect[N, T]): Vect[N + 1, T] = Vect(
        elem.prependImpl(vect.value)
      )
    extension [N <: Int, T](left: Vect[N, T])

      override def head(using proof: N > 0 =:= true): T = left.value.headImpl
      override def last(using proof: N > 0 =:= true, I: ValueOf[N - 1]): T =
        left.value.lastImpl

      override def index[I <: Int](using
          proof: N > I =:= true,
          I: ValueOf[I]
      ): T = left.value.applyImpl(I.value)

      @targetName("concat")
      override infix def ++[M <: Int](right: Vect[M, T]): Vect[N + M, T] =
        Vect[N + M, T](left.value.concatImpl(right.value))
      @targetName("append")
      override infix def :+(elem: T): Vect[N + 1, T] = Vect(
        left.value.appendImpl(elem)
      )

      override def take[M <: Int](using
          proof: N >= M =:= true,
          M: ValueOf[M]
      ): Vect[M, T] = Vect(left.value.takeImpl(M.value))

      override def drop[M <: Int](using
          proof: N >= M =:= true,
          M: ValueOf[M]
      ): Vect[N - M, T] = Vect(left.value.dropImpl(M.value))
end VectOps
