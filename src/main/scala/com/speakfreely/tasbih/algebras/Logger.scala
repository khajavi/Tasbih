package com.speakfreely.tasbih.algebras

import scala.language.higherKinds

trait Logger[F[_]] {
  def log(string: String): F[Unit]
}

object Logger {
  def apply[F[_]](implicit F: Logger[F]): Logger[F] = F
}
