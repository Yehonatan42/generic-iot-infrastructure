package iot.gatewayserver.threadpool;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.*;
import java.util.PriorityQueue;
import java.util.Comparator;

public class WaitablePQueue<E>  {
    private PriorityQueue<E> queue = null;
    private final Lock lock = new ReentrantLock();
    private final Semaphore semaphore = new Semaphore(0);

    public WaitablePQueue(Comparator<? super E> comparator) {
        queue = new PriorityQueue<>(comparator);
    }
    public WaitablePQueue() {
        queue = new PriorityQueue<>();
    }

    public void enqueue(E element) {
        this.lock.lock();
        this.queue.add(element);
        this.lock.unlock();
        this.semaphore.release();
    }

    public E dequeue() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.lock.lock();
        E returnVal = this.queue.poll();
        this.lock.unlock();

        return returnVal;
    }

    //time in miliseconds
    public E dequeue(long time) throws TimeoutException {
        boolean wasAcquired = false;
        E returnVal = null;

        try {
            long startTime = System.currentTimeMillis();

            if (semaphore.tryAcquire(1, time, TimeUnit.MILLISECONDS)) {
                long spareTime = System.currentTimeMillis() - startTime;

                if (lock.tryLock(spareTime, TimeUnit.MILLISECONDS)) {
                    returnVal = queue.poll();

                    lock.unlock();
                }
                else {
                    throw new TimeoutException();
                }
            }
            else {
                throw new TimeoutException();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return returnVal;
    }

    public boolean remove(E element) {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.lock.lock();
        boolean doesContain = queue.remove(element);
        if (!doesContain){
            this.semaphore.release();
        }
        this.lock.unlock();

        return doesContain;
    }
}
