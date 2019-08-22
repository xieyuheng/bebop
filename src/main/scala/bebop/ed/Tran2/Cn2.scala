package bebop.ed

import akka.actor.ActorSystem

case class Cn2[A1, A2, R]
  (connector: (Cell[A1], Cell[A2], Cell[R]) => Unit)
  (implicit
    val arg1Join: Join[A1],
    val arg2Join: Join[A2],
    val retJoin: Join[R],
    val system: ActorSystem) extends Tran2[A1, A2, R] {

  def connect
    (arg1Cell: Cell[A1],
      arg2Cell: Cell[A2],
      retCell: Cell[R]): Unit = {
    connector(arg1Cell, arg2Cell, retCell)
  }
}
