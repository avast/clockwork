package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 2:25 PM
 */

public class CountCombiner<K> extends LimitedReducer<K, Long, K, Long> {

    /**
     * @param limit the maximum number of iterations in the <code>reduce</code> method.
     */
    public CountCombiner(int limit) {
        super(limit);
    }

    @Override
    protected void reduce_(K key, SuspendableIterator<Long> counters, Context context) throws Exception {
        long counterSum = 0;
        while (counters.hasNext()) {
            counterSum += counters.next();
        }

        emit(key, counterSum);
    }
}
