package bebop.ein.monad_and_all_that

import bebop.ein._

case class Logger[A](value: A, log: Vector[String])

object Logger {
  implicit def loggerMonad = {
    new Monad[Logger] {
      def unit[A](a: A): Logger[A] =
        Logger(a, Vector())

      def flatMap[A, B]
        (logger: Logger[A])
        (f: A => Logger[B]): Logger[B] = {
        val logger2 = f(logger.value)
        Logger(logger2.value, logger.log ++ logger2.log)
      }
    }
  }

  implicit class FunctorExtension[A](logger: Logger[A]) {
    def map[B](f: A => B): Logger[B] = loggerMonad.map(logger)(f)
  }

  implicit class MonadExtension[A](logger: Logger[A]) {
    def flatMap[B](f: A => Logger[B]) =
      loggerMonad.flatMap(logger)(f)
  }
}
