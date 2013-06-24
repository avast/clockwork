package com.avast.clockwork.netty;


import com.google.common.base.Function;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 3:03 PM
 */
public class HttpSender implements Function<String, Void> {

    private final String host;
    private final int port;
    private final HttpAsyncClient client;
    private final URI uri;
    private final String contentType;

    public HttpSender(URI uri, String contentType) {
        this.contentType = contentType;

        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "localhost" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if (scheme.equalsIgnoreCase("http")) {
                port = 80;
            } else if (scheme.equalsIgnoreCase("https")) {
                port = 443;
            }
        }

//        if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
        if (!scheme.equalsIgnoreCase("http")) {
            throw new IllegalArgumentException("Only HTTP(S) is supported.");
        }

        this.host = host;
        this.port = port;
        this.uri = uri;
        this.client = new HttpAsyncClient();
    }

    @Override
    public Void apply(String input) {
        HttpRequest request = new DefaultHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString());
        //request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

        ChannelBuffer content = ChannelBuffers.copiedBuffer(input, Charset.forName("utf-8"));
        int contentLength = content.readableBytes();
        request.setContent(content);

        request.setHeader(HttpHeaders.Names.CONTENT_LENGTH, contentLength);
        request.setHeader(HttpHeaders.Names.CONTENT_TYPE, contentType);
        request.setHeader(HttpHeaders.Names.HOST, host);
        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);

        client.send(request, host, port);

        return null;
    }
}
