package com.avast.clockwork;

/**
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

    public static ExecutionConfigBuilder newBuilder() {
        return new ExecutionConfigBuilder(new Execution.ExecutionConfig());
    }

    public ExecutionConfigBuilder autoFlush(long flushModulo) {
        this.execConfig.flushModulo = flushModulo;
        return this;
    }
}
