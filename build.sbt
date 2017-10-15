name := """play-framework-bootstrap"""
organization := "mv.naeem"
organizationName := "naeem.mv"
version := "1.0.0-alpha1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.json4s" % "json4s-native_2.11" % "3.5.3"
)