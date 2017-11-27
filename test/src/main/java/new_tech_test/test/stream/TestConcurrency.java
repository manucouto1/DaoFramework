package new_tech_test.test.stream;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

public class TestConcurrency {
	
	int count = 0;
	
	
	public TestConcurrency(){
		testInvokeAll();
	}
	
	void testInvokeAll() {
		ExecutorService executor = Executors.newWorkStealingPool();
		List<Callable<String>> callables = Arrays.asList(
				() -> "task1",
				() -> "task2",
				() -> "task3");
		try {
			executor.invokeAll(callables)
				.stream()
				.map(future -> {
					try{
						return future.get();
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				}).forEach(System.out::println);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
				
	}
	void testTimeOutException() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		Future<Integer> future = executor.submit(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
				return 123;
			} catch (InterruptedException e) {
				throw new IllegalStateException("task interrupted", e);
			}
		});
		
		try {
			future.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void testCallable() throws InterruptedException, ExecutionException {
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Callable<Integer> task = () -> {
			try {                           
				TimeUnit.SECONDS.sleep(1);
				return 123;
			} catch (InterruptedException e) {
				throw new IllegalStateException("task interrupted", e);
			}
		};
		
		Future<Integer> future = executor.submit(task);
		
		System.out.println("future done? " + future.isDone());
		
		Integer result = future.get();
		
		System.out.println("future done? " + future.isDone());
		System.out.println("result: " + result);
	}
	
	void testExecutors() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			String threadName = Thread.currentThread().getName();
			System.out.println("Hello " + threadName);
		});
		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(5,TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.err.println("task interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println(" cancel non-finished tasks");
			}
			executor.shutdown();
			System.out.println("shutdown finished");
		}
	}
	
	void testThreads() {
		Runnable task = () -> {
			try {
				String threadName = Thread.currentThread().getName();
				System.out.println("Hello "+threadName);
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		task.run();
		
		Thread thread = new Thread(task);
		thread.start();
		System.out.println("Done!");
	}
	void testSemaphores() {
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Semaphore semaphore = new Semaphore(5);
		Runnable longRunnigTask = () -> {
			boolean permit = false;
			try {
				permit = semaphore.tryAcquire(1, TimeUnit.NANOSECONDS);
				if (permit) {
					System.out.println("Semaphore acqueired");
					sleep(5);
				}else{
					System.out.println("Could not acquire semaphore");
				}
					
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};
		IntStream.range(0, 10)
			.forEach(i -> executor.submit(longRunnigTask));
		
		stop(executor);
	}
	
	void testLock() {
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();
		
		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try{
				System.out.println("Optimistic lock valid:" + lock.validate(stamp));
				sleep(1);
				System.out.println("Optimistic lock valid:" + lock.validate(stamp));
				sleep(2);
				System.out.println("Optimistic lock valid:" + lock.validate(stamp));
			} finally {
				lock.unlock(stamp);
			}
		
		});
		
		executor.submit(() -> {
			long stamp = lock.writeLock();
			try{
				System.out.println("Write Lock acquired");
				sleep(2);
			} finally {
				lock.unlock(stamp);
				System.out.println("Write done");
			}
		});
		
		stop(executor);
	}
	
	
	void testSync() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		IntStream.range(0, 10000)
		    .forEach(i -> executor.submit(this::incrementSync));
		stop(executor);
		System.out.println(count);
	}
	
	void increment() {
		count++;
	}
	
	void incrementSync() {
		synchronized (this) {
			count++;
		}
	}
	
	public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
	
}
