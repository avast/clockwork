package com.avast.clockwork;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.util.*;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:28 PM
 */
public class Execution<IK, IV, OK, OV> implements Emitter<IK, IV> {

    private final TransformerInstance firstInstance;
    private final Map<Transformer, TransformerInstance> transformerChainStepMap =
            new HashMap<Transformer, TransformerInstance>();
    private final Accumulator<OK, OV> accumulator;
    private long emitCounter = 0;

    private static final ThreadLocal<Stack<Execution>> executionStackTL = new ThreadLocal<Stack<Execution>>();
    private final ExecutionConfig execConfig;

    private abstract static class TransformerInstance {
        final Transformer transformer;
        final TransformerInstance next;
        TransformerInstance prev;

        private TransformerInstance(Transformer transformer, TransformerInstance next) {
            this.transformer = transformer;
            if (next != null) {
                next.prev = this;
            }
            this.next = next;
        }

        public Transformer getTransformer() {
            return transformer;
        }

        void flush() throws Exception {
            transformer.flush();
        }
    }

    private static class MapperInstance extends TransformerInstance {
        private MapperInstance(Mapper mapper, TransformerInstance next) {
            super(mapper, next);
        }

        @Override
        public Mapper getTransformer() {
            return (Mapper) super.getTransformer();
        }

    }

    public Accumulator<OK, OV> getAccumulator() {
        return accumulator;
    }

    private static class ReducerInstance extends TransformerInstance {
        final ReducerEmitter reducerEmitter;

        private ReducerInstance(Reducer reducer, TransformerInstance next) {
            super(reducer, next);
            this.reducerEmitter = new ReducerEmitter(reducer);
        }

        @Override
        public Reducer getTransformer() {
            return (Reducer) super.getTransformer();
        }

        @Override
        void flush() throws Exception {
            reducerEmitter.stopAll();
            super.flush();
        }
    }

    private Execution(TransformerStep<IK, IV, OK, OV> lastStep, Accumulator<OK, OV> accumulator)
            throws Exception {

        execConfig = lastStep.execConfig;

        this.accumulator = accumulator;

        if (lastStep != null) {
            TransformerStep transformerStep = lastStep;
            TransformerInstance lastInst = null;
            do {
                Transformer transformer = transformerStep.transformerPrototype.clone();
                TransformerInstance transformerInstance = transformer instanceof Mapper ?
                        new MapperInstance((Mapper) transformer, lastInst) :
                        new ReducerInstance((Reducer) transformer, lastInst);

                transformerChainStepMap.put(transformer, transformerInstance);
                lastInst = transformerInstance;

                transformer.onNewExecution(this);

                if (transformerStep.prev == null) {
                    break;
                }

                transformerStep = transformerStep.prev;

            } while (true);

            firstInstance = lastInst;
        } else {
            firstInstance = null;
        }

    }

    private Context context = null;

    private Context getContext() {
        // todo
        return context;
    }

    public void emit(IK key, IV value, Context context) throws Exception {
        this.context = context;
        try {
            emit(key, value);
        } finally {
            this.context = null;
        }
    }

    public void emit(IK key, Iterator<IV> values, Context context) throws Exception {
        this.context = context;
        try {
            emit(key, values);
        } finally {
            this.context = null;
        }
    }

    private void enter(Execution execution) {
        Stack<Execution> execStack = executionStackTL.get();
        if (execStack == null) {
            execStack = new Stack<Execution>();
            executionStackTL.set(execStack);
        }

        execStack.push(execution);
    }

    private void leave() {
        Stack<Execution> execStack = executionStackTL.get();
        execStack.pop();
        if (execStack.isEmpty()) {
            executionStackTL.remove();
        }
    }

    private void incEmitCounter() throws Exception {
        autoFlush();
        emitCounter++;
    }

    private void autoFlush() throws Exception {
        if (execConfig.flushModulo > 0) {
            if ((emitCounter + 1) % execConfig.flushModulo == 0) {
                flush();
            }
        }
    }

    @Override
    public void emit(IK key, IV value) throws Exception {
        enter(this);
        try {
            emitForStep(firstInstance, key, value);
            incEmitCounter();
        } finally {
            leave();
        }
    }

    public void emit(IK key, Iterator<IV> values) throws Exception {
        enter(this);
        try {
            emitForStep(firstInstance, key, values);
            incEmitCounter();
        } finally {
            leave();
        }
    }

    private void emitForStep(TransformerInstance tinst, IK key, IV value) throws Exception {
        if (tinst == null) {
            accumulator.send((OK) key, (OV) value, getContext());
        } else {
            Transformer transformer = tinst.transformer;
            if (transformer instanceof Mapper) {
                ((Mapper) transformer).map(key, value, getContext());
            } else {
                ReducerEmitter reducerEmitter = ((ReducerInstance) tinst).reducerEmitter;
                reducerEmitter.feed(key, value, getContext());
            }
        }
    }

