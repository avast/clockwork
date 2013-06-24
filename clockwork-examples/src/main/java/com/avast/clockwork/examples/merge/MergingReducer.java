package com.avast.clockwork.examples.merge;

import com.avast.clockwork.Context;
import com.avast.clockwork.Reducer;
import com.avast.clockwork.SuspendableIterator;
import de.matthiasmann.continuations.SuspendExecution;

import java.util.Map;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 9:52 PM
 */
public class MergingReducer extends Reducer<Object, Object, Object, Object> {

    private final Map<Object, MergeFunction> mergers;

    public MergingReducer(Map<Object, MergeFunction> mergers) {
        this.mergers = mergers;
    }

    @Override
    protected void reduce(Object inputKey, SuspendableIterator<Object> inputValues, Context context) throws SuspendExecution, Exception {

        MergeFunction mergeFunctionForKey = mergers.get(inputKey);
        if (mergeFunctionForKey != null) {

            Object mergedValue = null;
            while (inputValues.hasNext()) {
                Object next = inputValues.next();
                if (mergedValue == null) {
                    mergedValue = next;
                } else {
                    mergedValue = mergeFunctionForKey.merge(mergedValue, next);
                }
            }

            emit(inputKey, mergedValue);
        }
    }

}
