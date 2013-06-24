package com.avast.clockwork.util;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 3:38 PM
 */
public class IdentityMapper<K, V> extends Mapper<K, V, K, V> {

    @Override
    protected void map(K inputKey, V inputValue, Context context) throws Exception {
        emit(inputKey, inputValue);
    }
}
