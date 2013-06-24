package com.avast.clockwork.examples.naive;

import com.avast.clockwork.Context;
import com.avast.clockwork.Reducer;
import com.avast.clockwork.SuspendableIterator;
import de.matthiasmann.continuations.SuspendExecution;

/**
 * <p>
 *     See: http://nickjenkin.com/blog/?p=85
 * </p>
 * User: zslajchrt
 * Date: 6/12/13
 * Time: 8:20 PM
 */

public class InstanceReducer extends Reducer<String, Double, String, BaseStat> {

    @Override
    protected void reduce(String inputKey, SuspendableIterator<Double> inputValues, Context context)
            throws SuspendExecution, Exception {
        if (inputKey.startsWith("target_")) {
            int targetTotal = 0;
            while (inputValues.hasNext()) {
                Double value = inputValues.next();
                int targetCounter = value.intValue();
                targetTotal += targetCounter;
            }
            emit(inputKey, new BaseStat(targetTotal));

        } else {
            float sum = 0;
            float sumSq = 0;
            int count = 0;
            while (inputValues.hasNext()) {
                double attr = inputValues.next();
                sum += attr;
                sumSq += attr * attr;
                count++;
            }

            emit(inputKey, new AttrStat(sum, sumSq, count));

        }
    }
}
