package com.avast.clockwork.examples.naive.netty.back;

import com.avast.clockwork.Execution;
import com.avast.clockwork.ExecutionConfigBuilder;
import com.avast.clockwork.Submitter;
import com.avast.clockwork.examples.naive.BaseStat;
import com.avast.clockwork.examples.naive.StatAccumulator;
import com.avast.clockwork.util.IdentityMapper;
import com.avast.clockwork.util.XStreamReceiver;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 2:17 PM
 */
public class NaiveAggregatorHandler extends SimpleChannelHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NaiveAggregatorHandler.class);

    private final StatAccumulator statAccumulator;
    private final XStreamReceiver xStreamReceiver;

    public NaiveAggregatorHandler(int flushModulo, Map<String, Double> classPriors, int maxExecConcurrency) throws Exception {
        statAccumulator = new StatAccumulator(classPriors);

        Execution.Builder<String, BaseStat, String, BaseStat> execBuilder = Execution.newBuilder(ExecutionConfigBuilder.newBuilder()
                .autoFlush(flushModulo))
                .mapper(new IdentityMapper<String, BaseStat>())
                .accumulator(statAccumulator);
        Submitter<String, BaseStat, String, BaseStat> submitter =
                new Submitter<String, BaseStat, String, BaseStat>(execBuilder, maxExecConcurrency);
        xStreamReceiver = new XStreamReceiver<String, BaseStat>(submitter);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();

        HttpResponse response;
        if (request.getUri().endsWith("guess")) {

            ChannelBufferInputStream contentAsStream = new ChannelBufferInputStream(request.getContent());
            ObjectMapper jsonMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> batch = jsonMapper.readValue(contentAsStream, Map.class);

            if (batch.containsKey("attrs")) {
                response = guess(request, batch);
            } else {
                response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.BAD_REQUEST);
            }
        } else {
            response = learn(request);
        }

        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response.getContent().readableBytes());
        Channels.write(ctx, e.getFuture(), response);
    }

    private HttpResponse guess(HttpRequest request, Map<String, Object> batch) {
        HttpResponse response;
        @SuppressWarnings("unchecked")
        List<Number> guessAttrs = (List<Number>) batch.get("attrs");

        Map.Entry<String, Double> guessed = statAccumulator.guess(guessAttrs);
        String responseText = "{\"sex\":\"" + guessed.getKey() + "\", \"p\":" + guessed.getValue() + "}";
        ChannelBuffer responseContent = ChannelBuffers.copiedBuffer(responseText.getBytes());

        response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8");
        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response.getContent().readableBytes());
        response.setContent(responseContent);
        return response;
    }

    private HttpResponse learn(HttpRequest request) throws Exception {
        HttpResponse response;

        xStreamReceiver.submit(request);

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
