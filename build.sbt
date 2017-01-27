lazy val root = (project in file(".")).
  settings(
    name := "food-eviews",
    version := "1.0",
    scalaVersion := "2.11.8",
    mainClass in assembly := Some("foodreviews.Main"),
    assemblyJarName in assembly := "reviews.jar"
  )
