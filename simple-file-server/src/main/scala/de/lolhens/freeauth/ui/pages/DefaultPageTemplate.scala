package de.lolhens.freeauth.ui.pages

import de.lolhens.freeauth.ui.pages.PageTemplate.{Javascript, Page, Stylesheet}
import org.http4s.Uri
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait DefaultPageTemplate extends PageTemplate {
  override protected def template(page: Page): TypedTag[String] = {
    val newPage = page
      .withStylesheets(List(
        Stylesheet(Uri.unsafeFromString("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"), integrity = Some("sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"))
      ))
      .withJavascripts(List(
        Javascript(Uri.unsafeFromString("https://code.jquery.com/jquery-3.3.1.slim.min.js"), integrity = Some("sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo")),
        Javascript(Uri.unsafeFromString("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"), integrity = Some("sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1")),
        Javascript(Uri.unsafeFromString("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"), integrity = Some("sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM")),
        Javascript(Uri.unsafeFromString("https://use.fontawesome.com/2171858466.js"), crossorigin = None)
      ))

    html(
      pageHead(newPage),
      pageBody(newPage)
    )
  }

  def pageHead(page: Page): TypedTag[String] = {
    head(
      meta(charset := "UTF-8"),
      page.title.map(tag("title")(_)),
      page.stylesheets.map(_.render),
      page.javascripts.map(_.render),
      tag("style")(
        ".stretched-link-container { transform: rotate(0); }"
      )
    )
  }

  def pageBody(page: Page): TypedTag[String] = {
    body(`class` := "h-100",
      div(`class` := "h-100 d-flex flex-column",
        div(`class` := "flex-fill d-flex flex-column",
          div(id := "content", `class` := "flex-fill",
            page.content
          )
        )
      )
    )
  }
}
