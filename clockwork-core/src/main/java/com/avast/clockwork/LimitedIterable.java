package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;

import java.util.NoSuchElementException;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 6:44 PM
 */
public class LimitedIterable<T> implements SuspendableIterator<T> {

    private final SuspendableIterator<T> wrapped;
    private long loopLimit;

    public LimitedIterable(SuspendableIterator<T> wrapped, long loopLimit) {
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
