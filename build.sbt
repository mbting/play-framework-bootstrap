name := """play-framework-bootstrap"""
organization := "mv.naeem"
organizationName := "naeem.mv"
version := "1.0.0-alpha1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  guice
)