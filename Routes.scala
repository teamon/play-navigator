import play.core.Router
import navigator.PlayNavigator

trait RoutesDefinition extends Router.Routes with PlayNavigator {
  import controllers._

  // Basic
  val home  = GET   on root       to Application.index _
  val index = GET   on "index"    to Application.index _
  val about = GET   on "about"    to Application.about _
  val foo   = POST  on "foo"      to Application.about _
  val show  = GET   on "show" / * to Application.show
  val ws    = GET   on "ws"       to Application.ws _
  val bar   = GET   on "bar" / * / * / "blah" / * to Application.bar

  // Catches /long/a/b/c/.../z
  var long  = GET   on "long" / ** to Application.long

  // Require extension: /ext/{param}.{ext}
  GET on "ext" / * as "json" to Application.extJson
  GET on "ext" / * as "xml"  to Application.extXml

  // REST routes
  val todos = resources("todos", Todos)

  // Namespace ...
  namespace("api"){
    namespace("v1"){
      GET on "index" to Application.index _
    }
  }

  // ... or with reverse routing support
  val api = new Namespace("api"){
    val v2 = new Namespace("v2"){
      val about = GET on "about" to Application.about _
    }
  }

  // and back to top-level namespace
  GET   on "showalt" / * to Application.show
}

package controllers {
  object Routing extends RoutesDefinition
}

object Routes extends RoutesDefinition
