package de.lolhens.freeauth.ui.pages

import de.lolhens.freeauth.ui.{AdditionalTags, NavigationContext}
import monix.eval.Task
import org.http4s.Uri
import scalatags.Text.TypedTag

trait Page extends AdditionalTags {
  def pageTitle: String

  def uri(root: Uri): Uri

  def render(navContext: NavigationContext): Task[TypedTag[String]]
}
