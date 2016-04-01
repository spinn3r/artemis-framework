package com.spinn3r.artemis.network.init;

/**
 *
 */
public class ExecutorConfig {

    private int maxRetries = 5;

    private int sleepIntervalMillis = 30_000;

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getSleepIntervalMillis() {
        return sleepIntervalMillis;
    }

    @Override
    public String toString() {
        return "ExecutorConfig{" +
                 "maxRetries=" + maxRetries +
                 ", sleepIntervalMillis=" + sleepIntervalMillis +
                 '}';
    }

}
