package ru.mit.spbau;

import org.junit.Assert;
import org.junit.Test;
import ru.mit.spbau.Utils.SumValueOfRange;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by kate on 17.09.17.
 */


public class TestLightFutureImpl {

    @Test(expected = Test.None.class)
    public void getFutureValueTest() throws InterruptedException, LightExecutionException {
        final int threads = 10;
        ThreadPool pool = new ThreadPoolImpl(threads);
        LightFuture<Integer> future = pool.add(new SumValueOfRange(threads));
        Assert.assertTrue(future.get().equals(IntStream.range(0, threads).sum()));
        pool.shutdown();
    }

    @Test(expected = LightExecutionException.class)
    public void getExceptionTest() throws InterruptedException, LightExecutionException {
        final int threads = 1;
        ThreadPool pool = new ThreadPoolImpl(threads);
        pool.add(() -> {
            throw new RuntimeException();
        }).get();
        pool.shutdown();
    }
    @Test(expected = Test.None.class)
    public void isReadyTest() throws InterruptedException, LightExecutionException {
        final int threads = 5;
        final int sleep = 100;
        final int result = 10;
        ThreadPool pool = new ThreadPoolImpl(threads);
        LightFuture<Integer> future = pool.add(() -> {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
            }
            return result;
        });

        assertFalse(future.isReady());
        assertEquals(result, future.get().intValue());
        assertTrue(future.isReady());
    }


    @Test(expected = Test.None.class)
    public void testFutureThenApply() throws LightExecutionException, InterruptedException {
        final int threads = 2;
        final int sleepValue = 200;
        ThreadPool threadPool = new ThreadPoolImpl(threads);
        LightFuture<Integer> function1 = threadPool.add(new Supplier<Integer>() {
            @Override
            public Integer get() {
                try {
                    Thread.sleep(sleepValue);
                } catch (InterruptedException ex) {
                }
                return IntStream.range(0, threads).sum(); // 1
            }
        });
        LightFuture<Integer> function2 = threadPool.add(new Supplier<Integer>() {
            @Override
            public Integer get() {
                try {
                    Thread.sleep(sleepValue);
                } catch (InterruptedException ex) {
                }
                return IntStream.range(0, threads).map(i -> i * i).sum(); // 1
            }
        });

        function1 = function1.thenApply(i -> i * 2); //2
        function2 = function2.thenApply(i -> i * 2); //2
        assertEquals(4, function1.get() + function2.get());
        threadPool.shutdown();
    }
}

