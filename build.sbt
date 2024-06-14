scalaVersion := "2.11.12"

version := "1.0-SNAPSHOT"

val macWireVersion = "2.2.4"
val macwire = "com.softwaremill.macwire" %% "macros" % macWireVersion % "provided"
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
      name := """squareRootApi""",
      libraryDependencies ++= Seq(
          macwire, javaJdbc,
          "com.typesafe.play" %% "play" % "2.5.18",
          "com.typesafe.akka" %% "akka-actor" % "2.3.6",
          "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
          "org.scalatest" %% "scalatest" % "3.0.8" % Test,
          "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
          "com.typesafe.akka" %% "akka-testkit" % "2.4.20" % Test

      )
  )