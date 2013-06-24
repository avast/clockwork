package com.avast.clockwork.examples.naive.netty.front;

import com.avast.clockwork.*;
import com.avast.clockwork.examples.naive.BaseStat;
import com.avast.clockwork.examples.naive.InstanceMapperJSON;
import com.avast.clockwork.examples.naive.InstanceReducer;
import com.avast.clockwork.examples.naive.StatReducer;
import com.avast.clockwork.netty.HttpSender;
import com.avast.clockwork.util.XStreamAccumulator;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 2:17 PM
 */
public class NaiveFrontHandler extends SimpleChannelHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NaiveFrontHandler.class);

    private final AtomicLong feedCounter = new AtomicLong(0);
    private final Submitter<Long, Map<String, Object>, String, BaseStat> submitter;

    public NaiveFrontHandler(int flushModulo, URI termAggregatorURI, int maxExecConcurrency) throws Exception {

        Execution.Builder<Long, Map<String, Object>, String, BaseStat> execBuilder =
                Execution.newBuilder(ExecutionConfigBuilder.newBuilder().autoFlush(flushModulo))
                        .mapper(new InstanceMapperJSON())
                        .reducer(new InstanceReducer())
                        .accumulator(new XStreamAccumulator<String, BaseStat>(null, new StatReducer(),
                                new HttpSender(termAggregatorURI, "application/xml")));

        submitter = new Submitter<Long, Map<String, Object>, String, BaseStat>(execBuilder, maxExecConcurrency);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();

        ChannelBufferInputStream contentAsStream = new ChannelBufferInputStream(request.getContent());
        ObjectMapper jsonMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> batch = jsonMapper.readValue(contentAsStream, Map.class);

        HttpResponse response;
        if (batch.containsKey("feeds")) {
            response = learn(request, batch);
        } else {
            response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.BAD_REQUEST);
        }

        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response.getContent().readableBytes());
        Channels.write(ctx, e.getFuture(), response);
    }

    private HttpResponse learn(HttpRequest request, Map<String, Object> batch) throws Exception {
        HttpResponse response;
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> feeds = (List<Map<String, Object>>) batch.get("feeds");

        for (Map<String, Object> feed : feeds) {
            long counter = feedCounter.getAndIncrement();
            submitter.submit(counter, feed, null);
        }

        response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response.getContent().readableBytes());
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        LOGGER.error("Error in pipeline", e.getCause());

        Channel ch = e.getChannel();
        ch.close();
    }
}
