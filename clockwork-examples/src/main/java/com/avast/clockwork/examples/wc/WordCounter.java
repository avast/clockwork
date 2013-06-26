package com.avast.clockwork.examples.wc;

import com.avast.clockwork.Context;
import com.avast.clockwork.Reducer;
import com.avast.clockwork.SuspendableIterator;
import de.matthiasmann.continuations.SuspendExecution;

/**
* Created by IntelliJ IDEA.
* User: slajchrt
* Date: 1/15/12
* Time: 1:41 PM
* To change this template use File | Settings | File Templates.
*/

public class WordCounter extends Reducer<String, Long, String, Long> {
    @Override
    protected void reduce(String word, SuspendableIterator<Long> occurrences, Context context)
            throws SuspendExecution, Exception {
        long counter = 0;
        while (occurrences.hasNext()) {
            counter += occurrences.next();
        }
        emit(word, counter);
    }
}
