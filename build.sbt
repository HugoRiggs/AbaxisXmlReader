libraryDependencies ++= {
    Seq(
      "org.scalactic" %% "scalactic" % "3.0.0",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.jsoup" % "jsoup" % "1.8.3",
      "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2",
      "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"
    )
}

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.12.1",
  test in assembly := {}
)




lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    assemblyJarName in assembly := "AbaxisXmlReader.jar",
    mainClass in assembly := Some("gui.SwingWrapper"),
    fullClasspath in assembly := (fullClasspath in Compile).value
  )

