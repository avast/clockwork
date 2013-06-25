package com.avast.clockwork;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * A pool for execution instances. It is suitable in multi-threaded environment in which we want to invoke one execution
 * per thread since the execution is not thread safe. The new execution instances are created by means of the execution
 * builder passed through the constructor or the factory method.
 * <p/>
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 4:33 PM
 * @see Execution
 * @see ExecutionConfigBuilder
 */
public class ExecutionPool<IK, IV, OK, OV> extends GenericObjectPool<Execution<IK, IV, OK, OV>> {

    private ExecutionPool(PoolableObjectFactory<Execution<IK, IV, OK, OV>> factory, int maxActive) {
        super(factory, maxActive);
    }

    /**
     * Creates an instance of this execution pool.
     * @param execBuilder the execution builder used for creating execution instances
     * @param maxActive the number of max active executions in the pool
     * @param <IK> the input key type
     * @param <IV> the input value type
     * @param <OK> the output key type
     * @param <OV> the output value type
     * @return the pool
     */
    public static <IK, IV, OK, OV>  ExecutionPool<IK, IV, OK, OV> newPool(Execution.Builder<IK, IV, OK, OV> execBuilder, int maxActive) {
        return new ExecutionPool<IK, IV, OK, OV>(new ExecutionPoolFactory<IK, IV, OK, OV>(execBuilder), maxActive);
    }

    private static class ExecutionPoolFactory<IK, IV, OK, OV>  implements PoolableObjectFactory<Execution<IK, IV, OK, OV>> {

        private final Execution.Builder<IK, IV, OK, OV> execBuilder;

        private ExecutionPoolFactory(Execution.Builder<IK, IV, OK, OV> execBuilder) {
            this.execBuilder = execBuilder;
        }

        @Override
        public Execution<IK, IV, OK, OV> makeObject() throws Exception {
            return execBuilder.build();
        }

        @Override
        public void destroyObject(Execution<IK, IV, OK, OV> execution) throws Exception {
            execution.close();
        }

        @Override
        public boolean validateObject(Execution<IK, IV, OK, OV> obj) {
            return true;
        }

        @Override
        public void activateObject(Execution<IK, IV, OK, OV> obj) throws Exception {
        }

        @Override
        public void passivateObject(Execution<IK, IV, OK, OV> obj) throws Exception {
        }
    }

}
