package com.spinn3r.artemis.datetime;

import java.time.ZonedDateTime;

/**
 *
 */
public class ZonedDateTimeRange {

    private final ZonedDateTime after;

    private final ZonedDateTime before;

    public ZonedDateTimeRange(ZonedDateTime after, ZonedDateTime before) {
        this.after = after;
        this.before = before;
    }

    public ZonedDateTime getAfter() {
        return after;
    }

    public ZonedDateTime getBefore() {
        return before;
    }

    @Override
    public String toString() {
        return "ZonedDateTimeRange{" +
                 "after=" + after +
                 ", before=" + before +
                 '}';
    }

}
