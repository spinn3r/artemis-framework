package com.spinn3r.artemis.datetime.partial.decorators;

import com.spinn3r.artemis.datetime.LocalDates;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 */
public class LocalDatePartialDateTimeDecorator implements PartialDateTime {

    private final LocalDate localDate;

    public LocalDatePartialDateTimeDecorator(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public boolean isPartial() {
        return true;
    }

    @Override
    public Date toDate() {
        return LocalDates.toDate(localDate);
    }

    @Override
    public String toString() {
        return localDate.toString();
    }

}
