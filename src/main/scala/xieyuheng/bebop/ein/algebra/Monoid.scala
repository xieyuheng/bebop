package xieyuheng.bebop.ein

trait Monoid[A] extends Semigroup[A] {
  def id: A

  def idLeft(x: A): Equal[A] =
    Equal(mul(id, x), x)

  def idRight(x: A): Equal[A] =
    Equal(mul(x, id), x)
}
