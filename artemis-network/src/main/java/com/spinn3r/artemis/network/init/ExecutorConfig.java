package com.spinn3r.artemis.network.init;

/**
 *
 */
public class ExecutorConfig {

    private int maxRetries = 0;

    private int sleepIntervalMillis = 30_000;

    /**
     * Specify the number of retries we should use when executing requests.
     * The default is zero or to never execute retries.
     */
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
