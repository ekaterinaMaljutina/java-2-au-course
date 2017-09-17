package ru.mit.spbau;

import java.util.function.Function;

/**
 * Created by kate on 16.09.17.
 */
public interface LightFuture<T> {

    boolean isReady();

    T get() throws InterruptedException, LightExecutionException;

    <Y> LightFuture<Y> thenApply(Function<? super T, ? extends Y> function);
}
