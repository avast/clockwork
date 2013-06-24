package com.avast.clockwork.examples.wc;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;

/**
* Created by IntelliJ IDEA.
* User: slajchrt
* Date: 1/15/12
* Time: 1:41 PM
* To change this template use File | Settings | File Templates.
*/
public class PercentilesMapper extends Mapper<String, Long, Long, Integer> {
    @Override
    protected void map(String word, Long prevalence, Context context) throws Exception {
        emit(prevalence, 1);
    }
}
