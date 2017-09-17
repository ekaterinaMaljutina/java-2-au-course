package ru.mit.spbau;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by kate on 16.09.17.
 */

public class ThreadPoolImpl implements ThreadPool {

    private final Collection<Thread> threads = new LinkedList<>();
    private final Queue<Task<?>> workQueue = new LinkedList<>();

    public ThreadPoolImpl(@NotNull int size) {

        Stream.iterate(0, x -> x + 1)
                .limit(size)
                .map(x -> new Thread(this::createTask))
                .peek(Thread::start)
                .forEach(threads::add);
    }

    @Override
    @Nullable
    public <R> LightFuture<R> add(@NotNull Supplier<R> supplier) {
        return addTask(new Task<R>() {
            @Override
            public R getFromSupplier() {
                return supplier.get();
            }
        });
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    @Nullable
    private <R> LightFuture<R> addTask(@Nullable Task<R> task) {
        synchronized (workQueue) {

            workQueue.add(task);
            workQueue.notify();

        }
        return task;
    }

    private void createTask() {
        Task<?> task;
        while (!Thread.interrupted()) {
            synchronized (workQueue) {

                waitQueue();
                task = workQueue.poll();

            }
            task.run();
        }
    }

    private void waitQueue() {
        while (workQueue.isEmpty()) {
            try {

                workQueue.wait();

            } catch (InterruptedException ex) {
            }
        }
    }

    @Nullable
    private <R, U> Task<U> dependentTask(@NotNull Task<R> prev,
                                         @NotNull Function<? super R, ? extends U> p) {
        return new Task<U>() {
            @Override
            public U getFromSupplier() throws LightExecutionException {
                return p.apply(prev.get());
            }
        };
    }

    private abstract class Task<R> implements LightFuture<R>, Runnable {
        private R result;
        private LightExecutionException exception;
        private final Collection<Task<?>> dependents = new LinkedList<>();

        public abstract R getFromSupplier() throws LightExecutionException;

        @Override
        public boolean isReady() {
            return result != null || exception != null;
        }

        @Override
        @Nullable
        public R get() throws LightExecutionException {
            waitIsReady();
            if (exception != null) {
                throw exception;
            }
            return result;
        }

        @Override
        @Nullable
        public <U> LightFuture<U> thenApply(@NotNull Function<? super R, ? extends U> function) {
            Task<U> task = dependentTask(this, function);

            addTask(task);

            return task;
        }

        @Override
        public void run() {
            try {
                result = getFromSupplier();
            } catch (Exception e) {
                exception = new LightExecutionException(e);
            }
            clearDependents();
        }

        private synchronized <U> void addTask(@Nullable Task<U> task) {
            if (isReady()) {
                addTask(task);
            } else {
                dependents.add(task);
            }
        }

        private synchronized void clearDependents() {
            dependents.forEach(ThreadPoolImpl.this::addTask);
            dependents.clear();
            notifyAll();
        }

        private synchronized void waitIsReady() {
            while (!isReady()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}