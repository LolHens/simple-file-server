package de.lolhens.fileserver.ui.pages

import de.lolhens.fileserver.ui.AdditionalTags
import de.lolhens.fileserver.ui.pages.PageTemplate.Page
import org.http4s.Uri
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait PageTemplate extends AdditionalTags {
  protected def template(page: Page): TypedTag[String]
}

object PageTemplate extends AdditionalTags {

  case class Page(uri: Uri,
                  title: Option[String] = None,
                  stylesheets: List[Stylesheet] = List.empty,
                  javascripts: List[Javascript] = List.empty)
                 (val content: Modifier*) {
    def withUri(uri: Uri): Page =
      Page(uri, title, stylesheets, javascripts)(content: _*)

    def withTitle(title: Option[String]): Page =
      Page(uri, title, stylesheets, javascripts)(content: _*)

    def withStylesheets(stylesheets: List[Stylesheet]): Page =
      Page(uri, title, stylesheets, javascripts)(content: _*)

    def withJavascripts(javascripts: List[Javascript]): Page =
      Page(uri, title, stylesheets, javascripts)(content: _*)

    def withContent(content: Modifier*): Page =
      Page(uri, title, stylesheets, javascripts)(content: _*)

    def mapUri(f: Uri => Uri): Page = withUri(f(uri))

    def mapTitle(f: Option[String] => Option[String]): Page = withTitle(f(title))

    def mapStylesheets(f: List[Stylesheet] => List[Stylesheet]): Page = withStylesheets(f(stylesheets))

    def mapJavascripts(f: List[Javascript] => List[Javascript]): Page = withJavascripts(f(javascripts))

    def mapContent(f: Seq[Modifier] => Seq[Modifier]): Page = withContent(f(content): _*)
  }

  case class Stylesheet(uri: Uri,
                        integrity: Option[String] = None,
                        crossorigin: Option[String] = Some("anonymous")) {
    def render: TypedTag[String] =
      link(
        rel := "stylesheet",
        href := uri,
        integrity.map(attr("integrity") := _),
        crossorigin.map(attr("crossorigin") := _)
      )
  }

  case class Javascript(uri: Uri,
                        integrity: Option[String] = None,
                        crossorigin: Option[String] = Some("anonymous")) {
    def render: TypedTag[String] =
      script(
        src := uri,
        integrity.map(attr("integrity") := _),
        crossorigin.map(attr("crossorigin") := _)
      )
  }

}
