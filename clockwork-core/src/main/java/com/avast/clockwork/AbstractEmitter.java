package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:37 PM
 */
public abstract class AbstractEmitter<OK, OV> implements Emitter<OK, OV> {
    @Override
    public void emit(OK outputKey, OV outputValue) throws Exception {
        Execution.emit(this, outputKey, outputValue);
    }
}
