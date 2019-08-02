organization := "xieyuheng"
name := "bebop"
version := "0.0.1"
scalaVersion := "2.12.8"
lazy val akkaVersion = "2.5.23"
lazy val akkaHttpVersion = "10.1.9"
lazy val slickVersion = "3.3.1"
lazy val pracatVersion = "0.0.1-SNAPSHOT"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  // "-language:implicitConversions",
    "-Ywarn-dead-code",
  // "-Xfatal-warnings",
)

libraryDependencies ++= Seq(
  "xieyuheng" %% "pracat" % pracatVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.slick" %% "slick" % slickVersion,
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "io.spray" %% "spray-json" % "1.3.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
