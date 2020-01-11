package com.speakfreely.tasbih.algebras

import scala.language.higherKinds

trait Logger[F[_]] {
  def log(string: String): F[Unit]
}
