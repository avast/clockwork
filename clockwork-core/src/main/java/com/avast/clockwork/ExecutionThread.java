//package com.avast.clockwork;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.CountDownLatch;
//
///**
// * User: slajchrt
// * Date: 1/14/12
// * Time: 10:45 PM
// */
//public class ExecutionThread<IK, IV, OK, OV> implements Runnable {
//
//    private static Logger LOGGER = LoggerFactory.getLogger(ExecutionThread.class);
//
//    private final Execution<IK, IV, OK, OV> execution;
//    private final BlockingQueue<KeyValue<IK, IV>> queue;
//    private final CountDownLatch countDownLatch;
//    private final String name;
//    private final long pollTimeout;
//
//    public ExecutionThread(Execution<IK, IV, OK, OV> execution, BlockingQueue<KeyValue<IK, IV>> queue,
//                           long pollTimeout, String name) {
//        this(execution, queue, null, pollTimeout, name);
//    }
//
//    public ExecutionThread(Execution<IK, IV, OK, OV> execution, BlockingQueue<KeyValue<IK, IV>> queue,
//                           CountDownLatch countDownLatch, long pollTimeout, String name) {
//        this.execution = execution;
//        this.queue = queue;
//        this.countDownLatch = countDownLatch;
//        this.pollTimeout = pollTimeout;
//        this.name = name;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                KeyValue<IK, IV> keyValue = queue.take();
//                //KeyValue<IK, IV> keyValue = queue.poll(pollTimeout, TimeUnit.MILLISECONDS);
//                if (keyValue == null) {
//                    execution.flush();
//                    continue;
//                } else if (keyValue.getKey() == null && keyValue.getValue() == null) {
//                    break;
//                }
//
//                execution.emit(keyValue.getKey(), keyValue.getValue());
//
//            }
//
//        } catch (Exception e) {
//            LOGGER.error("MR execution error:", e);
//        } finally {
//            try {
//                execution.close();
//            } catch (Exception e) {
//                LOGGER.error("MR execution error:", e);
//            } finally {
//                if (countDownLatch != null) {
//                    countDownLatch.countDown();
//                }
//            }
//
//        }
//    }
//}
