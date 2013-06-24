package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Context;

import java.nio.channels.WritableByteChannel;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 3:06 PM
 */
public interface WritableChannelContext extends Context {

    WritableByteChannel getWritableChannel();

}
