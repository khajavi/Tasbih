package com.speakfreely.tasbih

import cats.Monad
import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.speakfreely.tasbih.algebras.{BaseBlockChain, Logger}
import com.speakfreely.tasbih.implementation.{BlockChain, BlockLogger}
import zio.{Task, ZIO}
import zio.console.Console
import zio.interop.catz._

import scala.language.higherKinds

object Main extends CatsApp {
  private implicit def blockChain[F[_]](implicit sync: Sync[F]): BlockChain[F] = BlockChain[F]

  private implicit def logger[F[_]](implicit sync: Sync[F]): BlockLogger[F] = BlockLogger[F]

  private def sampleProgram[F[_]: Logger: BaseBlockChain: Monad]: F[Unit] = {
    val L = Logger[F]
    val B = BaseBlockChain[F]
    import L._, B._

    for {
      _ <- addBlock("Hello")
      _ <- addBlock("Block")
      b <- addBlock("Chain!")
      _ <- log("printing latest block" + b.latestBlock)
    } yield ()
  }

  override def run(args: List[String]): ZIO[Console, Nothing, Int] =
    sampleProgram[Task].fold(_ => 1, _ => 0)
}
