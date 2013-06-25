package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;

import java.util.NoSuchElementException;

/**
 * This iterator returns at most N elements. It can be used in situations when we want a reducer to aggregate at most
 * N values.
 * <p/>
 * User: slajchrt
 * Date: 1/12/12
 * Time: 6:44 PM
 */
public class LimitedIterator<T> implements SuspendableIterator<T> {

    private final SuspendableIterator<T> wrapped;
    private long loopLimit;

    /**
     * @param wrapped the wrapped iterator
     * @param loopLimit the max number of elements to iterate
     */
    public LimitedIterator(SuspendableIterator<T> wrapped, long loopLimit) {
        this.wrapped = wrapped;
        this.loopLimit = loopLimit;
    }

    private long counter;

    public boolean hasNext() throws SuspendExecution {
        return counter < loopLimit && wrapped.hasNext();
    }

    public T next() throws SuspendExecution {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        counter++;
        return wrapped.next();
    }
}
