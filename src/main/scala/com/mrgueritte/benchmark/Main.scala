package com.mrgueritte.benchmark

import java.util.concurrent.TimeUnit
import cats._
import cats.effect.IO
import cats.implicits._
import org.openjdk.jmh.annotations._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(20)
@Fork(value = 1, jvmArgsAppend = Array("-Djmh.stack.lines=3"))
@BenchmarkMode(Array(Mode.Throughput))
class Main {
  import cats.syntax.all._

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
  def applyIO() = {
    IO.apply(1)
  }

  @Benchmark
  def fiboParallelWithFuture() = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val futureResult = Future.sequence((1 to 10).map(n => Future(fib(n, 1, 1))).toList)
    Await.result(futureResult, Duration.Inf)
  }

  @Benchmark
  def fiboParallelWithIOApply() = {
    import cats.effect.unsafe.implicits.global
    val ec = scala.concurrent.ExecutionContext.Implicits.global
    (1 to 10).toList.parTraverse(n => IO.apply(fib(n, 1, 1))).evalOn(ec).unsafeRunSync()
  }

  @Benchmark
  def fiboParallelWithIOPure() = {
    import cats.effect.unsafe.implicits.global
    val ec = scala.concurrent.ExecutionContext.Implicits.global
    (1 to 10).toList.parTraverse(n => IO.pure(fib(n, 1, 1))).evalOn(ec).unsafeRunSync()
  }

}
