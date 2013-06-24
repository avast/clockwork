package com.avast.clockwork.examples.merge;

import com.avast.clockwork.Execution;
import com.avast.clockwork.TableAccumulator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 10:37 PM
 */
public class Merger {

    private final MergingReducer mergingReducer;

    public Merger(MergingReducer mergingReducer) {
        this.mergingReducer = mergingReducer;
    }

    public Map merge(Map... parts) throws Exception {
        TableAccumulator accumulator = new TableAccumulator<String, Object>();
        Execution execution = Execution.newBuilder()
                .reducer(mergingReducer)
                .accumulator(accumulator)
                .build();

        for (Map part : parts) {
            Set<Map.Entry> partEntries = part.entrySet();
            for (Map.Entry partEntry : partEntries) {
                execution.emit(partEntry.getKey(), partEntry.getValue());
            }
        }

        execution.close();

        return accumulator.getTable();
    }

    public static void main(String[] args) throws Exception {
        Merger merger = MergerBuilder.newBuilder()
                .keyMerger("children", new MergeFunction<Integer>() {
                    @Override
                    public Integer merge(Integer children1, Integer children2) throws Exception {
                        return Math.max(children1, children2);
                    }
                })
                .keyMerger("salary", new MergeFunction<BigDecimal>() {
                    @Override
                    public BigDecimal merge(BigDecimal salary1, BigDecimal salary2) throws Exception {
                        return salary1.add(salary2);
                    }
                })
                .keyMerger("birth", new MergeFunction<Date>() {
                    @Override
                    public Date merge(Date value1, Date value2) throws Exception {
                        return new Date(Math.min(value1.getTime(), value2.getTime()));
                    }
                })
                .build();

        HashMap part0 = new HashMap();
        part0.put("children", 1);
        part0.put("salary", new BigDecimal(10000));
        part0.put("birth", new Date(/**/));

        HashMap part1 = new HashMap();
        part1.put("children", 2);
        part1.put("salary", new BigDecimal(15000));

        HashMap part2 = new HashMap();
        part2.put("salary", new BigDecimal(30000));
        part2.put("birth", new Date(/**/));

        Map mergedParts = merger.merge(part0, part1, part2);
        System.out.println(mergedParts);
    }

}
