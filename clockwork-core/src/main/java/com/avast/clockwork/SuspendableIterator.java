package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;

/**
 * This iterator is used in reducers.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 1:50 PM
 * @see Reducer
 */
public interface SuspendableIterator<T> {

    public boolean hasNext() throws SuspendExecution;

    public T next() throws SuspendExecution;

}
