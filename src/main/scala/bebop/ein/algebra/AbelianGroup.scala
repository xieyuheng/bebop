package bebop.ein

trait AbelianGroup[A] extends Group[A] {
  def add(x: A, y: A): A = mul(x, y)
  def sub(x: A, y: A): A = div(x, y)
  def neg(x: A): A = inv(x)

  def addCommutative(x: A, y: A): Law =
    Equal(add (x, y), add (y, x))
}
