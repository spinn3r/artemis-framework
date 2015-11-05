package com.spinn3r.artemis.time;

/**
 * functions for working with milliseconds
 */
public class Milliseconds {

    public static long toMinutes( long value ) {
        return value / (60L * 1000L);
    }

    public static long toHours( long value ) {
        return value / (60L * 60L * 1000L);
    }

}
