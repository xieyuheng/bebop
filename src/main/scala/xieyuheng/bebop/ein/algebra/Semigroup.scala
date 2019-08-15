package xieyuheng.bebop.ein

trait Semigroup[A] {
  def mul(x: A, y: A): A

  def mulAssociative(x: A, y: A, z: A): Equal[A] =
    Equal(
      mul(mul(x, y), z),
      mul(x, mul(y, z)))
}
