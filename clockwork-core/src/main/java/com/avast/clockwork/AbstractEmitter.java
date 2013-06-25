package com.avast.clockwork;

/**
 * A utility implementation for components within an {@link Execution execution}.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:37 PM
 */
public abstract class AbstractEmitter<OK, OV> implements Emitter<OK, OV> {
    /**
     * Delegates the call to the execution within which it is running.
     * @param outputKey the emitted key
     * @param outputValue the emitted value
     * @throws Exception
     */
    @Override
    public void emit(OK outputKey, OV outputValue) throws Exception {
        Execution.emit(this, outputKey, outputValue);
    }
}
