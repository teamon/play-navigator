package controllers

import play.api._
import play.api.mvc._
import navigator._

object Todos extends Controller with PlayResources[Int] {
  def index = Action { Ok("Todos.index") }
  def `new` = Action { Ok("Todos.new") }
  def create = Action { Ok("Todos.create") }
  def show(id: Int) = Action { Ok("Todos.show(%d)" format id) }
  def edit(id: Int) = Action { Ok("Todos.edit(%d)" format id) }
  def update(id: Int) = Action { Ok("Todos.update(%d)" format id) }
  def delete(id: Int) = Action { Ok("Todos.delete(%d)" format id) }
}
