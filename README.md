## Better router for Play Framework 2.0

## Instalation

Add `play-navigator` to your `build.sbt` file

``` scala
resolvers += "teamon.eu repo" at "http://repo.teamon.eu"

libraryDependencies += "eu.teamon" %% "play-navigator" % "0.1.0-SNAPSHOT"
```

Delete `conf/routes` file

Create new file `PROJECT_ROOT/app/Routes.scala`:

``` scala
import play.navigator.PlayNavigator

trait RoutesDefinition extends Router.Routes with PlayNavigator {
    // Your routes definition (see below)
}


// Below lines are very IMPORTANT
package controllers {
    object routes extends RoutesDefinition
}

object Routes extends RoutesDefinition
```

## Routes definition

``` scala
// Basic
val home  = GET   on root       to Application.index
val index = GET   on "index"    to Application.index
val about = GET   on "about"    to Application.about
val foo   = POST  on "foo"      to Application.about
val show  = GET   on "show" / * to Application.show
val ws    = GET   on "ws"       to Application.ws
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
```

`Application` and `Todos` controller used in example

``` scala
// app/controllers/Application.scala
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


// app/controllers/Todos.scala
package controllers

import play.api._
import play.api.mvc._
import navigator._

object Todos extends Controller with PlayResources[Int] {
  def index() = Action { Ok("Todos.index => %s" format routes.todos.index()) }
  def `new`() = Action { Ok("Todos.new => %s" format routes.todos.`new`()) }
  def create() = Action { Ok("Todos.create => %s" format routes.todos.create()) }
  def show(id: Int) = Action { Ok("Todos.show(%d) => %s" format (id, routes.todos.show(id))) }
  def edit(id: Int) = Action { Ok("Todos.edit(%d) => %s" format (id, routes.todos.edit(id))) }
  def update(id: Int) = Action { Ok("Todos.update(%d) => %s" format (id, routes.todos.update(id))) }
  def delete(id: Int) = Action { Ok("Todos.delete(%d) => %s" format (id, routes.todos.delete(id))) }
}

```
