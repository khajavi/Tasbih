package com.speakfreely.tasbih

import cats.Monad
import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.speakfreely.tasbih.algebras.{BaseBlockChain, Logger}
import com.speakfreely.tasbih.implementation.{BlockChain, BlockLogger}
import zio.ZIO
import zio.console.Console
import zio.interop.catz._

import scala.language.higherKinds

object Main extends CatsApp {
  type Task[+A] = ZIO[Any, Throwable, A]

  private implicit def blockChain[F[_]](implicit sync: Sync[F]): BlockChain[F] = BlockChain[F]

  private implicit def logger[F[_]](implicit sync: Sync[F]): BlockLogger[F] = BlockLogger[F]

  private def sampleProgram[F[_]](B: BaseBlockChain[F],
                            L: Logger[F])(implicit F: Monad[F]): F[Unit] = {
    import B._
    import L._
    for {
      _ <- addBlock("Hello")
      _ <- addBlock("Block")
      b <- addBlock("Chain!")
      _ <- log("printing latest block" + b.latestBlock)
    } yield ()
  }

  override def run(args: List[String]): ZIO[Console, Nothing, Int] =
    sampleProgram[Task](blockChain, logger).fold(_ => 1, _ => 0)
}
