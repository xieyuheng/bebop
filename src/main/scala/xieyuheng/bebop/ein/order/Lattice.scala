package xieyuheng.bebop.ein

trait Lattice[E] extends JoinSemilattice[E] with MeetSemilattice[E] {
  override def pre(a: E, b: E): Boolean =
    super[JoinSemilattice].pre(a, b)
}
