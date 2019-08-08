package xieyuheng.bebop

import akka.actor.ActorSystem

case class Ap1[A1, R]
  (applier: Cell[A1] => Cell[R])
  (implicit
    val arg1Lattice: JoinAble[A1],
    val retLattice: JoinAble[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  def connect(arg1Cell: Cell[A1], retCell: Cell[R]): Unit =
    applier(arg1Cell).unify(retCell)

  override def apply(arg1Cell: Cell[A1]): Cell[R] = {
    applier(arg1Cell)
  }
}
