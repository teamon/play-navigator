package play

import scala.language.reflectiveCalls
import play.navigator._
import play.api.test._
import org.specs2.mutable._

class DSLSpec extends Specification {
  import RoutesDefinition._

  "DSL" should {
    "RouteDef" in {
      "a" / "b" === RouteDef0(ANY, Static("a") :: Static("b") :: Nil, None)
      "a" / * === RouteDef1(ANY, Static("a") :: * :: Nil, None)

      (GET on "a" / "b") === RouteDef0(GET, Static("a") :: Static("b") :: Nil, None)
      (POST on "a" / *) === RouteDef1(POST, Static("a") :: * :: Nil, None)

      (GET on "a" / "b" to fun0) === Route0(RouteDef0(GET, Static("a") :: Static("b") :: Nil, None), fun0)
      (GET on "a" / * to fun1) === Route1(RouteDef1(GET, Static("a") :: * :: Nil, None), fun1)
    }

    "root path" in {
      home === Route0(RouteDef0(GET, Nil, None), fun0)
    }

    "static paths" in {
      a === Route0(RouteDef0(GET, List(Static("a")), None), fun0)
      ab === Route0(RouteDef0(GET, List(Static("a"), Static("b")), None), fun0)
      abc === Route0(RouteDef0(GET, List(Static("a"), Static("b"), Static("c")), None), fun0)
      abcd === Route0(RouteDef0(GET, List(Static("a"), Static("b"), Static("c"), Static("d")), None), fun0)
      abcde === Route0(RouteDef0(GET, List(Static("a"), Static("b"), Static("c"), Static("d"), Static("e")), None), fun0)
    }

    "methods" in {
      mOptions  === Route0(RouteDef0(OPTIONS, List(Static("options"))), fun0)
      mGet      === Route0(RouteDef0(GET, List(Static("get"))), fun0)
      mHead     === Route0(RouteDef0(HEAD, List(Static("head"))), fun0)
      mPost     === Route0(RouteDef0(POST, List(Static("post"))), fun0)
      mPut      === Route0(RouteDef0(PUT, List(Static("put"))), fun0)
      mDelete   === Route0(RouteDef0(DELETE, List(Static("delete"))), fun0)
      mTrace    === Route0(RouteDef0(TRACE, List(Static("trace"))), fun0)
      mConnect  === Route0(RouteDef0(CONNECT, List(Static("connect"))), fun0)
    }

    "params" in {
      param1 === Route1(RouteDef1(GET, List(Static("param1"), *)), fun1)
      param2 === Route2(RouteDef2(GET, List(Static("param2"), *, *)), fun2)
      param3 === Route3(RouteDef3(GET, List(Static("param3"), *, *, *)), fun3)
    }

    "catchall" in {
      catchall === Route1(RouteDef1(GET, List(Static("catchall"), **)), fun1)
    }

    "extensions" in {
      extjson === Route1(RouteDef1(GET, List(Static("ext"), *), Some("json")), fun1)
      extxml  === Route1(RouteDef1(GET, List(Static("ext"), *), Some("xml")), fun1)
    }

    "resource" in {
      res.index.routeDef  === RouteDef0(GET, List(Static("test-resources")))
      res.`new`.routeDef  === RouteDef0(GET, List(Static("test-resources"), Static("new")))
      res.create.routeDef === RouteDef0(POST, List(Static("test-resources")))
      res.show.routeDef   === RouteDef1(GET, List(Static("test-resources"), *))
      res.edit.routeDef   === RouteDef1(GET, List(Static("test-resources"), *, Static("edit")))
      res.update.routeDef === RouteDef1(PUT, List(Static("test-resources"), *))
      res.delete.routeDef === RouteDef1(DELETE, List(Static("test-resources"), *))
    }

    "namespace" in {
      api.about === Route0(RouteDef0(GET, List(Static("api"), Static("about"))), fun0)
      api.v2.about === Route0(RouteDef0(GET, List(Static("api"), Static("v2"), Static("about"))), fun0)
      afternamespace === Route0(RouteDef0(GET, List(Static("about"))), fun0)
    }
  }
}
