package com.avast.clockwork.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 3:26 PM
 */
public class HttpServer {

    private final String localHost;
    private final int localPort;
    private final ServerBootstrap bootstrap;

    public HttpServer(String localHost, int localPort, final ChannelHandler appHandler) {
        this.localHost = localHost;
        this.localPort = localPort;

        ChannelFactory factory =
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(new ThreadFactory() {
                            @Override
                            public Thread newThread(final Runnable r) {
                                return new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("HttpServer thread started");
                                            r.run();
                                        } finally {
                                            System.out.println("HttpServer thread finished");
                                        }
                                    }
                                });
                            }
                        }),
                        Executors.newCachedThreadPool());

        bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("appHandler", appHandler);
                //pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

                return pipeline;
            }
        });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

    }

    public void start() throws Exception {
        InetSocketAddress localAddress =
                localHost == null ? new InetSocketAddress(localPort) : new InetSocketAddress(localHost, localPort);
        bootstrap.bind(localAddress);
    }

}
