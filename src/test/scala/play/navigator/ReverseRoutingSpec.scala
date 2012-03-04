package play

import play.navigator._

import org.specs2.mutable._

import play.api.test._

class ReverseRoutingSpec extends Specification {
  import RoutesDefinition._

  "ReverseRouting" should {
    "root path" in {
      home() === "/"
    }

    "static paths" in {
      a() === "/a"
      ab() === "/a/b"
      abc() === "/a/b/c"
    }

    "params" in {
      param1("x") === "/param1/x"
      param2("x", "y") === "/param2/x/y"
      param3("x", "y", "z") === "/param3/x/y/z"
    }

    "catchall" in {
      catchall === Route1(RouteDef1(GET, List(Static("catchall"), **)), fun1)
    }

    "extensions" in {
      extjson("1") === "/ext/1.json"
      extxml("2")  === "/ext/2.xml"
      extxml("3", ext = Some("css"))  === "/ext/3.css"
      extxml("4", ext = None)  === "/ext/4"
    }

    "resource" in {
      res.index() === "/test-resources"
      res.`new`()  === "/test-resources/new"
      res.create() === "/test-resources"
      res.show(1)   === "/test-resources/1"
      res.edit(2)   === "/test-resources/2/edit"
      res.update(3) === "/test-resources/3"
      res.delete(4) === "/test-resources/4"
    }

    "namespace" in {
      api.about() === "/api/about"
      api.v2.about() === "/api/v2/about"
      afternamespace() === "/about"
    }
  }
}
