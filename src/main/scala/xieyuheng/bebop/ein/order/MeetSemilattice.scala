package xieyuheng.bebop.ein

trait MeetSemilattice[E] extends PartialOrder[E] {
  def meet(a: E, b: E): E

  def meetAssociative(a: E, b: E, c: E): Law =
    Equal(
      meet(a, meet(b, c)),
      meet(meet(a, b), c))

  def meetCommutative(a: E, b: E): Law =
    Equal(
      meet(a, b),
      meet(b, a))

  def meetIdempotent(a: E): Law =
    Equal(meet(a, a), a)

  def pre(a: E, b: E): Boolean = {
    meet(a, b) == a
  }
}
