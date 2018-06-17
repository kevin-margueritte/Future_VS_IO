package com.mrgueritte.example

import cats.effect.IO

import scala.concurrent.Future

object Main extends App {

  def referentialTransparency() = {
    def print(msg: String) = println(s"Hello $msg")

    val ioPrint = for {
      _ <- IO(print("IO"))
      _ <- IO(print("IO"))
    } yield ()
    // print nothing

    val futurePrint = for {
      _ <- Future(print("Future"))
      _ <- Future(print("Future"))
    } yield ()
    // > Hello Future
    // > Hello Future

    ioPrint.unsafeRunSync()
    // > Hello IO
    // > Hello IO
  }

  referentialTransparency()

}
