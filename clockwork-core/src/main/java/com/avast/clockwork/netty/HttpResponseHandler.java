package com.avast.clockwork.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 2:48 PM
 */
public class HttpResponseHandler extends SimpleChannelUpstreamHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpResponse response = (HttpResponse) e.getMessage();

        System.out.println("STATUS: " + response.getStatus());
        System.out.println("VERSION: " + response.getProtocolVersion());
        System.out.println();

        if (!response.getHeaderNames().isEmpty()) {
            for (String name : response.getHeaderNames()) {
                for (String value : response.getHeaders(name)) {
                    System.out.println("HEADER: " + name + " = " + value);
                }
            }
            System.out.println();
        }

        ChannelBuffer content = response.getContent();
        if (content.readable()) {
            System.out.println("CONTENT {");
            System.out.println(content.toString(CharsetUtil.UTF_8));
            System.out.println("} END OF CONTENT");
        }
    }
}
