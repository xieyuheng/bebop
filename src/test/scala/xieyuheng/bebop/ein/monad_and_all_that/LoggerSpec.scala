import org.scalatest._
import xieyuheng.bebop.ein.monad_and_all_that._

class LoggerSpec extends FlatSpec with Matchers {
  "Logger" can "trace function" in {
    def gcd(x: Int, y: Int): Int = {
      if (y == 0) x else gcd(y, x % y)
    }

    def logger[A](x: A, message: String): Logger[A] =
      Logger(x, Vector(message))

    def info(message: String): Logger[Int] =
      Logger(0, Vector(message))

    def gcdLogger(x: Int, y: Int): Logger[Int] = {
      if (y == 0) logger(x, s"Result: ${x}")
      else for {
        _ <- info(s"${x} mod ${y} == ${x % y}")
        result <- gcdLogger(y, x % y)
      } yield result
    }

    // gcdLogger(1071, 462).log.foreach(println)
    // 1071 mod 462 == 147
    // 462 mod 147 == 21
    // 147 mod 21 == 0
    // Result: 21
  }
}
