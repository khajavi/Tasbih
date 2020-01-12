package com.speakfreely.tasbih.algebras

import scala.language.higherKinds
import scala.util.Try

trait BaseBlockChain[F[_]] {
  def addBlock(data: String): F[BaseBlockChain[F]]

  def addBlock(data: Block): F[Try[BaseBlockChain[F]]]

  def firstBlock: Block

  def latestBlock: Block

}

object BaseBlockChain {
  def apply[F[_]](implicit F: BaseBlockChain[F]): BaseBlockChain[F] = F
}

case class Block(index: Int,
                 previousHash: String,
                 timestamp: Long,
                 data: String,
                 hash: String)

object GenesisBlock extends Block(
  index = 0,
  previousHash = "0",
  timestamp = 1497359352,
  data = "Genesis block",
  hash = "ccce7d8349cf9f5d9a9c8f9293756f584d02dfdb953361c5ee36809aa0f560b4"
)
