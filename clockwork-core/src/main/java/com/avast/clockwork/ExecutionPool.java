package com.avast.clockwork;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 4:33 PM
 */
public class ExecutionPool<IK, IV, OK, OV> extends GenericObjectPool<Execution<IK, IV, OK, OV>> {

    private ExecutionPool(PoolableObjectFactory<Execution<IK, IV, OK, OV>> factory, int maxActive) {
        super(factory, maxActive);
    }

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
