package com.spinn3r.artemis.datetime.partial;

import com.spinn3r.artemis.datetime.LocalDateTimes;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 */
public class PartialLocalDateTime implements PartialDateTime {

    private final LocalDateTime localDateTime;

    public PartialLocalDateTime(LocalDateTime localDateTime) {
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

}
