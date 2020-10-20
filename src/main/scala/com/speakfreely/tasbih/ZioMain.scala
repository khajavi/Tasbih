package com.speakfreely.tasbih

import com.speakfreely.tasbih.services.Ledger.BlockChain
import com.speakfreely.tasbih.services.{Ledger, Logger, ledger, logger}
import zio.{App, ZIO}

object MainApp extends App {
  val program: ZIO[Logger.Service with Ledger.Service, Nothing, Int] = {
    for {
      b1 <- ledger.addBlock("Hello,", BlockChain())
      b2 <- ledger.addBlock("Blockchain", b1)
      b3 <- ledger.addBlock("World!", b2)
      _ <- logger.log(b3.blocks.toString())
      latest <- ledger.latestBlock(b3)
      _ <- logger.log(latest.toString)
    } yield 1
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    program.provide(new Logger.Live with Ledger.Live)
  }
}

