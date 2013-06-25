package com.avast.clockwork;

/**
 * The common abstract ancestor for both mappers and reducers.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 8:44 PM
 */
public abstract class AbstractTransformer<IK, IV, OK, OV> extends AbstractEmitter<OK, OV>
        implements Transformer<IK, IV, OK, OV> {

    @Override
    public void onNewExecution(Execution execution) {
    }

    @Override
    public void onExecutionClosed(Execution execution) {
    }

    @Override
    public void flush() throws Exception {
    }

}
