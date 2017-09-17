package ru.mit.spbau.Utils;

import com.sun.istack.internal.NotNull;

import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Created by kate on 17.09.17.
 */

public class SumValueOfRange implements Supplier<Integer> {
    private static final int SLEEP_VALUE = 10;
    private final int range;
    private int sum = 0;

    public SumValueOfRange(int range) {
        this.range = range;
    }

    @Override
    @NotNull
    public Integer get() {
        IntStream.range(0, range)
                .forEach(i -> {
                    sum += i;
                    try {
                        Thread.sleep(SLEEP_VALUE);
                    } catch (InterruptedException ignored) {
                    }
                });
        return sum;
    }
}