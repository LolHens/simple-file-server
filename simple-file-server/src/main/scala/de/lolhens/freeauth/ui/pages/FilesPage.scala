package de.lolhens.freeauth.ui.pages

import de.lolhens.freeauth.ui.NavigationContext
import monix.eval.Task
import org.http4s.Uri
import scalatags.Text
import scalatags.Text.all._

object FilesPage extends DefaultPage {
  override def pageTitle: String = "Files"

  override def uri(root: Uri): Uri = root

  override def pageContainer(navContext: NavigationContext): Task[Text.TypedTag[String]] = Task {
    div("")
  }
}
