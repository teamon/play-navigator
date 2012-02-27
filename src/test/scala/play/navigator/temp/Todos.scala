// package controllers

// import play.api._
// import play.api.mvc._
// import navigator._

// object Todos extends Controller with PlayResources[Int] {
//   def index = Action { Ok("Todos.index => %s" format Routing.todos.index()) }
//   def `new` = Action { Ok("Todos.new => %s" format Routing.todos.`new`()) }
//   def create = Action { Ok("Todos.create => %s" format Routing.todos.create()) }
//   def show(id: Int) = Action { Ok("Todos.show(%d) => %s" format (id, Routing.todos.show(id))) }
//   def edit(id: Int) = Action { Ok("Todos.edit(%d) => %s" format (id, Routing.todos.edit(id))) }
//   def update(id: Int) = Action { Ok("Todos.update(%d) => %s" format (id, Routing.todos.update(id))) }
//   def delete(id: Int) = Action { Ok("Todos.delete(%d) => %s" format (id, Routing.todos.delete(id))) }
// }
