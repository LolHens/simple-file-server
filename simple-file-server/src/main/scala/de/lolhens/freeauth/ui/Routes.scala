package de.lolhens.freeauth.ui

import java.nio.file.{Files, Path, Paths}

import _root_.scalatags.Text.all._
import monix.eval.Task
import org.http4s._
import org.http4s.dsl.task._
import org.http4s.scalatags._

import scala.collection.JavaConverters._


class Routes {
  val rootPath: Path = Paths.get("files").toAbsolutePath

  def routes: HttpRoutes[Task] = HttpRoutes.of {
    case request@GET -> Root / "files" / path =>
      def listFiles(directory: Path): List[Path] = {
        val newPath = rootPath.resolve(directory).normalize()
        if (newPath.startsWith(rootPath)) {
          Files.list(newPath).iterator().asScala.toList
        } else {
          List.empty
        }
      }

      println(path)
      val files = listFiles(Paths.get(path).normalize())

      Ok(div(
        for (file <- files) yield {
          p(file.toString)
        }
      ))
  }
}
