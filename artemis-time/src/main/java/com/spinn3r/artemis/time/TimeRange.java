package com.spinn3r.artemis.time;

import java.util.concurrent.TimeUnit;

/**
 * Represents a time range.  If this range is represented from epoch it can be
 * a timestamp.
 */
public class TimeRange implements Comparable<TimeRange> {

    protected long time;

    protected TimeUnit unit;

    public TimeRange(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    public long getTime() {
        return time;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    /**
     * Get the time range in milliseconds.
     * @return
     */
    public long getTimeMillis() {
        return unit.toMillis( time );
    }

    public long getTimeSeconds() {
        return unit.toSeconds( time );
    }

    @Override
    public int compareTo(TimeRange o) {

        long diff = getTimeMillis() - o.getTimeMillis();

        if ( diff < 0 )
            return -1;

        if ( diff > 0 )
            return 1;

        return 0;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof TimeRange )) return false;

        TimeRange timeRange = (TimeRange) o;

        if (time != timeRange.time) return false;
        if (unit != timeRange.unit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) ( time ^ ( time >>> 32 ) );
        result = 31 * result + unit.hashCode();
        return result;
    }

    public String format() {
        return TimeRangeFormatter.format( this );
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                 "time=" + time +
                 ", unit=" + unit +
                 '}';
    }

}


