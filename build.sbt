scalaVersion := "2.11.12"

version := "1.0-SNAPSHOT"

val macWireVersion = "2.2.4"
val macwire = "com.softwaremill.macwire" %% "macros" % macWireVersion % "provided"
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
      name := """playdemo2""",
      libraryDependencies ++= Seq(
          macwire, javaJdbc,
          "com.typesafe.play" %% "play" % "2.5.18",
          "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
          "mysql" % "mysql-connector-java" % "8.0.33",  //database
          "com.typesafe.play" %% "filters-helpers" % "2.5.19"//csrf




      )
  )