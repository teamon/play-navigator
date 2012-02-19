import play.core.Router

object Routes extends Router.Routes with PlayNavigator {
  import controllers._

  GET   on "index"    to Application.index _
  GET   on "about"    to Application.about _
  POST  on "foo"      to Application.about _
  GET   on "show" / * to Application.show
  GET   on "ws"       to Application.ws _
}
