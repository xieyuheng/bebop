package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

class Cell[E](implicit lattice: JoinSemilattice[E]) {
  private var content: Option[E] = None

  def add(a: E) = {
    val old = content
    content match {
      case Some(b) =>
        content = Some(lattice.join(a, b))
      case None =>
        content = Some(a)
    }
    if (old != content) {
      ???
    }
  }

  def value: Option[E] = content
}
