
# Scala Future VS cats effects IO

A simple benchmark example to show the power monads/IO

# Pure method



	    @Benchmark  
	    def pureFuture() = Future.successful(1)  
	  
	    @Benchmark  
	    def pureIO() = IO.pure(1)  
	      
	    @Benchmark  
	    def applyIO() = IO.apply(1)    

|  Benchmark| Mode | Cnt | Score | Error | Units  
|--|--| -- | -- | -- | --|  
| Main.pureFuture | thrpt | 5 | 71534524,636 ± | 26252881,930 | ops/s  
| Main.pureIO | thrpt | 5 | 243704803,916 ± | 10403234,955 | ops/s  
| Main.applyIO | thrpt | 5 |  126596763,796 ± | 43952407,863 | ops/s

# Parallel execution



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

|  Benchmark| Mode | Cnt | Score | Error | Units  
|--|--| -- | -- | -- | --|  
| Main.fiboParallelWithFuture | thrpt | 5 | 37543,257 ± | 5880,173 | ops/s  
| Main.fiboParallelWithIOApply | thrpt | 5 | 10223,572 ± | 1026,223 | ops/s  
| Main.fiboParallelWithIOPure | thrpt | 5 | 9806,131 ± | 1704,853 | ops/s

# Run benchmark

Benchmarks are generated with [sbt-jmh](https://github.com/ktoso/sbt-jmh)  
Run benchmark

jmh:run -t1 -f 1 -wi 5 -i 5 .*Main.*
