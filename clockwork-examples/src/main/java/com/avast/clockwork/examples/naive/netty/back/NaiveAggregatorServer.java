package com.avast.clockwork.examples.naive.netty.back;

import com.avast.clockwork.netty.HttpServer;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 * <p>Try it by
 * <pre>curl -v -X POST -H "Content-Type: application/json" -d '{"attrs":[5, 100, 6]}' http://localhost:10000/guess</pre>
 * </p>
 *
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 3:15 PM
 */
public class NaiveAggregatorServer extends HttpServer {

    public NaiveAggregatorServer(String localHost, int localPort, Map<String, Double> classPriors, int flushModulo, int maxExecConcurrency) throws Exception {
        super(localHost, localPort, new NaiveAggregatorHandler(flushModulo, classPriors, maxExecConcurrency));
    }

    public static void main(String[] args) throws Exception {
        String localHost = args[0];
        int localPort = Integer.parseInt(args[1]);
        final int flushModulo = 1;
        int maxExecConcurrency = 1;
        final Map<String, Double> classPriors = Maps.newHashMap();
        classPriors.put("male", 0.5);
        classPriors.put("female", 0.5);

        new NaiveAggregatorServer(localHost, localPort, classPriors, flushModulo, maxExecConcurrency).start();

        System.out.println("Naive Accumulator server is running");
    }
}
