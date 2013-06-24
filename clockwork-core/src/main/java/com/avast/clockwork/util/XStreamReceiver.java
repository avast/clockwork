package com.avast.clockwork.util;

import com.avast.clockwork.Submitter;
import com.thoughtworks.xstream.XStream;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.util.Map;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 8:49 PM
 */
public class XStreamReceiver<K, V> {

    private final Submitter<K, V, ?, ?> submitter;

    public XStreamReceiver(Submitter<K, V, ?, ?> submitter) {
        this.submitter = submitter;
    }

    public void submit(HttpRequest request) {
        ChannelBufferInputStream content = new ChannelBufferInputStream(request.getContent());
        XStream xStream = new XStream();
        @SuppressWarnings("unchecked")
        Map<K, V> statTable = (Map<K, V>) xStream.fromXML(content);

        for (Map.Entry<K, V> statEntry : statTable.entrySet()) {
            submitter.submit(statEntry.getKey(), statEntry.getValue(), null);
        }
    }

}
