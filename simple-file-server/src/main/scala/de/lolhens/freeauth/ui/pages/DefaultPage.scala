package de.lolhens.freeauth.ui.pages

import de.lolhens.freeauth.Server._
import de.lolhens.freeauth.ui.{AdditionalTags, NavigationContext}
import monix.eval.Task
import org.http4s.Uri
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait DefaultPage extends Page {

  import DefaultPage._

  override def render(navContext: NavigationContext): Task[TypedTag[String]] = {
    for {
      pageHead <- pageHead(navContext)
      pageBody <- pageBody(navContext)
    } yield
      html(
        pageHead,
        pageBody
      )
  }


  def pageHead(navContext: NavigationContext): Task[TypedTag[String]] = {
    for {
      pageTitle <- pageTitle(navContext)
      stylesheets <- pageStylesheets(navContext)
      javascripts <- pageJavascripts(navContext)
    } yield
      head(
        meta(charset := "UTF-8"),
        pageTitle.map(tag("title")(_)),
        stylesheets.map(_.render),
        javascripts.map(_.render),
        tag("style")(
          ".stretched-link-container { transform: rotate(0); }"
        )
      )
  }

  // TODO
  def pageTitle(navContext: NavigationContext): Task[Option[String]] = Task.now(Some(pageTitle))

  def pageStylesheets(navContext: NavigationContext): Task[List[Stylesheet]] = Task.now(List(
    Stylesheet(Uri.unsafeFromString("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"), integrity = Some("sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"))
  ))

  def pageJavascripts(navContext: NavigationContext): Task[List[Javascript]] = Task.now(List(
    Javascript(Uri.unsafeFromString("https://code.jquery.com/jquery-3.3.1.slim.min.js"), integrity = Some("sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo")),
    Javascript(Uri.unsafeFromString("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"), integrity = Some("sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1")),
    Javascript(Uri.unsafeFromString("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"), integrity = Some("sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"))
  ))

  def pageBody(navContext: NavigationContext): Task[TypedTag[String]] = {
    for {
      pageContent <- pageContent(navContext)
    } yield
      body(`class` := "h-100",
        div(`class` := "h-100 d-flex flex-column",
          div(`class` := "flex-fill d-flex flex-column",
            div(id := "content", `class` := "flex-fill",
              pageContent
            )
          )
        )
      )
  }

  def pageContent(navContext: NavigationContext): Task[TypedTag[String]] = {
    for {
      pageContainer <- pageContainer(navContext)
    } yield
      div(`class` := "container h-100 pt-4",
        h1(pageTitle),
        pageContainer
      )
  }

  def pageContainer(navContext: NavigationContext): Task[TypedTag[String]]
}

object DefaultPage extends AdditionalTags {

  case class Stylesheet(uri: Uri,
                        integrity: Option[String] = None,
                        crossorigin: Option[String] = None) {
    def render: TypedTag[String] =
      link(
        rel := "stylesheet",
        href := uri,
        integrity.map(attr("integrity") := _),
        attr("crossorigin") := "anonymous"
      )
  }

  case class Javascript(uri: Uri,
                        integrity: Option[String] = None,
                        crossorigin: Option[String] = None) {
    def render: TypedTag[String] =
      script(
        src := uri,
        integrity.map(attr("integrity") := _),
        attr("crossorigin") := "anonymous"
      )
  }

  trait NavbarItem {
    def pageOption: Option[Page]

    def render(active: Boolean = true): TypedTag[String]
  }


  case class NavbarLink(text: String,
                        uriOption: Option[Uri],
                        pageOption: Option[Page] = None) extends NavbarItem {
    def render(active: Boolean): TypedTag[String] =
      li(`class` := List("nav-item") ++ List("active").filter(_ => active),
        uriOption match {
          case Some(uri) =>
            a(`class` := "nav-link", href := uri,
              text
            )

          case None =>
            a(`class` := "nav-link disabled", tabindex := "-1", `aria-disabled` := "true",
              text
            )
        }
      )
  }

  object NavbarLink {
    def fromPage(page: Page): NavbarLink =
      NavbarLink(page.pageTitle, pageOption = Some(page), uriOption = Some(page.uri(uiRoot)))
  }

}
