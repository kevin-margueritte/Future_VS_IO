package com.mrgueritte.benchmark

import java.util.concurrent.TimeUnit

import cats._
import cats.effect.IO
import cats.implicits._
import org.openjdk.jmh.annotations._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(20)
@Fork(value = 1, jvmArgsAppend = Array("-Djmh.stack.lines=3"))
@BenchmarkMode(Array(Mode.Throughput))
class Benchmarck {

  def fib(n: Int, a: Int, b: Int): Int = {
    n match {
      case 0 => a
      case _ => fib(n - 1, b, a + b)
    }
  }

  @Benchmark
  def pureFuture() = {
    Future.successful(1)
  }

  @Benchmark
  def pureIO() = {
    IO.pure(1)
  }

  @Benchmark
  def fiboParallelWithFuture() = {
    for {
      _ <- Future.sequence((1 to 10).map(n => Future(fib(n, 1, 1))).toList)
    } yield ()
  }

  @Benchmark
  def fiboParallelWithIO() = {
    (for {
      _ <- Parallel.parSequence((1 to 10).map(n => IO(fib(n, 1, 1))).toList)
    } yield ()).unsafeRunSync()
  }

}
