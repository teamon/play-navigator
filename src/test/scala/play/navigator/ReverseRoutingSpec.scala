package play

import scala.language.reflectiveCalls
import play.navigator._
import play.api.mvc.Call
import play.api.test._
import org.specs2.mutable._


class ReverseRoutingSpec extends Specification {
  import RoutesDefinition._

  "ReverseRouting" should {
    "root path" in {
      home() === Call("GET", "/")
    }

    "static paths" in {
      a() === Call("GET", "/a")
      ab() === Call("GET", "/a/b")
      abc() === Call("GET", "/a/b/c")
    }

    "params" in {
      param1("x") === Call("GET", "/param1/x")
      param2("x", "y") === Call("GET", "/param2/x/y")
      param3("x", "y", "z") === Call("GET", "/param3/x/y/z")
    }

    "catchall" in {
      catchall === Route1(RouteDef1(GET, List(Static("catchall"), **)), fun1)
      catchall("foo/bar/baz") === Call("GET", "/catchall/foo/bar/baz")
    }

    "extensions" in {
      extjson("1") === Call("GET", "/ext/1.json")
      extxml("2")  === Call("GET", "/ext/2.xml")
      extxml("3", ext = Some("css"))  === Call("GET", "/ext/3.css")
      extxml("4", ext = None)  ===Call("GET",  "/ext/4")
    }

    "resource" in {
      res.index() === Call("GET", "/test-resources")
      res.`new`()  === Call("GET", "/test-resources/new")
      res.create() === Call("POST", "/test-resources")
      res.show(1)   === Call("GET", "/test-resources/1")
      res.edit(2)   === Call("GET", "/test-resources/2/edit")
      res.update(3) === Call("PUT", "/test-resources/3")
      res.delete(4) === Call("DELETE", "/test-resources/4")
    }

    "namespace" in {
      api.about() === Call("GET", "/api/about")
      api.v2.about() === Call("GET", "/api/v2/about")
      afternamespace() === Call("GET", "/about")
    }

    "module" in {
      first.home() === Call("GET", "/first")
      first.foobar(1) === Call("GET", "/first/foo/bar/1")
      second.home() === Call("GET", "/second/module")
      second.foobar(1) === Call("GET", "/second/module/foo/bar/1")
    }
  }
}
