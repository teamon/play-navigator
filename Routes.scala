import play.core.Router
import navigator.PlayNavigator

trait RoutesDefinition extends Router.Routes with PlayNavigator {
  import controllers._

  val home  = GET   on root       to Application.index _
  val index = GET   on "index"    to Application.index _
  val about = GET   on "about"    to Application.about _
  val foo   = POST  on "foo"      to Application.about _
  val show  = GET   on "show" / * to Application.show
  val ws    = GET   on "ws"       to Application.ws _
  val bar   = GET   on "bar" / * / * / "blah" / * to Application.bar
  var long  = GET   on "long" / ** to Application.long

  GET on "ext" / * as "json" to Application.extJson
  GET on "ext" / * as "xml"  to Application.extXml

  val todos = resources("todos", Todos)

  namespace("api"){
    namespace("v1"){
      GET on "index" to Application.index _
    }
  }
}

package controllers {
  object Routing extends RoutesDefinition
}

object Routes extends RoutesDefinition
