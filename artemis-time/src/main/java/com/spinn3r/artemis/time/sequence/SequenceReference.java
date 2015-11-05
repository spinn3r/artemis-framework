package com.spinn3r.artemis.time.sequence;

import com.spinn3r.artemis.time.Time;

import java.util.Date;

/**
 * A reference to a sequence.
 */
public class SequenceReference implements Comparable<SequenceReference> {

    /**
     * Enough padding to allow us to add 2^28 (268435456) without shifting
     * anything.
     */
    public static final long PADDING = 1000000000;

    /**
     * Resolution of time component.
     */
    public static final long RESOLUTION = 1000;

    protected final long value;

    public SequenceReference(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public int compareTo(SequenceReference o) {
        return Long.compare( value, o.value );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof SequenceReference )) return false;

        SequenceReference that = (SequenceReference) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) ( value ^ ( value >>> 32 ) );
    }

    @Override
    public String toString() {
        return "SequenceReference{" +
                 "value=" + value +
                 '}';
    }

    /**
     * Convert the current sequence reference to a date.
     *
     * @return
     */
    public Date toDate() {
        return SequenceReferences.toDate( value );
    }

    public Time toTime() {
        return new Time( toDate() );
    }

}
