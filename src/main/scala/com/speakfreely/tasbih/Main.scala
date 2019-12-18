package com.speakfreely.tasbih

import java.time.LocalDateTime

import zio.App
import zio.console._

object Main extends App {

  val myAppLogic =
    for {
      _ <- putStrLn("Hello! What is your name?")
      name <- getStrLn
      _ <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
    } yield ()

  def run(args: List[String]) =
    myAppLogic.fold(_ => 1, _ => 0)
}


object AB extends scala.App {

  import tsec.common._
  import tsec.hashing._
  import tsec.hashing.jca._

  val a: CryptoHash[SHA256] = "hellssdasdsd".utf8Bytes.hash[SHA256]

  println(a.foldLeft(List[Byte]())((x: List[Byte], y) => x.+:(y)).toArray.toUtf8String)

}

case class Block(index: Int,
                 hash: String,
                 previousHash: String,
                 timestamp: LocalDateTime,
                 data: String) {

  import cats.effect.IO
  import tsec.common._
  import tsec.hashing.jca._

  private def hash(data: String) = {
    for {
      hash <- SHA256.hash[IO]("hiHello".utf8Bytes)
    } yield hash.toHexString
  }

  def blockHash: IO[String] = {
    hash(index + previousHash + timestamp + data)
  }
}

object GenesisBlock extends Block(
  index = 0,
  previousHash = "0",
  hash = "034854f03ec9f59c20cd06275f219fa5348e8da238624d98989a5ed174d46863",
  timestamp = LocalDateTime.of(2019, 12, 18, 23, 22),
  data = "Genesis Block"
)

