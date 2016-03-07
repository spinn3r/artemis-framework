package com.spinn3r.artemis.datetime.partial.decorators;

import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.time.ISO8601;

import java.util.Date;

/**
 *
 */
public class DatePartialDateTimeDecorator implements PartialDateTime {

    private final Date date;

    private final boolean partial;

    public DatePartialDateTimeDecorator(Date date, boolean partial) {
        this.date = date;
        this.partial = partial;
    }

    @Override
    public boolean isPartial() {
        return partial;
    }

    @Override
    public Date toDate() {
        return date;
    }

    @Override
    public String toString() {
        return ISO8601.format( date );
    }

}
