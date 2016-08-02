package com.spinn3r.artemis.metrics.tags;

import com.spinn3r.metrics.kairosdb.Tag;

import java.util.List;

/**
 * Various metric and tag related functions.
 */
public class Tags {

    /**
     *
     * Tokenize status codes, content etc by rounding it and then adding a suffix.
     * for example.. HTTP status code 200 becomes 2xx.  11432 bytes becomes 11k.
     *
     */
    public static String tokenized(long value, int divisor, String suffix) {
        return Long.toString(value / divisor) + suffix;
    }

    public static Tag[] toArray( List<Tag> list ) {
        return list.toArray( new Tag[list.size()] );
    }

    /**
     * Round the given value to the nearest interval.
     *
     * @param value
     * @param interval
     * @return
     */
    public static long rounded(long value, long interval ) {

        if ( interval == 0 )
            return 0;

        double range = value / (double)interval;

        double rounded = Math.round( range );

        return (long)(rounded * interval);

    }

}
