package de.lolhens.fileserver

import cats.effect.ExitCode
import de.lolhens.fileserver.ui.Routes
import monix.eval.{Task, TaskApp}
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends TaskApp {
  val uiRoot: Uri = Uri.unsafeFromString("/")

  val httpApp: HttpApp[Task] = Router(
    uiRoot.path -> new Routes().routes
  ).orNotFound

  def startServer(host: String, port: Int): Task[Nothing] = Task.deferAction { scheduler =>
    BlazeServerBuilder[Task](scheduler)
      .bindHttp(port, host)
      .withHttpApp(httpApp)
      .resource
      .use(_ => Task.never)
  }

  override def run(args: List[String]): Task[ExitCode] =
    for {
      _ <- startServer("0.0.0.0", 8080)
    } yield
      ExitCode.Success
}
