package com.avast.clockwork.examples.iterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 4:05 PM
 */

public class JoinedIterators<T> implements Iterator<T> {
    private final Iterator<Iterator<T>> iteratorIterator;
    private Iterator<T> currentIterator;

    public JoinedIterators(Iterator<T>... iterators) {
        iteratorIterator = Arrays.asList(iterators).iterator();
    }

    public boolean hasNext() {
        if (currentIterator == null) {
            if (!iteratorIterator.hasNext()) {
                return false;
            } else {
                currentIterator = iteratorIterator.next();
            }
        }
        while (!currentIterator.hasNext()) {
            if (iteratorIterator.hasNext()) {
                currentIterator = iteratorIterator.next();
            } else {
                currentIterator = null;
                return false;
            }
        }
        return true;
    }

    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentIterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
