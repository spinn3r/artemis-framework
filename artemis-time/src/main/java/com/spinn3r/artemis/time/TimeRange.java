package com.spinn3r.artemis.time;

import java.util.concurrent.TimeUnit;

/**
 * Represents a time range.  If this range is represented from epoch it can be
 * a timestamp.
 */
public class TimeRange implements Comparable<TimeRange> {

    /**
     * The max time range we can represent without overflow when converting to
     * other units. 
     */
    public static TimeRange MAX_RANGE = new TimeRange(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    protected long duration;

    protected TimeUnit timeUnit;

    public TimeRange(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Convert this to a new TimeRange using the given unit.
     */
    public TimeRange convert(TimeUnit timeUnit) {
        return new TimeRange(timeUnit.convert(duration, timeUnit), timeUnit);
    }

    /**
     * Get the time range in milliseconds.
     * @return
     */
    public long getTimeMillis() {
        return timeUnit.toMillis( duration );
    }

    public long getTimeSeconds() {
        return timeUnit.toSeconds( duration );
    }

    public long get(TimeUnit targetTimeUnit) {
        return targetTimeUnit.convert(duration, timeUnit);
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

        if (duration != timeRange.duration) return false;
        if (timeUnit != timeRange.timeUnit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) ( duration ^ ( duration >>> 32 ) );
        result = 31 * result + timeUnit.hashCode();
        return result;
    }

    public String format() {

        if( this == MAX_RANGE)
            return "max range";

        switch (timeUnit) {

            case NANOSECONDS:
                return String.format("%,dns", duration);

            case MICROSECONDS:
                return String.format("%,dµs", duration);

            case MILLISECONDS:
                return String.format("%,dms", duration);

            case SECONDS:
                return String.format("%,ds", duration);

            case MINUTES:
                return String.format("%,dm", duration);

            case HOURS:
                return String.format("%,dh", duration);

            case DAYS:
                return String.format("%,dd", duration);

            default:
                throw new RuntimeException("Unknown time unit: " + timeUnit);

        }

    }

    @Override
    public String toString() {
        return "TimeRange{" +
                 "duration=" + duration +
                 ", timeUnit=" + timeUnit +
                 '}';
    }

    public static TimeRange ofMillis(long millis) {
        return new TimeRange(millis, TimeUnit.MILLISECONDS);
    }

    public static TimeRange ofSeconds(long seconds) {
        return new TimeRange(seconds, TimeUnit.MILLISECONDS);
    }

    public static TimeRange ofMinutes(long minutes) {
        return new TimeRange(minutes, TimeUnit.MINUTES);
    }

    public static TimeRange ofHours(long hours) {
        return new TimeRange(hours, TimeUnit.HOURS);
    }

    public static TimeRange ofDays(long days) {
        return new TimeRange(days, TimeUnit.DAYS);
    }

    public static TimeRange ofWeeks(long weeks) {
        return new TimeRange(weeks * 7, TimeUnit.DAYS);
    }

}


