package com.spinn3r.artemis.datetime.partial.decorators;

import com.spinn3r.artemis.datetime.LocalDateTimes;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 */
public class LocalDateTimePartialDateTimeDecorator implements PartialDateTime {

    private final LocalDateTime localDateTime;

    public LocalDateTimePartialDateTimeDecorator(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean isPartial() {
        return true;
    }

    @Override
    public Date toDate() {
        return LocalDateTimes.toDate( localDateTime );
    }

    @Override
    public String toString() {
        return localDateTime.toString();
    }

}
