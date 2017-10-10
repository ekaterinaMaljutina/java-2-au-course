package ru.mit.spbau;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Created by kate on 16.09.17.
 */

public class ThreadPoolImpl implements ThreadPool {

    private final Collection<Thread> threads = new LinkedList<>();
    private final Queue<Task<?>> workQueue = new LinkedList<>();

    public ThreadPoolImpl(int size) {
        IntStream.range(0, size)
                .mapToObj(idx -> new Worker())
                .map(Thread::new)
                .peek(Thread::start)
                .forEach(threads::add);
    }

    @Override
    public <R> LightFuture<R> add(Supplier<R> supplier) {
        final Task<R> task = new Task<>(supplier);
        return addTask(task);
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    private <R> LightFuture<R> addTask(Task<R> task) {
        synchronized (workQueue) {
            workQueue.add(task);
            workQueue.notify();
        }
        return task;
    }

    private Task<?> getTask() throws InterruptedException {
        synchronized (workQueue) {
            waitQueue();
            return workQueue.poll();
        }
    }

    private void waitQueue() throws InterruptedException {
        while (workQueue.isEmpty()) {
            workQueue.wait();
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    final Task<?> currentTask = getTask(); // throw InterruptedException
                    currentTask.runTask();
                }
            } catch (InterruptedException ignore) {
            }
        }
    }

    private class Task<R> implements LightFuture<R> {
        private R result;
        private LightExecutionException exception;
        private final Collection<Task<?>> dependents = new LinkedList<>();
        private final Supplier<R> supplier;
        private volatile boolean isReady;

        public Task(final Supplier<R> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public R get() throws LightExecutionException, InterruptedException {
            if (!isReady()) {
                waitIsReady();
            }
            if (exception != null) {
                throw exception;
            }
            return result;
        }

        @Override
        public <U> LightFuture<U> thenApply(Function<? super R, ? extends U> function) {
            final Task<U> task = createDependentTask(function);
            if (isReady()) {
                addTask(task);
            } else {
                addDependantTask(task);
            }
            return task;
        }

        public void runTask() {
            try {
                setResult();
            } catch (Exception ex) {
                setException(new LightExecutionException(ex));
            }
            submitDependentsAndClear();
        }

        private <U> Task<U> createDependentTask(Function<? super R, ? extends U> function) {
            return new Task<>(() -> {
                try {
                    return function.apply(get());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        private synchronized <U> void addDependantTask(Task<U> task) {
            if (isReady()) {
                addTask(task);
            } else {
                synchronized (dependents) {
                    dependents.add(task);
                }
            }
        }

        private void submitDependentsAndClear() {
            synchronized (dependents) {
                dependents.forEach(ThreadPoolImpl.this::addTask);
                dependents.clear();
            }
        }

        private synchronized void waitIsReady() throws InterruptedException {
            while (!isReady()) {
                wait();
            }
        }

        private synchronized void setResult() {
            result = supplier.get();
            isReady = true;
            notifyAll();
        }

        private synchronized void setException(final LightExecutionException exception) {
            this.exception = exception;
            isReady = true;
            notifyAll();
        }
    }
}