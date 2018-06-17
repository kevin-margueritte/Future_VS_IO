import pl.project13.scala.sbt.JmhPlugin

name := "fun_IO"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "org.typelevel" %% "cats-effect" % "1.0.0-RC2"

enablePlugins(JmhPlugin)
