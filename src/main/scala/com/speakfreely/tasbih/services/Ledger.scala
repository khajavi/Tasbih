package com.speakfreely.tasbih.services

import java.util.Date

import com.speakfreely.models.{Block, GenesisBlock}
import tsec.common._
import tsec.hashing.jca.SHA256
import zio.UIO

import scala.util.{Failure, Success, Try}

trait Ledger {
  def blockChain: Ledger.Service
}

object Ledger {

  case class BlockChain(blocks: Seq[Block])

  object BlockChain {
    def apply(): BlockChain = new BlockChain(Seq(GenesisBlock))

//    def apply(blocks: Seq[Block]): UIO[[BlockChain]] =
//    {
//      for {
//        valid <- validChain(blocks)
//      } yield
//        if (valid)
//          Success(new BlockChain(blocks))
//        else
//          Failure(new IllegalArgumentException("Invalid chain specified."))
//    }

    //FIXME: this function is not tail-recursive
    private def validChain(chain: Seq[Block]): UIO[Boolean] = {
      chain match {
        case singleBlock :: Nil
          if singleBlock == GenesisBlock => UIO(true)
        case head :: beforeHead :: tail => for {
          res <- if (validBlock(head, beforeHead)) validChain(beforeHead :: tail) else UIO(false)
        } yield res
        case _ => UIO(false)
      }
    }

    def validBlock(newBlock: Block, previousBlock: Block): Boolean = {
      previousBlock.index + 1 == newBlock.index &&
        previousBlock.hash == newBlock.previousHash &&
        calculateHashForBlock(newBlock) == newBlock.hash
    }

    private def calculateHashForBlock(block: Block): String = calculateHash(block.index, block.previousHash, block.timestamp, block.data)

    private def hash(data: String): String = SHA256.hash(data.utf8Bytes).toHexString

    def calculateHash(index: Int, previousHash: String, timestamp: Long, data: String): String =
      hash(s"$index:$previousHash:$timestamp:$data")
  }

  trait Service {
    def addBlock(data: String, blockChain: BlockChain): UIO[BlockChain]

    def addBlock(data: Block, blockChain: BlockChain): UIO[Try[BlockChain]]

    def firstBlock(blockChain: BlockChain): UIO[Block]

    def latestBlock(blockChain: BlockChain): UIO[Block]
  }

  trait Live extends Service {
    override def addBlock(data: String, blockChain: BlockChain): UIO[BlockChain] = for {
      next <- generateNextBlock(data, blockChain)
    } yield BlockChain(next +: blockChain.blocks)

    override def addBlock(block: Block, blockChain: BlockChain): UIO[Try[BlockChain]] = for {
      valid <- validBlock(block, blockChain)
    } yield if (valid)
      Success(new BlockChain(block +: blockChain.blocks))
    else
      Failure(new IllegalArgumentException("Invalid block added"))


    override def firstBlock(blockChain: BlockChain): UIO[Block] = UIO(blockChain.blocks.last)

    override def latestBlock(blockChain: BlockChain): UIO[Block] = UIO(blockChain.blocks.head)

    private def generateNextBlock(data: String, blockChain: BlockChain): UIO[Block] = for {
      previousBlock <- latestBlock(blockChain)
      nextTimestamp = new Date().getTime() / 1000 //Fixme: use java.time.LocalDateTime
      nextIndex = previousBlock.index + 1
      nextHash = BlockChain.calculateHash(nextIndex, previousBlock.hash, nextTimestamp, data)
    } yield Block(nextIndex, previousBlock.hash, nextTimestamp, data, nextHash)


    private def validBlock(data: Block, blockChain: BlockChain): UIO[Boolean] = for {
      previous <- latestBlock(blockChain)
    } yield BlockChain.validBlock(data, previous)
  }

  object Live extends Live

}
