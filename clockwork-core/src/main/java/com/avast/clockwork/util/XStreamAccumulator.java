package com.avast.clockwork.util;

import com.avast.clockwork.TableAccumulator;
import com.avast.clockwork.Reducer;
import com.google.common.base.Function;
import com.thoughtworks.xstream.XStream;

import java.util.Comparator;

/**
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 6:49 PM
 */
public class XStreamAccumulator<K, V> extends TableAccumulator<K, V> {

    Function<String, Void> sender;

    public XStreamAccumulator(Function<String, Void> sender) {
        this(null, null, sender);
    }

    public XStreamAccumulator(Comparator<? super K> comparator, Reducer<K, V, K, V> combiner,
                              Function<String, Void> sender) {
        super(comparator, combiner);
        this.sender = sender;
    }

    @Override
    protected void drain() throws Exception {
        XStream xStream = new XStream();
        String ser = xStream.toXML(table);

        sender.apply(ser);

        clear();
    }
}
