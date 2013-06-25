package com.avast.clockwork;

/**
 * An abstraction of components capable of emitting key-value pairs to another component.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:31 PM
 */
public interface Emitter<OK, OV> {

    /**
     * Emits the key-value pair to another component.
     * @param outputKey the emitted key
     * @param outputValue the emitted value
     * @throws Exception
     */
    void emit(OK outputKey, OV outputValue) throws Exception;

}
