organization := "eu.teamon"

name := "play-navigator"

version := "0.5.0"

scalaVersion := "2.10.2"

scalaBinaryVersion := "2.10"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-encoding", "utf8", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeRepo("snapshots")
)

libraryDependencies ++= (
  val play_version = Option(System.getenv("PLAY_VERSION")).getOrElse("2.2.0") 
  Seq(
    "com.typesafe.play" %% "play" % play_version % "compile",
    "com.typesafe.play" %% "play-test" % play_version % "test",
    "org.specs2" %% "specs2" % "2.2.2" % "test"
  )
)
