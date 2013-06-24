package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;
import org.jboss.netty.handler.codec.http.HttpMessage;

import java.nio.ByteBuffer;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 1:58 PM
 */
public class HttpMessageDecoder extends Mapper<Long, ByteBuffer, Long, HttpMessage> {

    @Override
    protected void map(Long sessionId, ByteBuffer inputValue, Context context) throws Exception {
        HttpMessage msg = parseMessage(sessionId);
        emit(sessionId, msg);
    }

    private HttpMessage parseMessage(Long inputKey) {
        return null;  // todo
    }
}
