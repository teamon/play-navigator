package play.navigator

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object RoutesDefinition extends Navigator[Unit] {
  import controllers._

  val fun0 = () => ()

  // Basic
  val home  = GET   on root                         to fun0
  val a     = GET   on "a"                          to fun0
  val ab    = GET   on "a" / "b"                    to fun0
  val abc   = GET   on "a" / "b" / "c"              to fun0
  val abcd  = GET   on "a" / "b" / "c" / "d"        to fun0
  val abcde = GET   on "a" / "b" / "c" / "d" / "e"  to fun0

  val mOptions  = OPTIONS on "options"  to fun0
  val mGet      = GET     on "get"      to fun0
  val mHead     = HEAD    on "head"     to fun0
  val mPost     = POST    on "post"     to fun0
  val mPut      = PUT     on "put"      to fun0
  val mDelete   = DELETE  on "delete"   to fun0
  val mTrace    = TRACE   on "trace"    to fun0
  val mConnect  = CONNECT on "connect"  to fun0

  // val show  = GET   on "show" / * to Application.show
  // val ws    = GET   on "ws"       to Application.ws _
  // val bar   = GET   on "bar" / * / * / "blah" / * to Application.bar

  // // Catches /long/a/b/c/.../z
  // var long  = GET   on "long" / ** to Application.long

  // // Require extension: /ext/{param}.{ext}
  // GET on "ext" / * as "json" to Application.extJson
  // GET on "ext" / * as "xml"  to Application.extXml

  // // REST routes
  // val todos = resources("todos", Todos)

  // // Namespace ...
  // namespace("api"){
  //   namespace("v1"){
  //     GET on "index" to Application.index _
  //   }
  // }

  // // ... or with reverse routing support
  // val api = new Namespace("api"){
  //   val v2 = new Namespace("v2"){
  //     val about = GET on "about" to Application.about _
  //   }
  // }

  // // and back to top-level namespace
  // GET   on "showalt" / * to Application.show
}

class DSLSpec extends Specification {
  import RoutesDefinition._

  "DSL" should {
    "root path" in {
      home === Route0(RoutePath0(GET, Nil), fun0)
    }

    "static paths" in {
      a === Route0(RoutePath0(GET, List(Static("a"))), fun0)
      ab === Route0(RoutePath0(GET, List(Static("a"), Static("b"))), fun0)
      abc === Route0(RoutePath0(GET, List(Static("a"), Static("b"), Static("c"))), fun0)
      abcd === Route0(RoutePath0(GET, List(Static("a"), Static("b"), Static("c"), Static("d"))), fun0)
      abcde === Route0(RoutePath0(GET, List(Static("a"), Static("b"), Static("c"), Static("d"), Static("e"))), fun0)
    }

    "methods" in {
        mOptions  === Route0(RoutePath0(OPTIONS, List(Static("options"))), fun0)
        mGet      === Route0(RoutePath0(GET, List(Static("get"))), fun0)
        mHead     === Route0(RoutePath0(HEAD, List(Static("head"))), fun0)
        mPost     === Route0(RoutePath0(POST, List(Static("post"))), fun0)
        mPut      === Route0(RoutePath0(PUT, List(Static("put"))), fun0)
        mDelete   === Route0(RoutePath0(DELETE, List(Static("delete"))), fun0)
        mTrace    === Route0(RoutePath0(TRACE, List(Static("trace"))), fun0)
        mConnect  === Route0(RoutePath0(CONNECT, List(Static("connect"))), fun0)
    }
  }
}
