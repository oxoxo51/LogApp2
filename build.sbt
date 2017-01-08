name := """LogApp2"""

version := "1.0-SNAPSHOT"

herokuAppName in Compile := "logapp2"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.5.0-1",
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.webjars" % "bootstrap" % "3.3.7"
)