    private void emitForStep(TransformerInstance tinst, IK key, Iterator<IV> values) throws Exception {
        if (tinst == null) {
            while (values.hasNext()) {
                IV nextValue = values.next();
                accumulator.send((OK) key, (OV) nextValue, getContext());
            }
        } else {
            Transformer transformer = tinst.transformer;
            if (transformer instanceof Mapper) {
                while (values.hasNext()) {
                    IV nextValue = values.next();
                    ((Mapper) transformer).map(key, nextValue, getContext());
                }
            } else {
                ReducerEmitter reducerEmitter = ((ReducerInstance) tinst).reducerEmitter;
                reducerEmitter.feed(key, values, getContext());
            }
        }
    }

    static void emit(Emitter emitter, Object key, Object value) throws Exception {
        Execution execution = getExecution();
        Preconditions.checkNotNull(execution, "No MR execution in the current thread");

        TransformerInstance current = execution.getChainStep(emitter);
        Preconditions.checkNotNull(current);

        TransformerInstance next = current.next;
        execution.emitForStep(next, key, value);
    }

    public static Execution getExecution() {
        return executionStackTL.get().peek();
    }

    public static <IK, IV, OK, OV> void execute(IK key, Iterator<IV> values, Context context,
                                                Function<Map.Entry<OK, OV>, Void> output,
                                                AbstractTransformer<?, ?, ?, ?>... transformers) throws Exception {
        NotifyingAccumulator<OK, OV> notifier = new NotifyingAccumulator<OK, OV>(output);
        Execution<IK, IV, OK, OV> execution = (Execution<IK, IV, OK, OV>) Execution.newBuilder(Arrays.asList(transformers), notifier)
                .build();
        execution.emit(key, values, context);
        execution.close();
    }

    private TransformerInstance getChainStep(Emitter emitter) {
        return transformerChainStepMap.get(emitter);
    }

    public void flush() throws Exception {
        enter(this);
        try {
            TransformerInstance tinst = firstInstance;
            while (tinst != null) {
                tinst.flush();
                tinst = tinst.next;
            }

        } finally {
            leave();
            accumulator.flush();
        }
    }

    public void close() throws Exception {
        try {
            flush();
        } finally {
            try {
                accumulator.close();
            } finally {
                notifyClose();
            }
        }
    }

    /**
     * Notify the transformers
     */
    private void notifyClose() {
        TransformerInstance transformerInstance = firstInstance;
        while (transformerInstance != null) {
            Transformer transformer = transformerInstance.getTransformer();
            transformer.onExecutionClosed(this);
            transformerInstance = transformerInstance.next;
        }
    }

    public static FirstStep newBuilder() {
        return new FirstStep(new ExecutionConfig());
    }

    public static FirstStep newBuilder(ExecutionConfigBuilder executionConfigBuilder) {
        return new FirstStep(executionConfigBuilder.getExecConfig());
    }

    public static <OK, OV> Builder<?, ?, OK, OV> newBuilder(Collection<AbstractTransformer<?, ?, ?, ?>> transformers,
                                                            Accumulator<OK, OV> accumulator) {
        BuilderStep step = Execution.newBuilder();
        for (AbstractTransformer<?, ?, ?, ?> transformer : transformers) {
            if (transformer instanceof Mapper) {
                step = step.mapper_((Mapper) transformer);
            } else if (transformer instanceof Reducer) {
                step = step.reducer_((Reducer) transformer);
            } else {
                throw new IllegalArgumentException("Unknown transformer type: " + transformer);
            }
        }

        return step.accumulator_(accumulator);
    }


    static class ExecutionConfig {
        long flushModulo;
    }

    abstract static class BuilderStep {
        protected final ExecutionConfig execConfig;

        protected BuilderStep(ExecutionConfig execConfig) {
            this.execConfig = execConfig;
        }

        abstract MapperTransformerStep mapper_(Mapper mapperPrototype);

        abstract ReducerTransformerStep reducer_(Reducer reducerPrototype);

        abstract Builder accumulator_(Accumulator accumulator);

    }

    public static class FirstStep extends BuilderStep {

        public FirstStep(ExecutionConfig execConfig) {
            super(execConfig);
        }

        public <IK, IV, OK, OV> MapperTransformerStep<IK, IV, OK, OV> mapper(Mapper<IK, IV, OK, OV> mapperPrototype) {
            return new MapperTransformerStep<IK, IV, OK, OV>(mapperPrototype, execConfig);
        }

        public <IK, IV, OK, OV> ReducerTransformerStep<IK, IV, OK, OV> reducer(Reducer<IK, IV, OK, OV> reducerPrototype) {
            return new ReducerTransformerStep<IK, IV, OK, OV>(reducerPrototype, execConfig);
        }

        public <IK, IV, OK, OV> Builder<IK, IV, OK, OV> accumulator(Accumulator<OK, OV> accumulator) {
            return new Builder<IK, IV, OK, OV>(null, accumulator);
        }

        @Override
        MapperTransformerStep mapper_(Mapper mapperPrototype) {
            return mapper(mapperPrototype);
        }

        @Override
        ReducerTransformerStep reducer_(Reducer reducerPrototype) {
            return reducer(reducerPrototype);
        }

        @Override
        Builder accumulator_(Accumulator accumulator) {
            return accumulator(accumulator);
        }
    }

