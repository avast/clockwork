package com.avast.clockwork.shuffle;

import com.avast.clockwork.*;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: slajchrt
 * Date: 6/7/12
 * Time: 12:44 PM
 */
public final class ShufflingAccumulator<K, V, P> extends AbstractPartitioner<K, V, P> {

    private static Logger LOGGER = LoggerFactory.getLogger(ShufflingAccumulator.class);

    private static class Shuffler<K, V, P> {

        private final StopRunnable STOP_MARK = new StopRunnable();
        private final CountDownLatch stopLatch = new CountDownLatch(1);

        private class StopRunnable implements Runnable {
            @Override
            public void run() {
                try {
                    Preconditions.checkArgument(stopLatch.getCount() == 1, "Stop mark cannot be reused");

                    LOGGER.debug("Stopping shuffling");
                    started.getAndSet(false);

                    if (shuffleTimer != null) {
                        shuffleTimer.shutdown();
                    }

                } finally {
                    stopLatch.countDown();
                }
            }

            public void await() throws InterruptedException {
                while (!stopLatch.await(1000, TimeUnit.MILLISECONDS)) {
                    //LOGGER.info("Stopping... Still {} requests in queue", shuffleRequests.size());
                    LOGGER.info("Stopping... Still {} requests in queue");
                }
            }
        }

        private final long maxRecordCount;
        private final AtomicLong recordCounter = new AtomicLong();
        private final List<ShufflingAccumulator<K, V, P>> partitioners = Lists.newLinkedList();

        //private final BlockingQueue<Runnable> shuffleRequests;
        private final long shuffleDelay;
        private final AtomicBoolean started = new AtomicBoolean();
        volatile private boolean debugEnabled;

        private final SortedRecordsWriter<K, V, P> recordsWriter;
        private final ScheduledExecutorService shuffleExecutor;
        private final ScheduledExecutorService shuffleTimer;

//        = Executors.newScheduledThreadPool(1,
//                new NamedThreadFactory("ShufflingPartitionerScheduler"))

        private final Comparator<K> keyComparator;
        private final ValueCollectionFactory<V> valueCollectionFactory;

        private final Reducer<K, V, K, V> combiner;

        private Shuffler(long maxRecordCount, long shuffleDelay, SortedRecordsWriter<K, V, P> recordsWriter,
                         Comparator<K> keyComparator, ValueCollectionFactory<V> valueCollectionFactory, Reducer<K, V, K, V> combiner,
                         ScheduledExecutorService shuffleExecutor) {
            this.maxRecordCount = maxRecordCount;
            this.shuffleDelay = shuffleDelay;
            this.recordsWriter = recordsWriter;
            this.keyComparator = keyComparator;
            this.valueCollectionFactory = valueCollectionFactory;
            this.combiner = combiner;
            this.shuffleExecutor = shuffleExecutor;
            this.shuffleTimer = shuffleDelay >= 0 ?
                    //Executors.newScheduledThreadPool(1, new NamedThreadFactory("ShuffleTimer")) :
                    Executors.newScheduledThreadPool(1) :
                    null;
        }

        private void checkStarted() throws Exception {
            boolean prevValue = started.getAndSet(true);
            if (!prevValue) {
                start();
            }
        }

