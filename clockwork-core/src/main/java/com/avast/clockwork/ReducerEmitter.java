package com.avast.clockwork;

import de.matthiasmann.continuations.Coroutine;
import de.matthiasmann.continuations.CoroutineProto;
import de.matthiasmann.continuations.SuspendExecution;
import javolution.util.FastMap;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: slajchrt
 * Date: 1/12/12
 * Time: 8:31 PM
 */
public class ReducerEmitter {

    private final Reducer reducer;
    //private final Map<Object, Coroutine> coroutineMap = new HashMap<Object, Coroutine>();
    private final FastMap<Object, Coroutine> coroutineMap = new FastMap<Object, Coroutine>().shared();

    private static final Object STOP = new String("STOP");

    public ReducerEmitter(Reducer reducer) {
        this.reducer = reducer;
        if (reducer.getKeyComparator() != null) {
            coroutineMap.setKeyComparator(reducer.getKeyComparator());
        }
    }

    public void feed(Object key, Object value, Context context) {
        feed_(key, value, context);
    }

    public void feed(Object key, Iterator values, Context context) {
        feed_(key, new SuspendableIteratorAdapter(values), context);
    }

    public void feed_(Object key, Object value, Context context) {
        Coroutine coroutine = coroutineMap.get(key);
        if (coroutine == null || Coroutine.State.FINISHED.equals(coroutine.getState())) {
            CoroutineProto reducerCoroutineProto = new ReducerCoroutineProto(key, value, context);
            coroutine = new Coroutine(reducerCoroutineProto);
            coroutineMap.put(key, coroutine);
        } else {
            ((ReducerCoroutineProto) coroutine.getProto()).updateValue(value);
        }

        coroutine.run();

        switch (coroutine.getState()) {
            case FINISHED:
                // Remove the coroutine if and only if the key is not the STOP marker.
                // The stop method clears the map at once.
                //if (STOP != value) {
                //coroutineMap.remove(key);
                //}
                coroutineMap.remove(key);
                break;
            case SUSPENDED:
                break;
            case NEW:
                // shouldn't be the case
                break;
            case RUNNING:
                // shouldn't be the case
                break;
        }
    }

    public void stop(Object key) {
        feed(key, STOP, null);
        //coroutineMap.clear();
    }

    public void stopAll() {
        while (true) {
            Iterator<Object> keyIter = coroutineMap.keySet().iterator();
            if (!keyIter.hasNext()) {
                break;
            }
            Object key = keyIter.next();
            stop(key);
        }
    }

    class ReducerCoroutineProto implements CoroutineProto {

        private final Object key;
        private final Context context;
        private Object value;

        ReducerCoroutineProto(Object key, Object value, Context context) {
            this.key = key;
            this.value = value;
            this.context = context;
        }

        void updateValue(Object value) {
            this.value = value;
        }

        public void coExecute() throws SuspendExecution {
            try {
                if (value instanceof com.avast.clockwork.SuspendableIterator) {
                    reducer.reduce(key, (com.avast.clockwork.SuspendableIterator) value, context);
                } else {
                    reducer.reduce(key, new SuspendableIterator(), context);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        class SuspendableIterator implements com.avast.clockwork.SuspendableIterator<Object> {

            private boolean done;

            public boolean hasNext() throws SuspendExecution {
                if (done) {
                    done = false;
                    Coroutine.yield();

                    return value != STOP;
                } else {
                    return true;
                }
            }

            public Object next() throws SuspendExecution {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                done = true;
                return value;
            }

        }

    }

}