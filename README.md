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

|  Benchmark| Mode | Cnt | Score | Error | Units
|--|--| -- | -- | -- | --|
| Main.fiboParallelWithFuture | thrpt | 5 | 206348,242± | 44904,403| ops/s
| Main.fiboParallelWithIO | thrpt | 5 | 25429,418± | 7802,003 | ops/s

# Run benchmark

Benchmarks are generated with [sbt-jmh](https://github.com/ktoso/sbt-jmh)
Results are computed with this arguments

    jmh:run -t1 -f 1 -wi 5 -i 5 .*Benchmark.*

