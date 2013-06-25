package com.avast.clockwork;

/**
 * The mapper component. It typically transforms, filters or enhances the input key-value pairs.
 * <p/>
 * User: slajchrt
 * Date: 1/12/12
 * Time: 2:21 PM
 */
public abstract class Mapper<IK, IV, OK, OV> extends AbstractTransformer<IK, IV, OK, OV> {

    /**
     * Maps the input key-value pair. The implementation should use the <code>emit</code> method to pass the output
     * key-value pair to the next component.
     * @param inputKey the input key
     * @param inputValue the input value
     * @param context the context
     * @throws Exception
     */
    protected abstract void map(IK inputKey, IV inputValue, Context context) throws Exception;

}
