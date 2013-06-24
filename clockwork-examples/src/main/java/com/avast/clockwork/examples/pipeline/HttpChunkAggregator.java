package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Context;
import com.avast.clockwork.Reducer;
import com.avast.clockwork.SuspendableIterator;
import de.matthiasmann.continuations.SuspendExecution;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 1:59 PM
 */
public class HttpChunkAggregator extends Reducer<Long, HttpMessage, Long, HttpRequest> {
    private final int maxContentLength;

    public HttpChunkAggregator(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    @Override
    protected void reduce(Long sessionId, SuspendableIterator<HttpMessage> inputMessages, Context context) throws SuspendExecution, Exception {

        HttpRequest aggregatedRequest = null;

        while (inputMessages.hasNext()) {
            HttpMessage nextMessage = inputMessages.next();
            if (nextMessage instanceof HttpRequest) {
                aggregatedRequest = (HttpRequest) nextMessage;
            } else if (nextMessage instanceof HttpChunk) {
                aggregatedRequest = aggregateChunks(aggregatedRequest, (HttpChunk) nextMessage);
            }
        }

        emit(sessionId, aggregatedRequest);
    }

    private HttpRequest aggregateChunks(HttpRequest aggregated, HttpChunk next) {
        return null; // todo
    }

}
