package com.spinn3r.artemis.datetime.partial.decorators;

import com.spinn3r.artemis.datetime.ZonedDateTimes;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.time.Time;
import com.spinn3r.artemis.time.Times;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 */
public class ZonedDateTimePartialDateTimeDecorator implements PartialDateTime {

    private final ZonedDateTime zonedDateTime;

    public ZonedDateTimePartialDateTimeDecorator(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    @Override
    public boolean isPartial() {
        return false;
    }

    @Override
    public Date toDate() {
        return ZonedDateTimes.toDate( zonedDateTime );
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    @Override
    public String toString() {
        return zonedDateTime.toString();
    }

}

