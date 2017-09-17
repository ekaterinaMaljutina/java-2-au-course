package ru.mit.spbau;

/**
 * Created by kate on 16.09.17.
 */
public class LightExecutionException extends Exception {

    private Throwable throwable;

    public LightExecutionException(Throwable throwable) {
        this.throwable = throwable;
    }

    Throwable getThrowable() {
        return throwable;
    }
}
