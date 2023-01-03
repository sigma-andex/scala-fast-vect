package fastvect

opaque type VectM[M[_], N <: Int, T] = M[T]

object VectM:
   private[fastvect] def apply[M[_], N <: Int, T](underlying: M[T]): VectM[M, N, T] = underlying

extension [M[_], N <: Int, T](vect: VectM[M, N, T]) {
  private[fastvect] def value: M[T] = vect
}
