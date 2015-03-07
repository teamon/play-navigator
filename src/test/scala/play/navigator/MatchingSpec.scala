package play

import play.navigator._
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._

class MatchingSpec extends Specification {
  import RoutesDefinition._

  def route[T](method: String, path: String) = {
    val req = FakeRequest(method, path).asInstanceOf[FakeRequest[T]]
    RoutesDefinition.routes.lift(req).map {
      case action: Action[_] => contentAsString(action.asInstanceOf[Action[T]](req))
    }
  }

  def get(path: String) = route("GET", path)

  "routes" should {
    "match GET /" in { get("/") === Some("index") }
    "match GET /xa/ya" in { get("/xa/ya") === Some("xa & ya") }
    "not match GET /xa/ya/za" in { get("/xa/ya/za") === None }
    "match GET /x/3" in { get("/x/3") === Some("xint = 3") }
    "match GET /x/3.14" in { get("/x/3.14") === Some("xdouble = 3.14") }
    "match GET /x/3.14foo" in { get("/x/3.14foo") === Some("xstring = 3.14foo") }
    "match GET /b/true/False/1/0/YES/No" in { get("/b/true/False/1/0/YES/No") === Some("bool = true false true false true false") }
    "match GET /second/module/foo/bar/42" in { get("/second/module/foo/bar/42") === Some("SecondModule foo/bar/42") }
  }

  "catch all" should {
    "match GET /reallycatchall/1/2/c/d" in { get("/reallycatchall/1/2/c/d") === Some("catchall and more = 1 2 c/d") }
    "match GET /reallycatchall/a/b/c/d/e/f" in { get("/reallycatchall/a/b/c/d/e/f") === Some("catchall = a/b/c/d/e/f") }
  }

  "extension" should {
    "match GET /mext/1.json" in { get("/mext/1.json") === Some("mext json = 1") }
    "match GET /mext/2.xml" in { get("/mext/2.xml") === Some("mext xml = 2") }
    "not match GET /mext/3.css" in { get("/mext/3.css") === None }
  }

  "redirect" should {
    "match GET /redirect-me" in { get("/redirect-me") === Some("") }
  }
}
