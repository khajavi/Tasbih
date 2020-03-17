package com.speakfreely.tasbih.services

import zio.{UIO, ZIO}


trait Logger {
  def logger: Logger.Service
}

object Logger {

  trait Service {
    def log(txt: String): UIO[Unit]
  }

  trait Live extends Logger.Service {
    override def log(str: String): ZIO[Any, Nothing, Unit] = {
      ZIO.effectTotal(println("Logging: " + str))
    }
  }

  object Live extends Live

}


