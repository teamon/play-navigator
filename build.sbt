organization := "eu.teamon"

name := "play-navigator"

version := "0.2.1-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-Xlint","-deprecation", "-unchecked","-encoding", "utf8")

resolvers ++= Seq(
  Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "play" %% "play" % "2.0-RC1-SNAPSHOT" % "compile",
  "play" %% "play-test" % "2.0-RC1-SNAPSHOT" % "test",
  "org.specs2" %% "specs2" % "1.7.1" % "test"
)
