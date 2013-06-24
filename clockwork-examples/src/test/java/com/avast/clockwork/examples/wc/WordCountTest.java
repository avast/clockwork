package com.avast.clockwork.examples.wc;

import com.avast.clockwork.Execution;
import com.avast.clockwork.ForwardingAccumulator;
import com.avast.clockwork.TableAccumulator;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * User: slajchrt
 * Date: 1/15/12
 * Time: 1:39 PM
 */
public class WordCountTest extends TestCase {

    private final static String fileName =
            "/Users/zslajchrt/Documents/Projects/Avast/trunk/clockwork/src/test/data/shakespeare/all";
    //"C:\\WRC2\\clockwork\\src\\test\\data\\shakespeare\\all";

    public void testPercentiles() throws Exception {
        percentilesMR();
    }

    public void percentilesMR() throws Exception {

//        Execution<Long, String, Long, Integer> execution = Execution.newBuilder()
//                .mapper(new WordSplitter())
//                .reducer(new WordCounter())
//                .mapper(new PercentilesMapper())
//                .reducer(new PercentilesReducer())
//                .accumulator(new ForwardingAccumulator<Long, Integer>() {
//                    @Override
//                    protected void send_(Long key, Integer value) throws Exception {
//                        System.out.println(key + ":" + value);
//                    }
//
//                }).build();

        Execution<Long, String, String, Long> execution = Execution.newBuilder()
                .mapper(new WordSplitter())
                .reducer(new WordCounter())
                .accumulator(new TableAccumulator<String, Long>()).build();

        long counter = 0;
        BufferedReader reader =
                new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            execution.emit(counter++, line);
        }

        execution.close();

        System.out.println("END");
    }

}
