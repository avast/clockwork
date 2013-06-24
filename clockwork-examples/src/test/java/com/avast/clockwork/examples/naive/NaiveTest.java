package com.avast.clockwork.examples.naive;

import com.avast.clockwork.*;
import com.google.common.collect.Maps;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * User: slajchrt
 * Date: 1/15/12
 * Time: 1:39 PM
 */
public class NaiveTest extends TestCase {

    private final static String fileName =
            "/Users/zslajchrt/Documents/Projects/Avast/open/clockwork-samples/src/test/resources/gender/feeds";
    //"C:\\WRC2\\clockwork\\src\\test\\data\\shakespeare\\feeds";

    public void testGender() throws Exception {
        learnGender();
    }

    public void learnGender() throws Exception {

        HashMap<String, Double> classPriors = Maps.newHashMap();
        classPriors.put("male", 0.5);
        classPriors.put("female", 0.5);

        StatAccumulator statPartitioner = new StatAccumulator(classPriors) {
            @Override
            public void flush() throws Exception {
                // do not clear the statistics
            }
        };
        Execution<Long, String, String, BaseStat> execution = Execution.newBuilder()
                .mapper(new InstanceMapper())
                .reducer(new InstanceReducer())
                .accumulator(statPartitioner).build();

        long counter = 0;
        BufferedReader reader =
                new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            execution.emit(counter++, line);
        }

        execution.close();

        Map.Entry<String, Double> sex = statPartitioner.guess(6, 130, 8);
        System.out.println(sex);
    }

}
