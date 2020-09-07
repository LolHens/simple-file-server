inThisBuild(Seq(
  name := "simple-file-server",
  organization := "de.lolhens",

  scalaVersion := "2.13.3",

  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
))

name := (ThisBuild / name).value

lazy val simpleFileServer = project.in(file("simple-file-server"))
  .settings(
    name := "simple-file-server",
    version := "0.0.0",

    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "io.monix" %% "monix" % "3.2.2",
      "io.circe" %% "circe-core" % "0.13.0",
      "io.circe" %% "circe-generic" % "0.13.0",
      "io.circe" %% "circe-parser" % "0.13.0",
      "org.http4s" %% "http4s-dsl" % "0.21.7",
      "org.http4s" %% "http4s-blaze-server" % "0.21.7",
      "org.http4s" %% "http4s-circe" % "0.21.7",
      "org.http4s" %% "http4s-scalatags" % "0.21.7",
      "com.lihaoyi" %% "scalatags" % "0.9.1"
    )
  )
