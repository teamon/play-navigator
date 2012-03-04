package play

import play.navigator._
import play.api.mvc._

object TestResource extends PlayResourcesController[Int] {
  def index() = Action { Ok("index") }
  def `new`() = Action { Ok("new") }
  def create() = Action { Ok("create") }
  def show(id: Int) = Action { Ok("show") }
  def edit(id: Int) = Action { Ok("edit") }
  def update(id: Int) = Action { Ok("update") }
  def delete(id: Int) = Action { Ok("delete") }
}

case class FirstModule(parent: PlayNavigator) extends PlayModule(parent) with Controller {
  val home = GET on root to (() => Action { Ok("FirstModule index") } )
  val foobar = GET on "foo" / "bar" / * to ((i: Int) => Action { Ok("FirstModule foo/bar/" + i) } )
}

case class SecondModule(parent: PlayNavigator) extends PlayModule(parent) with Controller {
  val home = GET on root to (() => Action { Ok("SecondModule index") } )
  val foobar = GET on "foo" / "bar" / * to ((i: Int) => Action { Ok("SecondModule foo/bar/" + i) } )
}

object RoutesDefinition extends PlayNavigator with Controller {
  // import controllers._

  val first = "first" --> FirstModule
  val second = "second" / "module" --> SecondModule

  val fun0 = () => Action { Ok("index") }
  val fun1 = (a: String) => Action { Ok("index") }
  val fun2 = (a: String, b: String) => Action { Ok("index") }
  val fun3 = (a: String, b: String, c: String) => Action { Ok("index") }

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
  GET on "reallycatchall" / * / * / ** to ((a: Int, b: Int, s: String) => Action { Ok("catchall and more = " + a + " " + b + " " + s) })
  GET on "reallycatchall" / ** to ((s: String) => Action { Ok("catchall = " + s) })

  // Require extension: /ext/{param}.{ext}
  val extjson = GET on "ext" / * as "json" to fun1
  val extxml  = GET on "ext" / * as "xml"  to fun1

  // Redirect
  GET on "redirect-me" to redirect("http://google.com")


  GET on "xa" / "ya" to (() => Action { Ok("xa & ya") })
  GET on "x" / * to ((x: Int) => Action { Ok("xint = " + x) })
  GET on "x" / * to ((x: Double) => Action { Ok("xdouble = " + x) })
  GET on "x" / * to ((x: String) => Action { Ok("xstring = " + x) })
  GET on "mext" / * as "json" to ((x: Int) => Action { Ok("mext json = " + x) })
  GET on "mext" / * as "xml"  to ((x: Int) => Action { Ok("mext xml = " + x) })
  GET on "b" / * / * / * / * / * / * to (
    (a: Boolean, b: Boolean, c: Boolean, d: Boolean, e: Boolean, f: Boolean) =>
      Action { Ok("bool = " + List(a,b,c,d,e,f).mkString(" ")) }
  )



  // REST routes
  val res = resources("test-resources", TestResource)

  // Namespace ...
  namespace("api"){
    namespace("v1"){
      GET on "index" to fun0
    }
  }

  // ... or with reverse routing support
  val api = new Namespace("api"){
    val about = GET on "about" to fun0
    val v2 = new Namespace("v2"){
      val about = GET on "about" to fun0
    }
  }

  // and back to top-level namespace
  val afternamespace = GET  on "about" to fun0
// }

// object PlayRoutesDefinition extends PlayNavigator {
//   GET on "redirect-me" to redirect("http://google.com")
}

