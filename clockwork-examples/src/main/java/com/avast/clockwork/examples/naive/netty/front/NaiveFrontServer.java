package com.avast.clockwork.examples.naive.netty.front;

import com.avast.clockwork.netty.HttpServer;
import com.google.common.collect.Maps;

import java.net.URI;
import java.util.Map;

/**
 *
 * <p>Try it by
 * <pre>curl -v -X POST -H "Content-Type: application/json" -d '{"feeds": [{"class":"male","attrs":[1.23, 2.44444, 3]}]}' http://localhost:9999</pre>
 * or
 * <pre>curl -v -X POST -H "Content-Type: application/json" --data @clockwork-samples/src/test/resources/gender/feeds.json http://localhost:9999</pre>
 * </p>
 *
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 3:15 PM
 */
public class NaiveFrontServer extends HttpServer {

    public NaiveFrontServer(String localHost, int localPort, URI termAggregatorURI, int flushModulo, int maxExecConcurrency) throws Exception {
        super(localHost, localPort, new NaiveFrontHandler(flushModulo, termAggregatorURI, maxExecConcurrency));
    }

    public static void main(String[] args) throws Exception {
        String localHost = args[0];
        int localPort = Integer.parseInt(args[1]);
        final URI termAggregatorURI = URI.create(args[2]);
        final int flushModulo = 1;
        int maxExecConcurrency = 1;

        new NaiveFrontServer(localHost, localPort, termAggregatorURI, flushModulo, maxExecConcurrency).start();

        System.out.println("Naive Front server is running");
    }
}
