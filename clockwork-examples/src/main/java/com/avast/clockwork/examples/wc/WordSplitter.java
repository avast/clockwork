package com.avast.clockwork.examples.wc;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;
import com.google.common.base.Splitter;

/**
* Created by IntelliJ IDEA.
* User: slajchrt
* Date: 1/15/12
* Time: 1:41 PM
* To change this template use File | Settings | File Templates.
*/

public class WordSplitter extends Mapper<Long, String, String, Long> {
    @Override
    protected void map(Long inputKey, String inputValue, Context context) throws Exception {
        Iterable<String> splits = Splitter.on(" ").trimResults().split(inputValue);
        for (String split : splits) {
            emit(split, 1L);
        }
    }
}
