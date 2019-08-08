package xieyuheng.bebop

import akka.actor.ActorSystem

case class Ap2[A1, A2, R]
  (applier: (Cell[A1], Cell[A2]) => Cell[R])
  (implicit
    val arg1Lattice: JoinAble[A1],
    val arg2Lattice: JoinAble[A2],
    val retLattice: JoinAble[R],
    val system: ActorSystem) extends Tran2[A1, A2, R] {

  def connect(arg1Cell: Cell[A1], arg2Cell: Cell[A2], retCell: Cell[R]): Unit =
    applier(arg1Cell, arg2Cell).unify(retCell)

  override def apply(arg1Cell: Cell[A1], arg2Cell: Cell[A2]): Cell[R] = {
    applier(arg1Cell, arg2Cell)
  }
}
