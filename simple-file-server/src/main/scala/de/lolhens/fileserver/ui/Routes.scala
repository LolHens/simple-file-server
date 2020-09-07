package de.lolhens.fileserver.ui

import java.nio.file.{Files, Path, Paths}

import cats.effect.Blocker
import de.lolhens.fileserver.ui.FileStore._
import de.lolhens.fileserver.ui.pages.FilesPage
import monix.eval.Task
import monix.execution.Scheduler
import org.http4s._
import org.http4s.dsl.task._
import org.http4s.headers.{`Content-Length`, `Content-Type`, `Transfer-Encoding`}
import org.http4s.scalatags._

import scala.jdk.CollectionConverters._

class Routes {
  private val ioBlocker = Blocker.liftExecutionContext(Scheduler.io("files"))

  def collapse(path: Path): Path = {
    if (Files.isDirectory(path)) {
      Files.list(path).iterator.asScala.take(2).toList match {
        case List(child) => collapse(child)
        case _ => path
      }
    } else
      path
  }

  def list(path: Path, collapseDirectories: Boolean = true): Iterator[Path] = {
    val filesIterator = Files.list(path).iterator.asScala
    if (collapseDirectories)
      filesIterator.map(collapse)
    else
      filesIterator
  }

  def fileResponse(path: Path, blocker: Blocker): Task[Response[Task]] = {
    implicit val pathEncoder: EntityEncoder[Task, Path] = EntityEncoder.filePathEncoder(blocker)

    for {
      response <- Ok(path)
      contentLength = Files.size(path)
    } yield
      response
        .withContentType(`Content-Type`(MediaType.application.`octet-stream`))
        .removeHeader(`Transfer-Encoding`)
        .putHeaders(`Content-Length`.unsafeFromLong(contentLength))
  }

  val routes: HttpRoutes[Task] = HttpRoutes.of {
    case request@GET -> path =>
      val filePath = store.getPath(path.toString).get

      if (Files.isDirectory(filePath)) {
        val files = list(filePath).toList

        Ok(FilesPage.page(request.uri, filePath, files))
      } else {
        fileResponse(filePath, ioBlocker)
      }
  }
}

case class FileStore(rootPath: Path) {
  def getPath(path: String): Option[Path] = {
    val normalized = Paths.get(path).normalize().toString.replace("\\", "/")
    val relative = normalized.dropWhile(_ == '/')
    val resolvedPath = rootPath.resolve(relative)
    if (!resolvedPath.startsWith(rootPath)) None
    else Some(resolvedPath)
  }

  def showPath(path: Path): Option[String] = {
    if (!path.startsWith(rootPath)) None
    else Some(rootPath.relativize(path).toString.replace("\\", "/"))
  }
}

object FileStore {
  val store: FileStore = FileStore(Paths.get("files"))
}
