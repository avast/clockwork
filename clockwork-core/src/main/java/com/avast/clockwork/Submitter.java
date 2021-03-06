package com.avast.clockwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class makes invoking executions easier in a multi-threaded environment. Passing a key-value pair to an execution
 * is done in a task executed by the encapsulated executor. Each submitting task borrows an execution from the execution
 * pool and returns it after the key-value pair is sent to the execution.
 * <p/>
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 4:51 PM
 * @see ExecutionPool
 */
public class Submitter<IK, IV, OK, OV> {

    private static Logger LOGGER = LoggerFactory.getLogger(Submitter.class);

    private final ExecutorService executor;
    private final ExecutionPool<IK,IV,OK,OV> executionPool;
    public Submitter(Execution.Builder<IK, IV, OK, OV> execBuilder, int maxExecConcurrency) {
        executor = Executors.newFixedThreadPool(maxExecConcurrency);
        executionPool = ExecutionPool.newPool(execBuilder, maxExecConcurrency);
    }

    public void submit(final IK key, final IV value, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    emit(context);
                } catch (Exception e) {
                    LOGGER.error("Error in clockwork execution", e);
                }
            }

            private void emit(Context context) throws Exception {
                Execution<IK, IV, OK, OV> execution = executionPool.borrowObject();
                try {
                    execution.emit(key, value, context);
                } finally {
                    executionPool.returnObject(execution);
                }
            }
        });
    }

    public void flush() {
        executionPool.clear();
    }
}
