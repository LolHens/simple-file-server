package de.lolhens.fileserver.ui

import org.http4s.Uri
import scalatags.Text.all._
import scalatags.text.Builder

trait AdditionalTags {
  protected val scope: Attr = attr("scope")

  protected val `aria-controls`: Attr = attr("aria-controls")
  protected val `aria-expanded`: Attr = attr("aria-expanded")
  protected val `aria-label`: Attr = attr("aria-label")
  protected val `aria-disabled`: Attr = attr("aria-disabled")

  private def attrValue[A](f: A => String): AttrValue[A] = { (t: Builder, a: Attr, v: A) =>
    stringAttr.apply(t, a, f(v))
  }

  protected implicit val stringListAttr: AttrValue[List[String]] = attrValue(_.mkString(" "))
  protected implicit val uriAttr: AttrValue[Uri] = attrValue(_.toString)
}
