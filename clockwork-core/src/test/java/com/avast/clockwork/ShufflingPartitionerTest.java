//package com.avast.clockwork;
//
//import com.avast.clockwork.shuffle.ShufflingAccumulator;
//import com.avast.clockwork.shuffle.SortedRecordsWriter;
//import com.avast.clockwork.shuffle.ValueCollectionFactory;
//import com.google.common.collect.Lists;
//import junit.framework.TestCase;
//
//import java.util.*;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.Executors;
//
///**
// * User: omer
// * Date: 6/12/12
// * Time: 1:04 PM
// */
//public class ShufflingPartitionerTest extends TestCase {
//
//    class DummyPartition{
//        public TreeMap<String, ? extends Iterable<String>> sortedRecords;
//    }
//    public static class StringComparator implements Comparator<String>{
//        @Override
//        public int compare(String o1, String o2) {
//            return o1.compareTo(o2);
//        }
//    }
//    public static class TreeSetFactory implements ValueCollectionFactory<String> {
//        @Override
//        public Collection<String> createValueCollection() {
//            return new TreeSet<String>();
//        }
//    }
//    class DummyRecordsWriter implements SortedRecordsWriter<String, String, DummyPartition> {
//
//        public final CountDownLatch latch = new CountDownLatch(1);
//
//        @Override
//        public void write(DummyPartition partition, TreeMap<String, ? extends Iterable<String>> sortedRecords) throws Exception {
//            partition.sortedRecords = sortedRecords;
//            latch.countDown();
//        }
//    }
//    class DymmyPartitionFunction implements PartitionFunction<String, String, DummyPartition>{
//
//        final DummyPartition partition;
//        public DymmyPartitionFunction(DummyPartition dummyPartition){
//            partition = dummyPartition;
//        }
//        @Override
//        public DummyPartition getPartition(String key, String value, int modulo) throws Exception {
//            return partition;
//        }
//    }
//
//    static final int NUM_THREADS = 4;
//    static final int SHUFFLE_DELAY = Integer.MAX_VALUE; //never
//
//    public void test_shuffle_on_threshold_exceeded() throws Exception {
//
//        final String[] inputKeyValues = {"a", "1", "a", "1", "a", "2", "b", "3", "b", "3", "b", "4" };
//
//        final int MAX_RECORDS = inputKeyValues.length/2;
//        DummyPartition partition = new DummyPartition();
//        DummyRecordsWriter writer = new DummyRecordsWriter();
//        List<ShufflingAccumulator<String, String, DummyPartition>> partitioners =
//                initPartitioners(NUM_THREADS, MAX_RECORDS, partition, writer);
//
//        int nPartitioner = 0;
//        for(int i=0; i<inputKeyValues.length-1; i+=2){
//            partitioners.get(nPartitioner).send(inputKeyValues[i], inputKeyValues[i+1], null);
//            nPartitioner = nPartitioner < NUM_THREADS-1 ? ++nPartitioner : 0 ;
//        }
//
//        writer.latch.await();
//
//        assertEquals("{a=[1, 2], b=[3, 4]}", partition.sortedRecords.toString());
//
//        //System.out.println(partition.sortedRecords.toString());
//        for(ShufflingAccumulator<String, String, DummyPartition> partitioner : partitioners){
//            partitioner.close();
//        }
//    }
//
//    List<ShufflingAccumulator<String, String, DummyPartition>> initPartitioners(
//            int numThreads, int maxRecord, DummyPartition partition, DummyRecordsWriter writer){
//        ShufflingAccumulator<String, String, DummyPartition> prototype = new ShufflingAccumulator(
//                new DymmyPartitionFunction(partition),
//                new StringComparator(),
//                new TreeSetFactory(),
//                writer,
//                NUM_THREADS/*modulu*/,
//                maxRecord/*max record count*/,
//                SHUFFLE_DELAY/*shuffle delay*/,
//                null /*combiner*/,
//                Executors.newScheduledThreadPool(1) /*executor*/
//                );
//
//        List<ShufflingAccumulator<String, String, DummyPartition>> ret = Lists.newArrayList();
//        for(int i=0; i<numThreads; i++){
//            ShufflingAccumulator<String, String, DummyPartition> partitioner = prototype.clone();
//            partitioner.start();
//            ret.add(partitioner);
//        }
//        return ret;
//    }
//}
