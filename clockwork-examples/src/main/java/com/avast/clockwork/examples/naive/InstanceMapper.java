package com.avast.clockwork.examples.naive;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * <p>
 *     See: http://nickjenkin.com/blog/?p=85
 * </p>
 * User: zslajchrt
 * Date: 6/12/13
 * Time: 8:10 PM
 */

public class InstanceMapper extends Mapper<Long, String, String, Double> {

    private final Splitter splitter = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults();

    @Override
    protected void map(Long rowNumber, String instanceRow, Context context) throws Exception {
        int attrCnt = 0;
        String target = null;
        for (String cell: splitter.split(instanceRow)) {
            if (target == null) {
                target = cell;
                emit("target_" + target, 1d);
            } else {
                double attr = Double.parseDouble(cell);
                emit(target + "_" + attrCnt, attr);
                attrCnt++;
            }
        }
    }
}
