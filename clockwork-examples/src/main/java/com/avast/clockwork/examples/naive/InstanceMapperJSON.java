package com.avast.clockwork.examples.naive;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * <p>
 * See: http://nickjenkin.com/blog/?p=85
 * </p>
 * User: zslajchrt
 * Date: 6/12/13
 * Time: 8:10 PM
 */
public class InstanceMapperJSON extends Mapper<Long, Map<String, Object>, String, Double> {

    @Override
    protected void map(Long rowNumber, Map<String, Object> feed, Context context) throws Exception {
        String target = (String) feed.get("class");
        emit("target_" + target, 1d);

        ArrayList<Object> attrs = (ArrayList<Object>) feed.get("attrs");
        int attrCnt = 0;
        for (Object a : attrs) {
            Double attr = ((Number) a).doubleValue();
            emit(target + "_" + attrCnt, attr);
            attrCnt++;
        }
    }
}
