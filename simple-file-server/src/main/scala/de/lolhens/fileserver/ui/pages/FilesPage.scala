package de.lolhens.fileserver.ui.pages

import java.nio.file.{Files, Path}

import de.lolhens.fileserver.ui.FileStore
import de.lolhens.fileserver.ui.pages.PageTemplate.Page
import org.http4s.Uri
import scalatags.Text
import scalatags.Text.all._

object FilesPage extends BootstrapPageTemplate {

  case class FileType(name: String, iconName: String, extensions: List[String])

  private val fileTypes = List[FileType](
    FileType("Text", "fa-file-text-o", List(
      "txt",
      "conf",
      "json",
      "yml",
      "yaml",
      "toml"
    )),
    FileType("PDF", "fa-file-pdf-o", List(
      "pdf"
    )),
    FileType("Word", "fa-file-word-o", List(
      "doc",
      "docx",
      "rtf"
    )),
    FileType("Excel", "fa-file-excel-o", List(
      "xls",
      "xlsx"
    )),
    FileType("PowerPoint", "fa-file-powerpoint-o", List(
      "ppt",
      "pptx"
    )),
    FileType("Image", "fa-file-image-o", List(
      "bmp",
      "jpg",
      "jpeg",
      "png",
      "gif",
      "svg"
    )),
    FileType("Archive", "fa-file-archive-o", List(
      "zip",
      "gz",
      "7zip",
      "rar"
    )),
    FileType("Audio", "fa-file-audio-o", List(
      "mp3",
      "wav"
    )),
    FileType("Video", "fa-file-video-o", List(
      "mp4",
      "mpeg",
      "flv"
    )),
    FileType("Code", "fa-file-code-o", List(
      "xml",
      "html",
      "js",
      "java",
      "scala",
      "cpp"
    ))
  )

  val regularFileType: FileType = FileType("File", "fa-file-o", List.empty)
  val directoryType: FileType = FileType("Directory", "fa-folder-open-o", List.empty)

  def sizeString(size: Long): String = {
    if (size < 1_000L)
      s"$size B"
    else if (size < 1_000_000L)
      s"${(size / 10).toDouble / 100} KB"
    else if (size < 1_000_000_000L)
      s"${(size / 10_000).toDouble / 100} MB"
    else
      s"${(size / 10_000_000).toDouble / 100} GB"
  }

  def getFileType(isFile: Boolean, extension: Option[String]): FileType = {
    if (isFile)
      extension.flatMap(extension =>
        fileTypes.find(_.extensions.contains(extension))
      ).getOrElse(regularFileType)
    else
      directoryType
  }

  def getFileType(path: Path): FileType = getFileType(
    Files.isRegularFile(path),
    Some(path.getFileName.toString.reverse.takeWhile(_ != '.').reverse).filter(_.nonEmpty)
  )

  def page(uri: Uri, path: Path, files: List[Path]): Text.TypedTag[String] =
    template(Page(uri, title = Some("Files")) {
      div(
        h1(s"Files for /${FileStore.store.showPath(path).get}"),
        table(`class` := "table table-hover",
          thead(
            tr(
              th(scope := "col", "Filename"),
              th(scope := "col", "Type"),
              th(scope := "col", "Size")
            )
          ),
          tbody(
            files.map { file =>
              val fileType = getFileType(file)
              val fileName = FileStore.store.showPath(file).get
              val fileSize = Files.size(file)

              tr(
                td(`class` := "p-0", div(`class` := "stretched-link-container p-2",
                  i(`class` := s"fa ${fileType.iconName}"),
                  span(`class` := "ml-2", a(`class` := "stretched-link", href := "/" + fileName, fileName))
                )),
                td(`class` := "p-0", div(`class` := "stretched-link-container p-2",
                  a(`class` := "stretched-link", href := "/" + fileName),
                  fileType.name
                )),
                td(`class` := "p-0", div(`class` := "stretched-link-container p-2",
                  a(`class` := "stretched-link", href := "/" + fileName),
                  sizeString(fileSize)
                ))
              )
            }
          )
        )
      )
    })
}
