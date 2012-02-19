package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("Applcation.index")
  }

  def about = Action {
    Ok("Application.about")
  }

  def show(id: Int) = Action {
    Ok("Application.show(%d)" format id)
  }

  def bar(f: Float, b: Boolean, s: String) = Action {
    Ok("Application.bar(%f, %b, %s)" format (f,b,s))
  }

  import play.api.libs.iteratee._

  def ws = WebSocket.using[String] { request =>
    // Log events to the console
    val in = Iteratee.foreach[String](println).mapDone { _ =>
      println("Disconnected")
    }

    // Send a single 'Hello!' message
    val out = Enumerator("Hello!")

    (in, out)
  }
}
