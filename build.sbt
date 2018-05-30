name := "ssje"

version := "1.0.0"

val scalaMajorVer = "2.12.3"

scalaVersion := scalaMajorVer
crossScalaVersions := scalaMajorVer +: Seq("2.11.8")

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import"
)

libraryDependencies ++= Seq(
  "io.spray" %% "spray-json" % "1.3.3",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)