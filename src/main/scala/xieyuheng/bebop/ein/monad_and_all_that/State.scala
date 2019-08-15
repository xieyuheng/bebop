package xieyuheng.bebop.ein.monad_and_all_that

import xieyuheng.bebop.ein._

case class State[S, A](run: S => (S, A))

object State {
  implicit def stateMonad[S] = {
    new Monad[State[S, ?]] {
      def unit[A](a: A): State[S, A] =
        State { s => (s, a) }

      def flatMap[A, B]
        (state: State[S, A])
        (f: A => State[S, B]): State[S, B] = State { s => {
          val (s2, a) = state.run(s)
          f(a).run(s2)
        }
      }
    }
  }

  implicit class FunctorExtension[A, S](state: State[S, A]) {
    def map[B](f: A => B): State[S, B] = stateMonad.map(state)(f)
  }

  implicit class MonadExtension[A, S](state: State[S, A]) {
    def flatMap[B](f: A => State[S, B]) =
      stateMonad.flatMap(state)(f)
  }
}
