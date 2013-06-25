package com.avast.clockwork;

import de.matthiasmann.continuations.SuspendExecution;
import javolution.util.FastComparator;

/**
 * The reducer component. It typically aggregates the values associated with a key.
 * <p/>
 * User: slajchrt
 * Date: 1/12/12
 * Time: 2:21 PM
 */
public abstract class Reducer<IK, IV, OK, OV> extends AbstractTransformer<IK, IV, OK, OV> {

    private volatile FastComparator<IK> keyComparator;

    /**
     * This method is here for the sake of {@link ReducerEmitter}.
     * @return the key comparator for the {@link javolution.util.FastMap}
     * @see javolution.util.FastMap
     * @see javolution.util.FastComparator
     */
    protected FastComparator<IK> getKeyComparator() {
        return keyComparator;
    }

    protected void setKeyComparator(FastComparator<IK> keyComparator) {
        this.keyComparator = keyComparator;
    }

    /**
     * Aggregates the input values associated with the input key.
     * @param inputKey the input key
     * @param inputValues the input value
     * @param context the context
     * @throws SuspendExecution
     * @throws Exception
     */
    protected abstract void reduce(IK inputKey, SuspendableIterator<IV> inputValues, Context context)
            throws SuspendExecution, Exception;

}
