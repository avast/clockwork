package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 1:50 PM
 */
public interface SuspendableIterator<T> {

    public boolean hasNext() throws SuspendExecution;

    public T next() throws SuspendExecution;

}
