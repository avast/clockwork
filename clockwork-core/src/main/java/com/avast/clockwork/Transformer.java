package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:35 PM
 */
public interface Transformer<IK, IV, OK, OV> extends Emitter<OK, OV>, Cloneable {

    Transformer<IK, IV, OK, OV> clone() throws CloneNotSupportedException;

    void onNewExecution(Execution execution);

    void onExecutionClosed(Execution execution);

    void flush() throws Exception;
}
