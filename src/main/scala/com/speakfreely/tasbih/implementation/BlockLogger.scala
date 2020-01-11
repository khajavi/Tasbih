package com.speakfreely.tasbih.implementation

import cats.effect.Sync
import com.speakfreely.tasbih.algebras.Logger

import scala.language.higherKinds

case class BlockLogger[F[_]](implicit F: Sync[F]) extends Logger[F] {
  override def log(string: String): F[Unit] = F.delay(println(string))
}
