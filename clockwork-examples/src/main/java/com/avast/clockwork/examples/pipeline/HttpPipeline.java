package com.avast.clockwork.examples.pipeline;

import com.avast.clockwork.Execution;
import com.avast.clockwork.TableAccumulator;

import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * User: zslajchrt
 * Date: 6/20/13
 * Time: 2:51 PM
 */
public class HttpPipeline {


    public static void main(String[] args) throws Exception {

//        Execution<Long, ByteBuffer, Long, ByteBuffer> pipeline =
//                Execution.newBuilder()
//                        .mapper(new HttpRequestDecoder())
//                        .mapper(new HttpResponseEncoder())
//                        .mapper(new MyHandler()) // custom business logic
//                        .accumulator(new ChannelWriter<Long>())
//                        .build();


        // Demonstrate the type-safety between transformers

        int maxContentLength = 10000;
        Execution<Long,ByteBuffer, Long, ByteBuffer> pipeline =
                Execution.newBuilder()
                .mapper(new HttpRequestDecoder())
                .reducer(new HttpChunkAggregator(maxContentLength))
                .mapper(new MyHandler())
                .mapper(new HttpResponseEncoder())
                .accumulator(new ChannelWriter<Long>())
                .build();

        Long rqCnt = 0l;
        ByteBuffer inputBuffer = null;
        Socket socket = null;

        WritableChannelContext channelContext = createContext(socket);
        pipeline.emit(rqCnt, inputBuffer, channelContext);

    }

    private static WritableChannelContext createContext(Socket socket) {
        return null;  // todo
    }
}
