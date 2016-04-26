package com.spinn3r.artemis.time.sequence;

import java.util.Date;

/**
 *
 */
public class SequenceReferences {
    /**
     * Compute a sequence value from a Date object.  DO NOT use this to write
     * values, only to reference them.  For example, to read values written
     * after a specific timestamp.
     *
     * @param date
     * @return
     */
    public static SequenceReference fromDate(Date date) {
        long result = (date.getTime() / 1000L) * SequenceReference.GLOBAL_TIME_PADDING;
        return new SequenceReference( result );
    }

    public static Date toDate(long value) {

        long v = value / SequenceReference.GLOBAL_TIME_PADDING;

        // sequence timestamps don't include milliseconds but Java time does.
        v = v * SequenceReference.TIME_RESOLUTION;

        return new Date( v );

    }

}
