package ru.mit.spbau;

import org.junit.Test;
import ru.mit.spbau.Utils.SumValueOfRange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by kate on 17.09.17.
 */

public class TestThreadPoolImpl {

    private static final int COUNT_THREADS = 10;
    private static final int SLEEP_MS = 100;
    private static final int RANGE = 100;
    private static final int EXPECTED_RESULT = 4950;

    @Test
    public void testSimple() throws LightExecutionException, InterruptedException {
        ThreadPoolImpl pool = new ThreadPoolImpl(COUNT_THREADS);

        final LightFuture<Integer> w1 = pool.add(new SumValueOfRange(RANGE));
        final LightFuture<Integer> w2 = pool.add(new SumValueOfRange(RANGE));

        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException ignored) {
        }

        Integer a1 = w1.get();
        assertTrue(w1.isReady());
        assertEquals(EXPECTED_RESULT, a1.intValue());

        Integer a2 = w2.get();
        assertTrue(w2.isReady());
        assertEquals(EXPECTED_RESULT, a2.intValue());
    }


    @Test
    public void manyThreadsTest() {

        ThreadPoolImpl pool = new ThreadPoolImpl(COUNT_THREADS);
        List<LightFuture<Integer>> futures = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        IntStream.range(0, RANGE)
                .forEach(i -> futures
                        .add(pool.add(new SumValueOfRange(RANGE))));

        futures.stream().map(f -> {
            Integer val = null;
            try {
                val = f.get();
            } catch (LightExecutionException | InterruptedException ex) {
            }
            return val;
        }).forEach(results::add);

        IntStream.range(0, RANGE).forEach(i -> assertEquals(EXPECTED_RESULT, results.get(i).intValue()));
    }

    @Test
    public void leastThreadCountThreadsInPoolTest() throws InterruptedException {
        final boolean[] arrived = {false};
        ThreadPool threadPool = new ThreadPoolImpl(COUNT_THREADS);
        CyclicBarrier barrier = new CyclicBarrier(COUNT_THREADS, () -> arrived[0] = true);
        IntStream.range(0, COUNT_THREADS).forEach(i -> {
            threadPool.add(() -> {
                try {
                    return barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
                return 0;
            });
        });
        Thread.sleep(SLEEP_MS);
        assertTrue(arrived[0]);
        threadPool.shutdown();

        threadPool.add(() -> {
            try {
                return barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
            return 0;
        });
        assertTrue(arrived[0]);
    }

    @Test
    public void mostThreadCountThreadsInPoolTest() throws InterruptedException {
        final boolean[] arrived = {false};
        ThreadPool threadPool = new ThreadPoolImpl(COUNT_THREADS);
        CyclicBarrier barrier = new CyclicBarrier(COUNT_THREADS + 1, () -> arrived[0] = true);
        IntStream.range(0, COUNT_THREADS).forEach(i -> {
            threadPool.add(() -> {
                try {
                    return barrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    Thread.currentThread().interrupt();
                }
                return 0;
            });
        });
        Thread.sleep(SLEEP_MS);
        assertFalse(arrived[0]);
        threadPool.shutdown();
    }
}
