package com.speakfreely.tasbih.services

import com.speakfreely.models.Block
import com.speakfreely.tasbih.services.Ledger.BlockChain
import zio.ZIO

package object logger {
  def log(string: String): ZIO[Logger.Service, Nothing, Unit] =
    ZIO.accessM(_.log(string))
}

package object ledger {
  def addBlock(data: String, blockChain: BlockChain): ZIO[Ledger.Service, Nothing, BlockChain] =
    ZIO.accessM(_.addBlock(data, blockChain))

  def latestBlock(blockChain: BlockChain): ZIO[Ledger.Service, Nothing, Block] =
    ZIO.accessM(_.latestBlock(blockChain))
}