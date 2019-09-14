package de.lolhens.freeauth.ui

import de.lolhens.freeauth.ui.pages.Page
import monix.eval.Task
import org.http4s.Uri
import scalatags.Text.TypedTag

case class NavigationContext(uri: Uri, page: Page, user: Option[String]) {
  def renderPage: Task[TypedTag[String]] = page.render(this)
}
