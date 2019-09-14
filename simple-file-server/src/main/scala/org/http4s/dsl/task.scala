package org.http4s.dsl

import monix.eval.Task
import org.http4s.HttpRoutes

import scala.language.higherKinds

object task extends Http4sDsl[Task] {
  type Service = HttpRoutes[Task]
}
