package play.navigator

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object TestResource extends Resources[Int, Unit] {
  def index() {}
  def `new`() {}
  def create() {}
  def show(id: Int){}
  def edit(id: Int){}
  def update(id: Int){}
  def delete(id: Int){}
}

object RoutesDefinition extends Navigator[Unit] {
  import controllers._

  val fun0 = () => ()
  val fun1 = (a: String) => ()
  val fun2 = (a: String, b: String) => ()
  val fun3 = (a: String, b: String, c: String) => ()

  // Basic
  val home  = GET   on root                         to fun0
  val a     = GET   on "a"                          to fun0
  val ab    = GET   on "a" / "b"                    to fun0
  val abc   = GET   on "a" / "b" / "c"              to fun0
  val abcd  = GET   on "a" / "b" / "c" / "d"        to fun0
  val abcde = GET   on "a" / "b" / "c" / "d" / "e"  to fun0

  // Methods
  val mOptions  = OPTIONS on "options"  to fun0
  val mGet      = GET     on "get"      to fun0
  val mHead     = HEAD    on "head"     to fun0
  val mPost     = POST    on "post"     to fun0
  val mPut      = PUT     on "put"      to fun0
  val mDelete   = DELETE  on "delete"   to fun0
  val mTrace    = TRACE   on "trace"    to fun0
  val mConnect  = CONNECT on "connect"  to fun0

  val param1    = GET on "param1" / *         to fun1
  val param2    = GET on "param2" / * / *     to fun2
  val param3    = GET on "param3" / * / * / * to fun3

  // Catches /long/a/b/c/.../z
  var catchall  = GET on "catchall" / ** to fun1

  // Require extension: /ext/{param}.{ext}
  val extjson = GET on "ext" / * as "json" to fun1
  val extxml  = GET on "ext" / * as "xml"  to fun1

  // REST routes
  val res = resources("test-resources", TestResource)

  // Namespace ...
  // namespace("api"){
    // namespace("v1"){
      // GET on "index" to Application.index _
    // }
  // }

  // ... or with reverse routing support
  val api = new Namespace("api"){
    val about = GET on "about" to fun0
    val v2 = new Namespace("v2"){
      val about = GET on "about" to fun0
    }
  }

  // // and back to top-level namespace
  val afternamespace = GET  on "about" to fun0
}

object PlayRoutesDefinition extends PlayNavigator {
  GET on "redirect-me" to redirect("http://google.com")
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

    "params" in {
      param1 === Route1(RoutePath1(GET, List(Static("param1"), *)), fun1)
      param2 === Route2(RoutePath2(GET, List(Static("param2"), *, *)), fun2)
      param3 === Route3(RoutePath3(GET, List(Static("param3"), *, *, *)), fun3)
    }

    "catchall" in {
      catchall === Route1(RoutePath1(GET, List(Static("catchall"), **)), fun1)
    }

    "extensions" in {
      extjson === Route1(RoutePath1(GET, List(Static("ext"), *), Some("json")), fun1)
      extxml  === Route1(RoutePath1(GET, List(Static("ext"), *), Some("xml")), fun1)
    }

    "resource" in {
      res.index.path  === RoutePath0(GET, List(Static("test-resources")))
      res.`new`.path  === RoutePath0(GET, List(Static("test-resources"), Static("new")))
      res.create.path === RoutePath0(POST, List(Static("test-resources")))
      res.show.path   === RoutePath1(GET, List(Static("test-resources"), *))
      res.edit.path   === RoutePath1(GET, List(Static("test-resources"), *, Static("edit")))
      res.update.path === RoutePath1(PUT, List(Static("test-resources"), *))
      res.delete.path === RoutePath1(DELETE, List(Static("test-resources"), *))
    }

    "namespace" in {
      api.about === Route0(RoutePath0(GET, List(Static("api"), Static("about"))), fun0)
      api.v2.about === Route0(RoutePath0(GET, List(Static("api"), Static("v2"), Static("about"))), fun0)
      afternamespace === Route0(RoutePath0(GET, List(Static("about"))), fun0)
    }
  }
}
