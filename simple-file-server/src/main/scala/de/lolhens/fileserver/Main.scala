package de.lolhens.fileserver

import cats.effect.ExitCode
import monix.eval.{Task, TaskApp}

object Main extends TaskApp {
  private val server: Server = new Server()

  override def run(args: List[String]): Task[ExitCode] =
    for {
      _ <- server.startServer("0.0.0.0", 8080)
    } yield
      ExitCode.Success
}
