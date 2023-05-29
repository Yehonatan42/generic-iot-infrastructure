package iot.gatewayserver.threadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadPool<V> implements Executor {
    private final Collection<Thread> threadArray = new ArrayList<>();
    private final WaitablePQueue<Task> taskPQ = new WaitablePQueue<>();
    private boolean isShutdown = false;
    private volatile boolean isPaused = false;
    private int numberOfThreads = 0;
    private final Object isPausedLock = new Object();
    private final Object awaitTerminationLock = new Object();
    private final ReentrantLock threadArraySynchronization = new ReentrantLock();
    private final int LOWEST_PRIORITY = 0;
    private final int HIGHEST_PRIORITY = 4;

    public ThreadPool(int numberOfThreads) {
        setNumOfThreads(numberOfThreads);
    }

    @Override
    public void execute(Runnable runnable) {
        submit(runnable, Priority.MEDIUM);
    }

    public Future<Void> submit(Runnable runnable, Priority priority) {
        Callable callable = Executors.callable(runnable);
        return submit(callable, priority);
    }

    public <V> Future<V> submit(Runnable runnable, Priority priority, V value) {
        Callable<V> callable = Executors.callable(runnable, value);
        return submit(callable, priority);
    }

    public <V> Future<V> submit(Callable<V> callable) {
        return submit(callable, Priority.MEDIUM);
    }

    public <V> Future<V> submit(Callable<V> callable, Priority priority) {
        if (isShutdown) {
            throw new RejectedExecutionException("Thread pool has shutdown and cannot receive new tasks");
        }
        Task<V> task = new Task<>(callable, priority.getValue());
        taskPQ.enqueue(task);
        return task.getFuture();
    }

    public void setNumOfThreads(int newNumOfThread) {
        if (newNumOfThread > numberOfThreads) {
            for (int i = 0; i < newNumOfThread - numberOfThreads; ++i) {
                WorkerThread workerThread = new WorkerThread();
                threadArray.add(workerThread);
                workerThread.start();
            }
        } else {
            for (int i = 0; i < numberOfThreads - newNumOfThread; ++i) {
                taskPQ.enqueue(new Task<>(new KillThreadTask(), HIGHEST_PRIORITY));
            }
        }
        numberOfThreads = newNumOfThread;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        if (isShutdown) {
            throw new RejectedExecutionException("Thread pool has shutdown");
        }
        isPaused = false;
        synchronized (isPausedLock) {
            isPausedLock.notifyAll();
        }
    }

    public void shutdown() {
        isShutdown = true;
        for (int i = 0; i < numberOfThreads; ++i) {
            taskPQ.enqueue(new Task<>(new KillThreadTask(), LOWEST_PRIORITY));
        }
    }

    public void awaitTermination() {
        if (!isShutdown) {
            throw new RejectedExecutionException("Thread pool must shutdown first");
        }
        while (!threadArray.isEmpty()) {
            synchronized (awaitTerminationLock) {
                try {
                    awaitTerminationLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class KillThreadTask implements Callable<V> {
        @Override
        public V call() {
            WorkerThread thread = (WorkerThread) Thread.currentThread();
            thread.isAlive = false;
            return null;
        }
    }

    private class Task<V> implements Comparable<Task> {
        private Callable<V> callable = null;
        private int priority = 0;
        private Future<V> future = null;

        private Task(Callable<V> callable, int priority) {
            this.callable = callable;
            this.priority = priority;
            this.future = new TaskFuture();
        }

        @Override
        public int compareTo(Task task) {
            return task.priority - priority;
        }

        private Future<V> getFuture() {
            return future;
        }

        private Callable<V> getCallable() {
            return callable;
        }

        private class TaskFuture implements Future<V> {
            private V result = null;
            private volatile boolean isDone = false;
            private volatile boolean isCancelled = false;
            private final Semaphore isDoneSemaphore = new Semaphore(0);

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (taskPQ.remove(Task.this)) {
                    isCancelled = true;
                    isDone = true;
                    isDoneSemaphore.release();
                    return true;
                }

                return false;
            }

            @Override
            public boolean isCancelled() {
                return isCancelled;
            }

            @Override
            public boolean isDone() {
                return isDone;
            }

            @Override
            public V get() throws InterruptedException, ExecutionException {
                isDoneSemaphore.acquire();
                isDoneSemaphore.release();

                return result;
            }

            @Override
            public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                if (!isDoneSemaphore.tryAcquire(timeout, unit)) {
                    throw new TimeoutException();
                }
                isDoneSemaphore.release();

                return result;
            }
        }
    }

    /*-----------------------------------------------------------------------------*/

    private class WorkerThread extends Thread {
        private boolean isAlive = true;

        @Override
        public void run() {
            while (isAlive) {
                try {
                    Task<V> task = taskPQ.dequeue();
                    Task<V>.TaskFuture taskFuture = (Task<V>.TaskFuture) task.getFuture();
                    taskFuture.result = task.getCallable().call();
                    taskFuture.isDone = true;
                    taskFuture.isDoneSemaphore.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (isPaused) {
                    synchronized (isPausedLock) {
                        if (isPaused) {
                            try {
                                isPausedLock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            threadArraySynchronization.lock();
            threadArray.remove(Thread.currentThread());
            threadArraySynchronization.unlock();
            synchronized (awaitTerminationLock) {
                awaitTerminationLock.notifyAll();
            }
        }
    }
}

