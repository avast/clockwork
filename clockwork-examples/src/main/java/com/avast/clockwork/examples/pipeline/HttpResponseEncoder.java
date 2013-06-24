package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.nio.ByteBuffer;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 1:59 PM
 */
public class HttpResponseEncoder extends Mapper<Long, HttpResponse, Long, ByteBuffer> {

    @Override
    protected void map(Long sessionId, HttpResponse response, Context context) throws Exception {
        ByteBuffer respBuf = toByteBuffer(response);
        emit(sessionId, respBuf);
    }

    private ByteBuffer toByteBuffer(HttpResponse response) {
        return null;  // todo
    }
}
