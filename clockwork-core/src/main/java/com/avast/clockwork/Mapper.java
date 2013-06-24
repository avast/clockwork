package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 2:21 PM
 */
public abstract class Mapper<IK, IV, OK, OV> extends AbstractTransformer<IK, IV, OK, OV> {

    protected abstract void map(IK inputKey, IV inputValue, Context context) throws Exception;

}