        private void start() {
//            // Start the "infinite" loop that pulls shuffle request form the queue
//            new Thread("ShufflingAccumulator Task Runner (maxRecordCount:" + maxRecordCount + ")") {
//                @Override
//                public void run() {
//
//                    try {
//                        while (true) {
//                            try {
//                                Runnable runnable = shuffleRequests.take();
//                                runnable.run();
//
//                                if (STOP_MARK == runnable) {
//                                    break;
//                                }
//
//                            } catch (Throwable t) {
//                                LOGGER.error("Error in shuffler loop", t);
//                            } finally {
//                                if (maxRecordCount == 1) {
//                                    LOGGER.debug("Shuffle job finished");
//                                }
//                            }
//                        }
//                    } finally {
//                        //System.out.println("ShufflingAccumulator: LEAVING LOOP");
//                    }
//
//                }
//            }.start();

            if (shuffleTimer != null) {
                // Schedule shuffling
                shuffleTimer.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Pipeline (schedule) the shuffle task through the central (single-task-per-time) executor
                            shuffleExecutor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        shuffle();
                                    } catch (Exception e) {
                                        LOGGER.error("Error during shuffling", e);
                                    }
                                }
                            });
                        } catch (Throwable t) {
                            LOGGER.error("Error during shuffling submission", t);
                        }
                    }
                }, shuffleDelay, shuffleDelay, TimeUnit.MILLISECONDS);
            } else {
                LOGGER.info("No scheduled shuffling");
            }

        }

        private void stop() throws Exception {
            shuffle();
            //shuffleRequests.put(STOP_MARK);
            shuffleExecutor.submit(STOP_MARK);
            STOP_MARK.await();
        }

        private void incrementVersion() throws Exception {
            long curVersion = recordCounter.incrementAndGet();

            if (maxRecordCount > 0 && curVersion % maxRecordCount == 0) {
                shuffle();
            }
        }

        private void registerPartitionerClone(ShufflingAccumulator<K, V, P> partitioner) {
            partitioners.add(partitioner);
        }

        private synchronized void shuffle() throws Exception {
            checkStarted();

            // Swap local caches
            final List<Map<P, TreeMap<K, Collection<V>>>> localCaches = Lists.newLinkedList();
            for (ShufflingAccumulator<K, V, P> partitioner : partitioners) {
                localCaches.add(partitioner.swapCaches());
            }

            if (maxRecordCount == 1) {
                LOGGER.debug("shuffling");
            }

            //shuffleRequests.put(new Runnable() {
            shuffleExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        doShuffle(localCaches);
                    } catch (Throwable t) {
                        LOGGER.error("Error during shuffling", t);
                    }
                }
            });
            LOGGER.debug("Shuffling scheduled (MaxRecCnt: {})", maxRecordCount);
        }

        private void doShuffle(List<Map<P, TreeMap<K, Collection<V>>>> localCaches) throws Exception {
            LOGGER.debug("Swapping caches (MaxRecCnt: {})", maxRecordCount);
            try {
                shuffleLocalCaches(localCaches);
            } finally {
                LOGGER.debug("Swapping caches finished (MaxRecCnt: {})", maxRecordCount);
            }
        }

        private void shuffleLocalCaches(List<Map<P, TreeMap<K, Collection<V>>>> localCaches) throws Exception {
            Map<P, TreeMap<K, Collection<V>>> aggrCache = new FastMap<P, TreeMap<K, Collection<V>>>();
            for (Map<P, TreeMap<K, Collection<V>>> localCache : localCaches) {
                for (Map.Entry<P, TreeMap<K, Collection<V>>> localCacheEntry : localCache.entrySet()) {
                    TreeMap<K, Collection<V>> existing = aggrCache.get(localCacheEntry.getKey());

                    if (existing == null) {
                        aggrCache.put(localCacheEntry.getKey(), localCacheEntry.getValue());
                    } else {
                        aggrCache.put(localCacheEntry.getKey(), mergeTables(existing, localCacheEntry.getValue()));
                    }

                }
            }

            flushCache(aggrCache);
        }

        private void flushCache(Map<P, TreeMap<K, Collection<V>>> aggrCache) throws InterruptedException {
            final CountDownLatch latch = new CountDownLatch(aggrCache.size());
            for (Map.Entry<P, TreeMap<K, Collection<V>>> aggrCacheEntry : aggrCache.entrySet()) {

                final P partition = aggrCacheEntry.getKey();
                final TreeMap<K, Collection<V>> sortedRecords = aggrCacheEntry.getValue();

                new Thread("ShufflingAccumulator-Writer (maxRecordCount:" + maxRecordCount + ")") {
                    @Override
                    public void run() {
                        try {
                            LOGGER.debug("writing sorted records. records count: {}", sortedRecords.keySet().size());

                            // call the combiner
                            if (combiner != null) {
                                TreeMap<K, Collection<V>> combinedRecords = combineRecords(sortedRecords);
                                recordsWriter.write(partition, combinedRecords);
                            } else {
                                recordsWriter.write(partition, sortedRecords);
                            }

                        } catch (Throwable t) {
                            LOGGER.error("Error during writing sorted records", t);
                        } finally {
                            latch.countDown();
                        }
                    }
                }.start();
            }

            latch.await();
        }

        private TreeMap<K, Collection<V>> combineRecords(TreeMap<?, ?> sortedRecords) throws Exception {
            TreeMap<K, Collection<V>> combinedRecords = Maps.newTreeMap(keyComparator);
            for (Map.Entry<?, ?> e : sortedRecords.entrySet()) {
                final Collection<V> combinedValues = valueCollectionFactory.createValueCollection();
                final Object inputKey = e.getKey();
                final Collection inputValues = (Collection)e.getValue();
                Execution.execute(inputKey,
                        inputValues.iterator(),
                        null,
                        new Function<Map.Entry<K, V>, Void>() {
                            @Override
                            public Void apply(Map.Entry<K, V> input) {
                                K key = input.getKey();
                                Preconditions.checkNotNull(key, "Combiner may not emit null key");
                                //Preconditions.checkArgument(key.equals(inputKey), "Combiner may not emit a different key from the input one");

                                combinedValues.add(input.getValue());

                                LOGGER.debug("Combined value {} for key {}", input.getValue(), key);

                                return null;
                            }
                        }, combiner);
            }
            return combinedRecords;
        }

        private TreeMap<K, Collection<V>> mergeTables(TreeMap<K, Collection<V>> first, TreeMap<K, Collection<V>> second) {
            for (Map.Entry<K, Collection<V>> secondTableEntry : second.entrySet()) {
                Collection<V> valuesFromFirst = first.get(secondTableEntry.getKey());
                if (valuesFromFirst != null) {
                    valuesFromFirst.addAll(secondTableEntry.getValue());
                } else {
                    first.put(secondTableEntry.getKey(), secondTableEntry.getValue());
                }
            }

            return first;
        }

    }

    private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private final Shuffler<K, V, P> shuffler;
    private volatile boolean isPrototype;

    private volatile Map<P, TreeMap<K, Collection<V>>> cache = Maps.newHashMap();
