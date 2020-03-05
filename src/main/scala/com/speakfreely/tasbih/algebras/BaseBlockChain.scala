package com.speakfreely.tasbih.algebras

import com.speakfreely.models.Block

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
