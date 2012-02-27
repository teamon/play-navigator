package controllers

import play.api.mvc._

object Application extends Controller {

  def index(): Action[_] = Action {
    Ok("Applcation.index => " + routes.index())
  }

  def about(): Action[_] = Action {
    Ok("Application.about => " + routes.about() + " or " + routes.api.v2.about())
  }

  def show(id: Int): Action[_] = Action {
    Ok("Application.show(%d) => %s" format (id, routes.show(id)))
  }

  def bar(f: Float, b: Boolean, s: String): Action[_] = Action {
    Ok("Application.bar(%f, %b, %s) => %s" format (f, b, s, routes.bar(f,b,s)))
  }

  def long(path: String) = Action {
    Ok("Application.long(%s)" format path)
  }

  def extJson(id: Int) = Action { Ok("Application.extJson(%d)" format id) }
  def extXml(id: String) = Action { Ok("Application.extXml(%s)" format id) }

  import play.api.libs.iteratee._

  def ws() = WebSocket.using[String] { request =>
    val in = Iteratee.foreach[String](println).mapDone { _ =>
      println("Disconnected")
    }

    val out = Enumerator("Hello!")

    (in, out)
  }
}