//    private final Map<P, TreeMap<K, Collection<V>>> cache2 = Maps.newHashMap();
//    private volatile boolean currentCacheFirst;

    /**
     * Just for cloning.
     */
    private final Accumulator<K, V> metaAccumulator = new Accumulator<K, V>() {

        @Override
        public void send(K key, V value, Context context) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flush(K key) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flush() throws Exception {
        }
    };

    public ShufflingAccumulator(PartitionFunction<K, V, P> partitionFunction, Comparator<K> keyComparator,
                                ValueCollectionFactory<V> valueCollectionFactory, SortedRecordsWriter<K, V, P> recordsWriter,
                                int modulo, long maxRecordCount, long shuffleDelay, Reducer<K, V, K, V> combiner,
                                ScheduledExecutorService shuffleExecutor) {
        super(partitionFunction, modulo);
        Preconditions.checkNotNull(keyComparator);
        Preconditions.checkNotNull(valueCollectionFactory);
        Preconditions.checkNotNull(recordsWriter);
        Preconditions.checkNotNull(shuffleExecutor);
        shuffler = new Shuffler<K, V, P>(maxRecordCount, shuffleDelay, recordsWriter, keyComparator,
                valueCollectionFactory, combiner, shuffleExecutor);
        this.isPrototype = true;
    }

    private ShufflingAccumulator(ShufflingAccumulator<K, V, P> image) {
        super(image);
        this.shuffler = image.shuffler;
        shuffler.registerPartitionerClone(this);
        this.isPrototype = false;
    }

    public void start() {
        shuffler.start();
    }

    @Override
    public void close() throws Exception {
        if (isPrototype) {
            shuffler.stop();
        }
    }

    /**
     * @return the inactivated cache
     */
    private Map<P, TreeMap<K, Collection<V>>> swapCaches() {
        LOGGER.debug("Waiting for cache lock (MaxRecCnt: {})", shuffler.maxRecordCount);
        cacheLock.writeLock().lock();
        LOGGER.debug("Cache lock acquired (MaxRecCnt: {})", shuffler.maxRecordCount);
        try {

            Map<P, TreeMap<K, Collection<V>>> oldCache = cache;
            cache = Maps.newHashMap();
            return oldCache;

        } finally {
            cacheLock.writeLock().unlock();
            LOGGER.debug("Cache lock released (MaxRecCnt: {})", shuffler.maxRecordCount);
        }
    }

    @Override
    protected void writeToPartition(P partition, K key, V value, Context context) throws Exception {
        LOGGER.debug("Data written to partition {}", partition);

        cacheLock.writeLock().lock();
        try {
            TreeMap<K, Collection<V>> partitionMap = this.cache.get(partition);
            if (partitionMap == null) {
                partitionMap = Maps.newTreeMap(shuffler.keyComparator);
                this.cache.put(partition, partitionMap);
            }

            Collection<V> sortedValues = partitionMap.get(key);
            if (sortedValues == null) {
                sortedValues = shuffler.valueCollectionFactory.createValueCollection();
                partitionMap.put(key, sortedValues);
            }

            sortedValues.add(value);

        } finally {
            cacheLock.writeLock().unlock();
        }

        shuffler.incrementVersion();
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.shuffler.debugEnabled = debugEnabled;
    }

    public Accumulator<K, V> getMetaAccumulator() {
        return metaAccumulator;
    }

//    public static void main(String[] args) throws InterruptedException {
//        ScheduledExecutorService shuffleExecutor = Executors.newScheduledThreadPool(2);
//        final CountDownLatch countDownLatch = new CountDownLatch(10);
//        for (int i = 0; i < 10; i++) {
//            final String msg = "Here I am " + i;
//            shuffleExecutor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(1000);
//                        System.out.println(msg);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        countDownLatch.countDown();
//                    }
//                }
//            });
//            System.out.println("Scheduled:" + msg);
//        }
//
//        countDownLatch.await();
//
//        shuffleExecutor.shutdown();
//    }

}
