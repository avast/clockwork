package com.avast.clockwork.examples.iterator;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * User: zslajchrt
 * Date: 6/24/13
 * Time: 7:38 PM
 */

public class BufferedJoinedIterator<T> implements Iterator<T> {

    private final Iterator<T> joinedIterator;

    public BufferedJoinedIterator(Iterator<T>... iterators) {
        LinkedList<T> allElems = new LinkedList<T>();
        for (Iterator<T> iterator : iterators) {
            while (iterator.hasNext()) {
                T next = iterator.next();
                allElems.add(next);
            }
        }
        joinedIterator = allElems.iterator();
    }

    @Override
    public boolean hasNext() {
        return joinedIterator.hasNext();
    }

    @Override
    public T next() {
        return joinedIterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
