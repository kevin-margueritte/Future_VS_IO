# Scala Future VS cats effects IO

A simple benchmark example to show the power monads/IO

# Pure method


    @Benchmark
    def pureFuture() = {
      Future.successful(1)
    }

    @Benchmark
    def pureIO() = {
      IO.pure(1)
    }

|  Benchmark| Mode | Cnt | Score | Error | Units
|--|--| -- | -- | -- | --|
| Main.pureFuture | thrpt | 5 | 90154525,520± | 5910050,341| ops/s
| Main.pureIO | thrpt | 5 | 140771372,675± | 18005146,944 | ops/s

# Parallel execution


    @Benchmark
    def fiboParallelWithFuture() = {
      val futureResult = Future.sequence((1 to 10).map(n => Future(fib(n, 1, 1))).toList)
      Await.result(futureResult, Duration.Inf)
    }  æ

    @Benchmark
    def fiboParallelWithIO() = {
      val ioResult = Parallel.parSequence((1 to 10).map(n => IO(fib(n, 1, 1))).toList)
      ioResult.unsafeRunSync()
    }

|  Benchmark| Mode | Cnt | Score | Error | Units
|--|--| -- | -- | -- | --|
| Main.fiboParallelWithFuture | thrpt | 5 | 18610,241± | 6582,670| ops/s
| Main.fiboParallelWithIO | thrpt | 5 | 24288,972± | 9793,324 | ops/s

# Run benchmark

Benchmarks are generated with [sbt-jmh](https://github.com/ktoso/sbt-jmh)
Results are computed with this arguments

    jmh:run -t1 -f 1 -wi 5 -i 5 .*Benchmark.*

