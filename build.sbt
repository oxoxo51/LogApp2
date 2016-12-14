name := """LogApp2"""

version := "1.0-SNAPSHOT"

herokuAppName in Compile := "logapp2"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "4.0.0-alpha.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
)