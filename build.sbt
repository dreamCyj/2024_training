scalaVersion := "2.11.12"

version := "1.0-SNAPSHOT"

//val macWireVersion = "2.2.4"
//val macwire = "com.softwaremill.macwire" %% "macros" % macWireVersion % "provided"
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
      name := """squareRootApi""",
      libraryDependencies ++= Seq(
          javaJdbc,
          "com.google.inject" % "guice" % "3.0",
          "com.typesafe.play" %% "play" % "2.5.18",
          "com.typesafe.akka" %% "akka-actor" % "2.3.6",
          "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
          //"org.scalatest" %% "scalatest" % "3.0.8" % Test
          "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test
          //"com.typesafe.akka" %% "akka-testkit" % "2.4.20" % Test

      )
  )

/*lazy val autoZip = taskKey[Unit]("AutoZip")

autoZip := {
    //任务 in 作用域 .value获取任务结果
    (clean in Compile).value
    (compile in Compile).value
    (dist in Universal).value
}*/
