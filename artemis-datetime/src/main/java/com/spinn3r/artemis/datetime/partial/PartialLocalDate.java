package com.spinn3r.artemis.datetime.partial;

import com.spinn3r.artemis.datetime.LocalDates;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 */
public class PartialLocalDate implements PartialDateTime {

    private final LocalDate localDate;

    public PartialLocalDate(LocalDate localDate) {
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

}
