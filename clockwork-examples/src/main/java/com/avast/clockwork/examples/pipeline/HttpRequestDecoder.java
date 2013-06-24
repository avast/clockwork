package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.nio.ByteBuffer;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 1:58 PM
 */
public class HttpRequestDecoder extends Mapper<Long, ByteBuffer, Long, HttpRequest> {

    @Override
    protected void map(Long sessionId, ByteBuffer inputValue, Context context) throws Exception {
        HttpRequest msg = parseRequest(sessionId);
        emit(sessionId, msg);
    }

    private HttpRequest parseRequest(Long inputKey) {
        return null;  // todo
    }
}
