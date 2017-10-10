package ru.mit.spbau;

import org.junit.Assert;
import org.junit.Test;
import ru.mit.spbau.Utils.SumValueOfRange;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by kate on 17.09.17.
 */


public class TestLightFutureImpl {

    private static final int COUNT_THREADS = 10;
    private static final int SLEEP_MS = 100;
    private static final int EXPECTED_RESULT = 10;

    @Test
    public void getFutureValueTest() throws InterruptedException, LightExecutionException {
        ThreadPool pool = new ThreadPoolImpl(COUNT_THREADS);
        LightFuture<Integer> future = pool.add(new SumValueOfRange(COUNT_THREADS));
        Assert.assertTrue(future.get().equals(IntStream.range(0, COUNT_THREADS).sum()));
        pool.shutdown();
    }

    @Test(expected = LightExecutionException.class)
    public void getExceptionTest() throws InterruptedException, LightExecutionException {
        ThreadPool pool = new ThreadPoolImpl(COUNT_THREADS);
        pool.add(() -> {
            throw new RuntimeException();
        }).get();
        pool.shutdown();
    }

    @Test
    public void isReadyTest() throws InterruptedException, LightExecutionException {
        ThreadPool pool = new ThreadPoolImpl(COUNT_THREADS);
        LightFuture<Integer> future = pool.add(() -> {
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
            }
            return EXPECTED_RESULT;
        });

        assertFalse(future.isReady());
        assertEquals(EXPECTED_RESULT, future.get().intValue());
        assertTrue(future.isReady());
    }

    @Test
    public void testFutureThenApply() throws LightExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(COUNT_THREADS);
        LightFuture<Integer> function1 = threadPool.add(() -> {
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException ex) {
            }
            return IntStream.range(0, 2).sum(); // 1
        });
        LightFuture<Integer> function2 = threadPool.add(() -> {
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException ex) {
            }
            return IntStream.range(0, 2).map(i -> i * i).sum(); // 1
        });

        function1 = function1.thenApply(i -> i * 5); // 1 * 5 => 5
        function2 = function2.thenApply(i -> i * 2); // 1 * 2 => 2
        assertEquals(EXPECTED_RESULT, function1.get() * function2.get()); // 5 * 2
        threadPool.shutdown();
    }
}

