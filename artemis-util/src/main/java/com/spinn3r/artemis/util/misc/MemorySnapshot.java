package com.spinn3r.artemis.util.misc;

/**
 *
 */
public class MemorySnapshot {

    private long memoryUsed;

    public MemorySnapshot(long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public MemoryDiff diff() {
        return new MemoryDiff( MemorySnapshots.computeMemoryUsed() - memoryUsed );
    }

    @Override
    public String toString() {
        return "MemorySnapshot{" +
                 "memoryUsed=" + memoryUsed +
                 '}';
    }

}
