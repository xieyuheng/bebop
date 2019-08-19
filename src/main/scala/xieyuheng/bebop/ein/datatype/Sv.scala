import xieyuheng.bebop.ein._

case class Sv[S, A](run: S => (S, A))

object Sv {
  implicit def svMonad[S] = {
    new Monad[Sv[S, ?]] {
      def unit[A](a: A): Sv[S, A] =
        Sv { s => (s, a) }

      def flatMap[A, B]
        (sv: Sv[S, A])
        (f: A => Sv[S, B]): Sv[S, B] = Sv { s => {
          val (s2, a) = sv.run(s)
          f(a).run(s2)
        }
      }
    }
  }

  implicit class FunctorExtension[A, S](sv: Sv[S, A]) {
    def map[B](f: A => B): Sv[S, B] = svMonad.map(sv)(f)
  }

  implicit class MonadExtension[A, S](sv: Sv[S, A]) {
    def flatMap[B](f: A => Sv[S, B]) =
      svMonad.flatMap(sv)(f)
  }
}
