package navigator

import play.api.mvc._

trait Navigator[Out] {
  val navigatorRoutes = new collection.mutable.ListBuffer[Route]
  def addRoute[T <: Route](route: T): T = {
    navigatorRoutes += route
    route
  }

  type In = Array[String]

  sealed trait PathElem
  case class Static(name: String) extends PathElem {
    override def toString = name
  }
  case object * extends PathElem

  sealed trait Method {
    def on[T](path: RoutePath[T]): T = path.withMethod(this)
    def on(name: String) = RoutePath0(this, Static(name) :: Nil)
  }

  case object ANY extends Method

  // http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
  case object OPTIONS extends Method
  case object GET extends Method
  case object HEAD extends Method
  case object POST extends Method
  case object PUT extends Method
  case object DELETE extends Method
  case object TRACE extends Method
  case object CONNECT extends Method


  trait BasicRoutePath {
    def parts: List[PathElem]
    def method: Method

    def variableIndices = parts.zipWithIndex.collect { case (e,i) if e == * => i }
    def length = parts.length

    override def toString = method.toString + "\t/" + parts.mkString("/")
  }

  sealed trait RoutePath[Self] extends BasicRoutePath {
    def withMethod(method: Method): Self
  }

  case class RoutePath0(method: Method, parts: List[PathElem]) extends RoutePath[RoutePath0] {
    def /(static: Static) = RoutePath0(method, parts :+ static)
    def /(p: *.type) = RoutePath1(method, parts :+ p)
    def to(f0: () => Out) = addRoute(Route0(this, f0))
    def withMethod(method: Method) = RoutePath0(method, parts)
  }

  case class RoutePath1(method: Method, parts: List[PathElem]) extends RoutePath[RoutePath1] {
    def /(static: Static) = RoutePath1(method, parts :+ static)
    def /(p: *.type) = RoutePath2(method, parts :+ p)
    def to[A : ParamMatcher : Manifest](f1: A => Out) = addRoute(Route1(this, f1))
    def withMethod(method: Method) = RoutePath1(method, parts)
  }

  case class RoutePath2(method: Method, parts: List[PathElem]) extends RoutePath[RoutePath2] {
    def /(static: Static) = RoutePath2(method, parts :+ static)
    def to[A : ParamMatcher : Manifest, B : ParamMatcher : Manifest](f2: (A, B) => Out) = addRoute(Route2(this, f2))
    def withMethod(method: Method) = RoutePath2(method, parts)
  }

  implicit def stringToRoutePath0(name: String) = RoutePath0(ANY, Static(name) :: Nil)
  implicit def asterixToRoutePath1(ast: *.type) = RoutePath1(ANY, ast :: Nil)
  implicit def stringToStatic(name: String) = Static(name)

  trait ParamMatcher[T]{
    def unapply(s: String): Option[T]
  }

  def silent[T](f: => T) = try { Some(f) } catch { case _ => None }
  implicit val IntParamMatcher = new ParamMatcher[Int] { def unapply(s: String) = silent(s.toInt) }
  implicit val LongParamMatcher = new ParamMatcher[Long] { def unapply(s: String) = silent(s.toLong) }
  implicit val FloatParamMatcher = new ParamMatcher[Float] { def unapply(s: String) = silent(s.toFloat) }
  implicit val DoubleParamMatcher = new ParamMatcher[Double] { def unapply(s: String) = silent(s.toDouble) }
  implicit val StringParamMatcher = new ParamMatcher[String] { def unapply(s: String) = Some(s) }
  implicit val BooleanParamMatcher = new ParamMatcher[Boolean] {
    def unapply(s: String) = s match {
      case "1" | "true" => Some(true)
      case "0" | "false" => Some(false)
      case _ => None
    }
  }

  object Resolver {
    def resolvePath0(parts: List[PathElem], in: In, fun: () => Out): Option[() => Out] = {
      if(in.length == parts.length && parts.zipWithIndex.forall {
          case (elem, i) => elem match {
            case * => true
            case Static(name) => name == in(i)
          }
      }) Some(fun)
      else None
    }

    def resolvePath1[A : ParamMatcher](parts: List[PathElem], in: In, fun: A => Out): Option[() => Out] = {
      val pm1 = implicitly[ParamMatcher[A]]
      (in.headOption, parts) match {
        case (Some(first), Static(name) :: rest) if name == first => resolvePath1(rest, in.drop(1), fun)
        case (Some(pm1(a)), * :: rest) => resolvePath0(rest, in.drop(1), () => fun(a))
        case _ => None
      }
    }

    def resolvePath2[A : ParamMatcher, B : ParamMatcher](parts: List[PathElem], in: In, fun: (A,B) => Out): Option[() => Out] = {
      val pm1 = implicitly[ParamMatcher[A]]
      (in.headOption, parts) match {
        case (Some(first), Static(name) :: rest) if name == first => resolvePath2(rest, in.drop(1), fun)
        case (Some(pm1(a)), * :: rest) => resolvePath1(rest, in.drop(1), (b: B) => fun(a,b))
        case _ => None
      }
    }
  }

  sealed trait Route {
    def path: BasicRoutePath
    def apply(method: String, parts: Array[String]) = {
      if(path.method.toString == method) matchPath(parts)
      else None
    }
    def args: List[scala.reflect.Manifest[_]]
    def matchPath(in: In): Option[() => Out]
  }

  case class Route0(path: RoutePath0, fun: () => Out) extends Route {
    def matchPath(in: In) = Resolver.resolvePath0(path.parts, in, fun)
    def args = Nil
  }

  case class Route1[A : ParamMatcher : Manifest](path: RoutePath1, fun: A => Out) extends Route {
    def matchPath(in: In) = Resolver.resolvePath1(path.parts, in, fun)
    def args = manifest[A] :: Nil
  }

  case class Route2[A : ParamMatcher : Manifest, B : ParamMatcher : Manifest](path: RoutePath2, fun: (A,B) => Out) extends Route {
    def matchPath(in: In) = Resolver.resolvePath2(path.parts, in, fun)
    def args = manifest[A] :: manifest[B] :: Nil
  }

  lazy val _documentation = navigatorRoutes.map { route =>
    (route.path.method.toString, route.path.parts.mkString("/", "/", ""), route.args.mkString(", "))
  }

  def resources[T : ParamMatcher : Manifest](name: String, controller: Resources[T, Out]) = {
    GET     on name               to controller.index _
    GET     on name / "new"       to controller.`new` _
    POST    on name               to controller.create _
    GET     on name / *           to controller.show _
    GET     on name / * / "edit"  to controller.edit _
    PUT     on name / *           to controller.update _
    DELETE  on name / *           to controller.delete _
  }
}

trait Resources[T, Out] {
  def index: Out
  def `new`: Out
  def create: Out
  def show(id: T): Out
  def edit(id: T): Out
  def update(id: T): Out
  def delete(id: T): Out
}

trait PlayResources[T] extends Resources[T, Handler]

trait PlayNavigator extends Navigator[Handler] {

  def documentation = _documentation

  def routes = new PartialFunction[RequestHeader, Handler] {
    private var _lastHandler: () => Handler = null // this one sucks a lot

    def isDefinedAt(req: RequestHeader) = {
      val parts = req.path.split("/").dropWhile(_ == "")
      navigatorRoutes.view.map(_(req.method, parts)).collectFirst { case Some(e) => e } match {
        case Some(handler) =>
          _lastHandler = handler
          true
        case None =>
          false
      }
    }

    def apply(req: RequestHeader) = _lastHandler()
  }
}

