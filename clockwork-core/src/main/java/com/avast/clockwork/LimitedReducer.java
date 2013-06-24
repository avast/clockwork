package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 6:50 PM
 */
public abstract class LimitedReducer<IK, IV, OK, OV> extends Reducer<IK, IV, OK, OV> {

    private final long limit;

    protected LimitedReducer(long limit) {
        this.limit = limit;
    }

    @Override
    final protected void reduce(IK inputKey, SuspendableIterator<IV> inputValues, Context context)
            throws SuspendExecution, Exception {
        reduce_(inputKey, new LimitedIterable<IV>(inputValues, limit), context);
    }

    abstract protected void reduce_(IK inputKey, SuspendableIterator<IV> inputValues, Context context)
            throws SuspendExecution, Exception;

}