    public abstract static class TransformerStep<IK, IV, OK, OV> extends BuilderStep {

        private final TransformerStep<IK, IV, ?, ?> prev;
        private TransformerStep<IK, IV, ?, ?> next;
        private final Transformer<?, ?, OK, OV> transformerPrototype;

        protected TransformerStep(Transformer<IK, IV, OK, OV> transProto, ExecutionConfig execConfig) {
            this(null, transProto, execConfig);
        }

        private TransformerStep(TransformerStep<IK, IV, ?, ?> prev, Transformer<?, ?, OK, OV> transProto, ExecutionConfig execConfig) {
            super(execConfig);

            if (prev != null)
                prev.next = this;
            this.prev = prev;
            this.transformerPrototype = transProto;
        }

        public Transformer<?, ?, OK, OV> getTransformerPrototype() {
            return transformerPrototype;
        }

        public Builder<IK, IV, OK, OV> accumulator(Accumulator<OK, OV> accumulator) {
            return new Builder<IK, IV, OK, OV>(this, accumulator);
        }

        @Override
        Builder accumulator_(Accumulator accumulator) {
            return accumulator(accumulator);
        }

    }

    public static class MapperTransformerStep<IK, IV, OK, OV> extends TransformerStep<IK, IV, OK, OV> {
        MapperTransformerStep(Mapper<IK, IV, OK, OV> mapperPrototype, ExecutionConfig execConfig) {
            super(mapperPrototype, execConfig);
        }

        MapperTransformerStep(TransformerStep<IK, IV, ?, ?> prev, Mapper<?, ?, OK, OV> mapperPrototype, ExecutionConfig execConfig) {
            super(prev, mapperPrototype, execConfig);
        }

        public <TK, TV> ReducerTransformerStep<IK, IV, TK, TV> reducer(Reducer<OK, OV, TK, TV> reducerPrototype) {
            return new ReducerTransformerStep<IK, IV, TK, TV>(this, reducerPrototype, execConfig);
        }

        public <TK, TV> MapperTransformerStep<IK, IV, TK, TV> mapper(Mapper<OK, OV, TK, TV> mapperPrototype) {
            return new MapperTransformerStep<IK, IV, TK, TV>(this, mapperPrototype, execConfig);
        }

        @Override
        MapperTransformerStep mapper_(Mapper mapperPrototype) {
            return mapper(mapperPrototype);
        }

        @Override
        ReducerTransformerStep reducer_(Reducer reducerPrototype) {
            return reducer(reducerPrototype);
        }
    }

    public static class ReducerTransformerStep<IK, IV, OK, OV> extends TransformerStep<IK, IV, OK, OV> {

        public ReducerTransformerStep(Reducer<IK, IV, OK, OV> reducerPrototype, ExecutionConfig execConfig) {
            super(reducerPrototype, execConfig);
        }

        ReducerTransformerStep(TransformerStep<IK, IV, ?, ?> prev, Reducer<?, ?, OK, OV> reducerPrototype, ExecutionConfig execConfig) {
            super(prev, reducerPrototype, execConfig);
        }

        public <TK, TV> MapperTransformerStep<IK, IV, TK, TV> mapper(Mapper<OK, OV, TK, TV> mapperPrototype) {
            return new MapperTransformerStep<IK, IV, TK, TV>(this, mapperPrototype, execConfig);
        }

        public <TK, TV> ReducerTransformerStep<IK, IV, TK, TV> reducer(Reducer<OK, OV, TK, TV> reducerPrototype) {
            return new ReducerTransformerStep<IK, IV, TK, TV>(this, reducerPrototype, execConfig);
        }

        @Override
        MapperTransformerStep mapper_(Mapper mapperPrototype) {
            return mapper(mapperPrototype);
        }

        @Override
        ReducerTransformerStep reducer_(Reducer reducerPrototype) {
            return reducer(reducerPrototype);
        }

    }

    public static class Builder<IK, IV, OK, OV> {
        private final TransformerStep<IK, IV, OK, OV> lastTransformerStep;
        private final Accumulator<OK, OV> accumulator;

        private Builder(TransformerStep<IK, IV, OK, OV> lastTransformerStep, Accumulator<OK, OV> accumulator) {
            this.lastTransformerStep = lastTransformerStep;
            this.accumulator = accumulator;
        }

        public Execution<IK, IV, OK, OV> build() throws Exception {
            return new Execution<IK, IV, OK, OV>(lastTransformerStep, accumulator);
        }

    }

    private static class NotifyingAccumulator<OK, OV> implements Accumulator<OK, OV> {

        private final Function<Map.Entry<OK, OV>, Void> callback;

        public NotifyingAccumulator(Function<Map.Entry<OK, OV>, Void> callback) {
            this.callback = callback;
        }

        @Override
        public void send(OK key, OV value, Context context) throws Exception {
            Map.Entry<OK, OV> pair = Collections.singletonMap(key, value).entrySet().iterator().next();
            callback.apply(pair);
        }

        @Override
        public void close() throws Exception {
        }

        @Override
        public void flush(OK key) throws Exception {
        }

        @Override
        public void flush() throws Exception {
        }

    }
}
