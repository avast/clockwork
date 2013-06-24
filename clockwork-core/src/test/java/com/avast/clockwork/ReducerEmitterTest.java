package com.avast.clockwork;

import de.matthiasmann.continuations.CoIterator;
import de.matthiasmann.continuations.SuspendExecution;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 9:10 PM
 */
public class ReducerEmitterTest extends TestCase {

    final String[] inputFiles = {
            "/Users/slajchrt/Projects/avast/clockwork/src/test/data/shakespeare/all-comedies",
            "/Users/slajchrt/Projects/avast/clockwork/src/test/data/shakespeare/all-tragedies",
            "/Users/slajchrt/Projects/avast/clockwork/src/test/data/shakespeare/all-poetry",
            "/Users/slajchrt/Projects/avast/clockwork/src/test/data/shakespeare/all-histories",
    };

    //"/Users/slajchrt/Projects/avast/clockwork/src/test/data/shakespeare/all";

    public void testFeed() throws Exception {

    }

//    public void feedMR() throws Exception {
//        WordSplitter wcMapper = new WordSplitter();
//        WordCounter wcReducer = new WordCounter();
//        PercentilesMapper percentilesMapper = new PercentilesMapper();
//        PercentilesReducer percentilesReducer = new PercentilesReducer();
//
//        // The phases are constructed in the reverse order
//
//        /////////////////////////////////////
//        // Phase 3: (Prevalence, Partial Hits) -> (Prevalence, Hits)
//
//        final CountDownLatch finalLatch = new CountDownLatch(1);
//
//        final Execution.Builder<Long, Integer, Long, Integer> execBuilder3 = Execution.newBuilder()
//                .reducer(percentilesReducer)
//                .aggregator(new PrintingTableAccumulator(finalLatch));
//
//        LoadBalancer<Long, Integer> balancer3 = new LoadBalancer<Long, Integer>("", 1, Integer.MAX_VALUE, execBuilder3, new LongPartFunction());
//
//        /////////////////////////////////////
//        // Phase 2: (Word, Partial Prevalence) -> (Prevalence, Partial Hits)
//
//        int nThreads = 4;
//
//        Execution.Builder<String, Long, Long, Integer> execBuilder2 = Execution.newBuilder()
//                .reducer(wcReducer)
//                .mapper(percentilesMapper)
//                .reducer(percentilesReducer)
//                .aggregator(balancer3);
//
//        LoadBalancer<String, Long> balancer2 = new LoadBalancer<String, Long>("", nThreads, Integer.MAX_VALUE, execBuilder2,
//                new HashCodePartFunction<String, Long>());
//
//        /////////////////////////////////////
//        // Phase 1: (LineNo, Line) -> (Word, Partial Prevalence)
//
//        final Execution.Builder<Long, String, String, Long> execBuilder1 = Execution.newBuilder()
//                .mapper(wcMapper)
//                .reducer(wcReducer)
//                .aggregator(balancer2);
//
//        final LoadBalancer<Long, String> balancer1 = new LoadBalancer<Long, String>("", nThreads, Integer.MAX_VALUE, execBuilder1,
//                new LongPartFunction());
//
//        //////////////////////
//        // The front feeder (front load balancer) running in 4 threads
//
//        // todo: Use Submitter instead of LoadBalancer
////        for (int i = 0; i < nThreads; i++) {
////            final String fileName = inputFiles[i % inputFiles.length];
////            LoadBalancerTask<Long, String> lbTask =
////                    new LoadBalancerTask<Long, String>(balancer1, new FileInputIterator(fileName), null);
////
////            new Thread(lbTask).start();
////        }
//
//        finalLatch.await();
//    }

    private synchronized void print(String s) {
        System.out.println(s);
    }

    class FileInputIterator extends CoIterator<KeyValue<Long, String>> implements Serializable {
        private final String fileName;

        FileInputIterator(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected void run() throws SuspendExecution {
            try {
                long counter = 0;
                BufferedReader reader =
                        new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = reader.readLine()) != null) {
                    produce(new KeyValue<Long, String>(counter++, line));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class PrintingTableAccumulator extends ForwardingAccumulator<Long, Integer> {
        private final CountDownLatch finalLatch;

        public PrintingTableAccumulator(CountDownLatch finalLatch) {
            this.finalLatch = finalLatch;
        }

        @Override
        protected void send_(Long key, Integer value) throws Exception {
            print(key + ":" + value);
        }

        @Override
        public void close() throws Exception {
            finalLatch.countDown();
            super.close();
        }

    }
}
