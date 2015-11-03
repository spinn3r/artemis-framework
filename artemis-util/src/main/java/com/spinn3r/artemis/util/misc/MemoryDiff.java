package com.spinn3r.artemis.util.misc;

/**
 *
 */
public class MemoryDiff {

    private long diff;

    public MemoryDiff(long diff) {
        this.diff = diff;
    }

    public long getDiff() {
        return diff;
    }

    @Override
    public String toString() {
        return String.format( "%,d bytes\n", diff );
    }

}


