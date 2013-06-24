package com.avast.clockwork;

import java.util.Iterator;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:31 PM
 */
public interface Emitter<OK, OV> {

    void emit(OK outputKey, OV outputValue) throws Exception;

}
