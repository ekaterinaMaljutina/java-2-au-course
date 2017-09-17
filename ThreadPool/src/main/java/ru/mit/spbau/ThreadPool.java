package ru.mit.spbau;

import java.util.function.Supplier;

/**
 * Created by kate on 16.09.17.
 */
public interface ThreadPool {

    <R> LightFuture<R> add(Supplier<R> supplier);

    void shutdown();
}
