package com.avast.clockwork.examples.wc;

import com.avast.clockwork.Context;
import com.avast.clockwork.Reducer;
import com.avast.clockwork.SuspendableIterator;
import de.matthiasmann.continuations.SuspendExecution;

/**
* Created by IntelliJ IDEA.
* User: slajchrt
* Date: 1/15/12
* Time: 1:42 PM
* To change this template use File | Settings | File Templates.
*/ //static class PercentilesReducer extends LimitedReducer<Long, Integer, Long, Integer> {
public class PercentilesReducer extends Reducer<Long, Integer, Long, Integer> {
//        PercentilesReducer(long limit) {
//            super(limit);
//        }

    @Override
    protected void reduce(Long prevalence, SuspendableIterator<Integer> hits, Context context)
            throws SuspendExecution, Exception {
        int hitCounter = 0;
        while (hits.hasNext()) {
            hitCounter += hits.next();
        }

        emit(prevalence, hitCounter);
    }
}
