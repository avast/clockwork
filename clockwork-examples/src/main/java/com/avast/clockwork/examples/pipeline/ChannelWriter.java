package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Accumulator;
import com.avast.clockwork.Context;

import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 3:02 PM
 */
public class ChannelWriter<K> implements Accumulator<K, ByteBuffer> {

    @Override
    public void send(K key, ByteBuffer buffer, Context context) throws Exception {
        WritableByteChannel writableChannel = ((WritableChannelContext) context).getWritableChannel();
        writableChannel.write(buffer);
    }

    @Override
    public void close() throws Exception {
        // todo
    }

    @Override
    public void flush(K key) throws Exception {
        // todo
    }

    @Override
    public void flush() throws Exception {
        // todo
    }
}
