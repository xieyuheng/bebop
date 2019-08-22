import org.scalatest._
import bebop.ein.util

class utilSpec extends FlatSpec with Matchers {
  "util.const" can "construct constant functions" in {
    assert(util.const(1)("a") == 1)
    assert(util.const("a")(1) == "a")
  }

  "util.id" should "just return its argument" in {
    assert(util.id(1) == 1)
    assert(util.id("a") == "a")
  }
}
