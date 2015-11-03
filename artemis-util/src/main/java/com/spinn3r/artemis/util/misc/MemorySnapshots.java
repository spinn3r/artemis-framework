package com.spinn3r.artemis.util.misc;

/**
 *
 */
public class MemorySnapshots {

    public static MemorySnapshot capture() {
        return new MemorySnapshot( computeMemoryUsed() );
    }

    public static long computeMemoryUsed() {

        Runtime runtime = Runtime.getRuntime();

        runtime.gc();

        return runtime.totalMemory() - runtime.freeMemory();

    }

}
