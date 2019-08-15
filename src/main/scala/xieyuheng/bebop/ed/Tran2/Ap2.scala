package xieyuheng.bebop.ed

import akka.actor.ActorSystem

case class Ap2[A1, A2, R]
  (applier: (Cell[A1], Cell[A2]) => Cell[R])
  (implicit
    val arg1Join: Join[A1],
    val arg2Join: Join[A2],
    val retJoin: Join[R],
    val system: ActorSystem) extends Tran2[A1, A2, R] {

  def connect(arg1Cell: Cell[A1], arg2Cell: Cell[A2], retCell: Cell[R]): Unit =
    applier(arg1Cell, arg2Cell).unify(retCell)

  override def apply(arg1Cell: Cell[A1], arg2Cell: Cell[A2]): Cell[R] = {
    applier(arg1Cell, arg2Cell)
  }
}
