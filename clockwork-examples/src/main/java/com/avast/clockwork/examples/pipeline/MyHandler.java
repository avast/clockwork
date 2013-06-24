package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Context;
import com.avast.clockwork.Mapper;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 2:53 PM
 */
public class MyHandler extends Mapper<Long, HttpRequest, Long, HttpResponse> {

    @Override
    protected void map(Long sessionId, HttpRequest request, Context context) throws Exception {
        HttpResponse response = processRequest(request);
        emit(sessionId, response);
    }

    private HttpResponse processRequest(HttpRequest request) {
        return null;  // todo
    }
}
