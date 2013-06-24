package com.avast.clockwork.examples.iterator;

import de.matthiasmann.continuations.CoIterator;
import de.matthiasmann.continuations.SuspendExecution;

import java.util.Iterator;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 4:05 PM
 */

public class CoJoinedIterators<T> extends CoIterator<T> {
    private final Iterator<T>[] iterators;

    public CoJoinedIterators(Iterator<T>... iterators) {
        this.iterators = iterators;
    }

    protected void run() throws SuspendExecution {
        for (Iterator<T> iterator : iterators) {
            while (iterator.hasNext()) {
                T next = iterator.next();
                produce(next);
            }
        }
    }
}
