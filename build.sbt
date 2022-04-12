import pl.project13.scala.sbt.JmhPlugin

name := "fun_IO"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.11"

enablePlugins(JmhPlugin)
