package com.avast.clockwork;

/**
 * The builder for configuring the execution parameters.
 * <p/>
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 5:58 PM
 */
public class ExecutionConfigBuilder {
    private final Execution.ExecutionConfig execConfig;

    ExecutionConfigBuilder(Execution.ExecutionConfig execConfig) {
        this.execConfig = execConfig;
    }

    Execution.ExecutionConfig getExecConfig() {
        return execConfig;
    }

    /**
     * The factory method for this builder.
     * @return the builder
     */
    public static ExecutionConfigBuilder newBuilder() {
        return new ExecutionConfigBuilder(new Execution.ExecutionConfig());
    }

    /**
     * Activates the auto-flush mode in which the execution will be flushing after each <code>flushModulo</code> key-value
     * pairs.
     * @param flushModulo the flush modulo
     * @return this builder
     */
    public ExecutionConfigBuilder autoFlush(long flushModulo) {
        this.execConfig.flushModulo = flushModulo;
        return this;
    }
}
