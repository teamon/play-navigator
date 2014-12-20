organization := "eu.teamon"

name := "play-navigator"

version := "0.5.1-SNAPSHOT"

scalaVersion := "2.11.4"

scalaBinaryVersion := "2.11"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-encoding", "utf8", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeRepo("snapshots")
)

val play_version = Option(System.getenv("PLAY_VERSION")).getOrElse("2.3.7")

libraryDependencies ++= (
  Seq(
    "com.typesafe.play" %% "play" % play_version % "compile",
    "com.typesafe.play" %% "play-test" % play_version % "test",
    "org.specs2" %% "specs2-core" % "2.4.15" % "test"
  )
)
