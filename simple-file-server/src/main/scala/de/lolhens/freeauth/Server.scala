package de.lolhens.freeauth

import de.lolhens.freeauth.Server._
import de.lolhens.freeauth.ui.Routes
import monix.eval.Task
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.{Router, ServerBuilder}

class Server {
  private val routes: Routes = new Routes()

  val httpApp: HttpApp[Task] = Router(
    uiRoot.path -> routes.routes
  ).orNotFound

  def serverBuilder(host: String, port: Int): Task[ServerBuilder[Task]] =
    Task.deferAction(implicit scheduler => Task {
      BlazeServerBuilder[Task].bindHttp(port, host).withHttpApp(httpApp)
    })

  def startServer(host: String, port: Int): Task[Unit] =
    serverBuilder(host, port).flatMap(_.resource.use(_ => Task.never))
}

object Server {
  val uiRoot: Uri = Uri.unsafeFromString("/")
}
