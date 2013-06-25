package com.avast.clockwork;

/**
 * This interface represents a transformer in {@link Execution an execution}.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:35 PM
 */
public interface Transformer<IK, IV, OK, OV> extends Emitter<OK, OV> {

    /**
     * Called before the execution starts
     * @param execution the execution
     */
    void onNewExecution(Execution execution);

    /**
     * Called when the execution was closed
     * @param execution the execution
     */
    void onExecutionClosed(Execution execution);

    /**
     * Called when the execution is being flushed.
     * @throws Exception
     */
    void flush() throws Exception;
}
