package org.http4s.dsl

import monix.eval.Task
import org.http4s.HttpRoutes

object task extends Http4sDsl[Task] {
  type Service = HttpRoutes[Task]
}
