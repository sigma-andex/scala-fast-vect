package fastvect.list

import fastvect.*

given VectConstructorImpl[List] with
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

given VectImpl[List] with
  extension [T](elem: T) def prependImpl(vect: List[T]): List[T] = elem +: vect

  extension [T](list: List[T])
    override def headImpl: T = list.head
    override def lastImpl: T = list.last
    override def applyImpl(n: Int): T = list.apply(n)

    override def concatImpl(other: List[T]): List[T] = list ++ other

    override def appendImpl(elem: T): List[T] = list :+ elem

    override def takeImpl(n: Int): List[T] = list.take(n)
    override def dropImpl(n: Int): List[T] = list.drop(n)

