name := """LogApp2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" % "bootstrap" % "4.0.0-alpha.2",
  "org.webjars" %% "webjars-play" % "2.4.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)
