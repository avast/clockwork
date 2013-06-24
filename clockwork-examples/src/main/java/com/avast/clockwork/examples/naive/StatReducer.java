package com.avast.clockwork.examples.naive;

import com.avast.clockwork.Context;
import com.avast.clockwork.Reducer;
import com.avast.clockwork.SuspendableIterator;
import de.matthiasmann.continuations.SuspendExecution;

/**
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 7:07 PM
 */
public class StatReducer extends Reducer<String, BaseStat, String, BaseStat> {

    @Override
    protected void reduce(String inputKey, SuspendableIterator<BaseStat> inputValues, Context context) throws SuspendExecution, Exception {
        // the flush modulo should correlate with the number of the front (sending) servers

        BaseStat aggrStat = null;
        while (inputValues.hasNext()) {
            BaseStat next = inputValues.next();
            if (aggrStat == null)
                aggrStat = next;
            else
                aggrStat = aggrStat.update(next);
        }

        emit(inputKey, aggrStat);
    }
}
