organization := "eu.teamon"

name := "play-navigator"

version := "0.5.0"

scalaVersion := "2.10.2"

scalaBinaryVersion := "2.10"

scalacOptions ++= Seq("-Xlint","-deprecation", "-unchecked","-encoding", "utf8", "-feature")

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/repo",
  Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % Option(System.getenv("PLAY_VERSION")).getOrElse("2.2.0-RC2") % "compile",
  "com.typesafe.play" %% "play-test" % Option(System.getenv("PLAY_VERSION")).getOrElse("2.2.0-RC2") % "test",
  "org.specs2" % "specs2_2.10" % "1.14" % "test"
)

publishTo := Some(Resolver.file("local-maven", new File("/Users/teamon/code/maven")))
