package ru.mit.spbau;

import org.junit.Test;
import ru.mit.spbau.Utils.SumValueOfRange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by kate on 17.09.17.
 */


public class TestThreadPoolImpl {

    @Test(expected = Test.None.class)
    public void testSimple() throws LightExecutionException, InterruptedException {
        final int sleepTime = 100;
        final int answer = 4950;
        final int range = 100;
        ThreadPoolImpl pool = new ThreadPoolImpl(2);

        final LightFuture<Integer> w1 = pool.add(new SumValueOfRange(range));
        final LightFuture<Integer> w2 = pool.add(new SumValueOfRange(range));

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignored) {
        }

        Integer a1 = w1.get();
        assertTrue(w1.isReady());
        assertEquals(answer, a1.intValue());

        Integer a2 = w2.get();
        assertTrue(w2.isReady());
        assertEquals(answer, a2.intValue());
    }


    @Test(expected = Test.None.class)
    public void manyThreadsTest() {
        final int poolSize = 25;
        final int range = 100;

        ThreadPoolImpl pool = new ThreadPoolImpl(poolSize);
        List<LightFuture<Integer>> futures = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        IntStream.range(0, range)
                .forEach(i -> futures
                        .add(pool.add(new SumValueOfRange(range))));

        futures.stream().map(f -> {
            Integer val = null;
            try {
                val = f.get();
            } catch (LightExecutionException | InterruptedException ex) {
            }
            return val;
        }).forEach(results::add);

        IntStream.range(0, range).forEach(i -> assertEquals(4950, results.get(i).intValue()));
    }

    @Test
    public void leastThreadCountThreadsInPoolTest() throws InterruptedException {
        final int threads = 10;
        final boolean[] arrived = {false};
        ThreadPool threadPool = new ThreadPoolImpl(threads);
        CyclicBarrier barrier = new CyclicBarrier(threads, () -> arrived[0] = true);
        IntStream.range(0, threads).forEach(i -> {
            threadPool.add(() -> {
                try {
                    return barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
                return 0;
            });
        });
        Thread.sleep(100);
        assertTrue(arrived[0]);
        threadPool.shutdown();
    }

    @Test
    public void mostThreadCountThreadsInPoolTest() throws InterruptedException {
        final int threads = 10;
        final boolean[] arrived = {false};
        ThreadPool threadPool = new ThreadPoolImpl(threads);
        CyclicBarrier barrier = new CyclicBarrier(threads + 1, () -> arrived[0] = true);
        IntStream.range(0, threads).forEach(i -> {
            threadPool.add(() -> {
                try {
                    return barrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    Thread.currentThread().interrupt();
                }
                return 0;
            });
        });
        Thread.sleep(100);
        assertFalse(arrived[0]);
        threadPool.shutdown();
    }
}
