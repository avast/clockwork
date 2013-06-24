package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 8:44 PM
 */
public abstract class AbstractTransformer<IK, IV, OK, OV> extends AbstractEmitter<OK, OV>
        implements Transformer<IK, IV, OK, OV> {

    @Override
    public AbstractTransformer<IK, IV, OK, OV> clone() throws CloneNotSupportedException {
        return (AbstractTransformer) super.clone();
    }

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
