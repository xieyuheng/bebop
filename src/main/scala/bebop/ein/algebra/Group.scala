package bebop.ein

trait Group[A] extends Monoid[A] {
  def inv(x: A): A

  def div(x: A, y: A): A =
    mul(x, inv(y))

  def idInv(x: A): Equal[A] =
    Equal(mul(x, inv(x)), id)
}
