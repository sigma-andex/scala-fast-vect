package fastvect.list

import fastvect.*
import fastvect as FV
import scala.annotation.targetName
import scala.compiletime.constValue
import scala.compiletime.ops.int.{S, +, >, >=, -}

opaque type Vect[N <: Int, T] = VectM[List, N, T]

object Vect:
  private[list] def apply[N <: Int, T](underlying: List[T]): Vect[N, T] =
    VectM(underlying)

extension (v: Vect.type)
  def from[N <: Int, T](underlying: List[T])(using
      N: ValueOf[N]
  ): Option[Vect[N, T]] =
    if (N.value == underlying.size)
      Some(Vect(underlying))
    else
      None

extension [Vect[N <: Int, _], N <: Int, T](vect: Vect[N, T])
  private[list] def underlying: List[T] = vect.asInstanceOf

given VectConstruction[Vect.type, Vect] with
  extension (vect: Vect.type)
    def empty[E]: Vect[0, E] = Vect(List.empty)
    def singleton[E](elem: E): Vect[1, E] = Vect(List(elem))
    def replicate[N <: Int, E](elem: E)(using N: ValueOf[N]): Vect[N, E] =
      Vect(List.fill(N.value)(elem))
    def from[N <: Int, T](list: List[T])(using
        N: ValueOf[N]
    ): Option[Vect[N, T]] = if (list.size == N.value)
      Some(Vect(list))
    else
      None

given ListVectOps(using
    vectConstructorImpl: VectConstruction[
      Vect.type,
      Vect
    ]
): VectOps.VectOps[Vect] with

  extension [T](elem: T)
    @targetName("prepend")
    override infix def +:[N <: Int](vect: Vect[N, T]): Vect[N + 1, T] = Vect(
      elem +: vect.underlying
    )
  extension [N <: Int, T](left: Vect[N, T])

    override def head(using proof: N > 0 =:= true): T = left.underlying.head
    override def last(using proof: N > 0 =:= true, I: ValueOf[N - 1]): T =
      left.underlying.last

    override def index[I <: Int](using
        proof: N > I =:= true,
        I: ValueOf[I]
    ): T = left.underlying.apply(I.value)

    @targetName("concat")
    override infix def ++[M <: Int](right: Vect[M, T]): Vect[N + M, T] =
      Vect[N + M, T](left.underlying ++ right.underlying)
    @targetName("append")
    override infix def :+(elem: T): Vect[N + 1, T] = Vect(
      left.underlying :+ elem
    )

    override def take[M <: Int](using
        proof: N >= M =:= true,
        M: ValueOf[M]
    ): Vect[M, T] = Vect(left.underlying.take(M.value))

    override def drop[M <: Int](using
        proof: N >= M =:= true,
        M: ValueOf[M]
    ): Vect[N - M, T] = Vect(left.underlying.drop(M.value))
