package com.avast.clockwork.examples.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 4:05 PM
 */

public class PipeJoinedIterators<T> implements Iterator<T>, Runnable {
    private final Iterator<T>[] iterators;
    private final ArrayBlockingQueue<Object> pipe = new ArrayBlockingQueue<Object>(1);
    private static final Object STOP = new Object();

    public PipeJoinedIterators(Iterator<T>... iterators) {
        this.iterators = iterators;
        new Thread(this).start();
    }

    private void run_() throws InterruptedException {
        for (Iterator<T> iterator : iterators) {
            while (iterator.hasNext()) {
                T next = iterator.next();
                pipe.put(next);
            }
        }
        pipe.offer(STOP);
    }

    public boolean hasNext() {
        Object e;
        while ((e = pipe.peek()) == null);
        return e != STOP;
    }

    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return (T)pipe.take();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void run() {
        try {
            run_();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
