inThisBuild(Seq(
  name := "simple-file-server",
  organization := "de.lolhens",

  scalaVersion := "2.12.8"
))

name := (ThisBuild / name).value

lazy val simpleFileServer = project.in(file("simple-file-server"))
  .settings(
    name := "simple-file-server",
    version := "0.0.0",

    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "io.monix" %% "monix" % "3.0.0",
      "io.circe" %% "circe-core" % "0.11.1",
      "io.circe" %% "circe-generic" % "0.11.1",
      "io.circe" %% "circe-parser" % "0.11.1",
      "org.http4s" %% "http4s-dsl" % "0.20.10",
      "org.http4s" %% "http4s-blaze-server" % "0.20.10",
      "org.http4s" %% "http4s-circe" % "0.20.10",
      "org.http4s" %% "http4s-scalatags" % "0.20.10",
      "com.lihaoyi" %% "scalatags" % "0.7.0"
    )
  )
