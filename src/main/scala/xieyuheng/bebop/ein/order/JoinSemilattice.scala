package xieyuheng.bebop.ein

trait JoinSemilattice[E] extends PartialOrder[E] {
  def join(a: E, b: E): E

  def joinAssociative(a: E, b: E, c: E): Law =
    Equal(
      join(a, join(b, c)),
      join(join(a, b), c))

  def joinCommutative(a: E, b: E): Law =
    Equal(
      join(a, b),
      join(b, a))

  def joinIdempotent(a: E): Law =
    Equal(join(a, a), a)

  def pre(a: E, b: E): Boolean = {
    join(a, b) == b
  }
}
