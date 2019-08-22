package bebop.ed

import akka.actor.ActorSystem

trait Tran2[A1, A2, R] {
  implicit val arg1Join: Join[A1]
  implicit val arg2Join: Join[A2]
  implicit val retJoin: Join[R]
  implicit val system: ActorSystem

  def connect
    (arg1Cell: Cell[A1],
      arg2Cell: Cell[A2],
      retCell: Cell[R]): Unit

  def apply(arg1Cell: Cell[A1], arg2Cell: Cell[A2]): Cell[R] = {
    val retCell = Cell[R]()
    connect(arg1Cell, arg2Cell, retCell)
    retCell
  }
}
