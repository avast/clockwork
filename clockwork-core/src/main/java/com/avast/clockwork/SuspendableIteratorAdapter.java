package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;

import java.util.Iterator;

/**
 * User: slajchrt
 * Date: 9/15/12
 * Time: 12:04 PM
 */
public class SuspendableIteratorAdapter<T> implements SuspendableIterator<T> {

    private final Iterator<T> wrapped;

    public SuspendableIteratorAdapter(Iterator<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean hasNext() throws SuspendExecution {
        return wrapped.hasNext();
    }

    @Override
    public T next() throws SuspendExecution {
        return wrapped.next();
    }
}
