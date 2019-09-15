package de.lolhens.freeauth.ui

import java.nio.file.{Files, Path, Paths}

import de.lolhens.freeauth.ui.FileStore._
import de.lolhens.freeauth.ui.pages.FilesPage
import monix.eval.Task
import monix.execution.Scheduler
import org.http4s._
import org.http4s.dsl.task._
import org.http4s.headers.{`Content-Length`, `Content-Type`, `Transfer-Encoding`}
import org.http4s.scalatags._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext


class Routes {

  val ioPool = Scheduler.io("files")

  def collapse(path: Path): Path = {
    if (Files.isDirectory(path)) {
      Some(Files.list(path).iterator.asScala.take(2).toList).collect {
        case List(child) => collapse(child)
      }.getOrElse(path)
    } else
      path
  }

  def list(path: Path, collapseDirectories: Boolean = true): Iterator[Path] = {
    val filesIterator = Files.list(path).iterator.asScala
    filesIterator.map(collapse)
  }

  def fileResponse(path: Path, executionContext: ExecutionContext): Task[Response[Task]] = {
    implicit val pathEncoder: EntityEncoder[Task, Path] = EntityEncoder.filePathEncoder(executionContext)

    for {
      response <- Ok(path)
      contentLength = Files.size(path)
    } yield
      response
        .withContentType(`Content-Type`(MediaType.application.`octet-stream`))
        .removeHeader(`Transfer-Encoding`)
        .putHeaders(`Content-Length`.unsafeFromLong(contentLength))
  }

  def routes: HttpRoutes[Task] = HttpRoutes.of {
    case request@GET -> path =>
      val filePath = store.getPath(path.toString).get

      if (Files.isDirectory(filePath)) {

        val files = list(filePath).toList

        Ok(FilesPage.page(request.uri, filePath, files))

        /*Ok(div(
          for (file <- files) yield {
            val fileString = store.showPath(file).get
            p(a(fileString, href := "/" + fileString))
          }
        ))*/
      } else {
        fileResponse(filePath, ioPool)
      }
  }
}

case class FileStore(rootPath: Path) {
  def getPath(path: String): Option[Path] = {
    val normalized = Paths.get(path).normalize().toString.replaceAll("\\\\", "/")
    val relative = if (normalized.startsWith("/")) normalized.drop(1) else normalized
    val resolvedPath = rootPath.resolve(relative)
    if (!resolvedPath.startsWith(rootPath)) None
    else Some(resolvedPath)
  }

  def showPath(path: Path): Option[String] = {
    if (!path.startsWith(rootPath)) None
    else Some(rootPath.relativize(path).toString)
  }
}

object FileStore {
  val store = FileStore(Paths.get("files"))
}
