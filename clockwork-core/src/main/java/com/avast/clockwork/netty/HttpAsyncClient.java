package com.avast.clockwork.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 2:39 PM
 */
public class HttpAsyncClient {

    private final ClientBootstrap bootstrap = new ClientBootstrap(
            new NioClientSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool()));

    {
        bootstrap.setPipelineFactory(new HttpClientPipelineFactory());
    }

    public void send(HttpRequest request, String host, int port) {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        // Wait until the connection attempt succeeds or fails.
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }

//        // Prepare the HTTP request.
//        HttpRequest request = new DefaultHttpRequest(
//        HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
//        request.setHeader(HttpHeaders.Names.HOST, host);
//        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
//        request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
//
//        // Set some example cookies.
//        CookieEncoder httpCookieEncoder = new CookieEncoder(false);
//        httpCookieEncoder.addCookie("my-cookie", "foo");
//        httpCookieEncoder.addCookie("another-cookie", "bar");
//        request.setHeader(HttpHeaders.Names.COOKIE, httpCookieEncoder.encode());

        // Send the HTTP request.
        channel.write(request);
    }

}
