scalaVersion := "2.11.1"

organization := "net.s_mach"

name := "codetools"

version := "0.1-SNAPSHOT"

scalacOptions ++= Seq("-feature","-unchecked", "-deprecation")

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value