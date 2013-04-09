organization := "eu.teamon"

name := "play-navigator"

version := "0.4.0"

scalaVersion := "2.10.0"

scalaBinaryVersion := "2.10"

scalacOptions ++= Seq("-Xlint","-deprecation", "-unchecked","-encoding", "utf8", "-feature")

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/repo",
  Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "play" %% "play" % Option(System.getenv("PLAY_VERSION")).getOrElse("2.1.0") % "compile",
  "play" %% "play-test" % Option(System.getenv("PLAY_VERSION")).getOrElse("2.1.0") % "test",
  "org.specs2" % "specs2_2.10" % "1.14" % "test"
)

publishTo := Some(Resolver.file("local-maven", new File("/Users/teamon/code/maven")))
