package com.spinn3r.artemis.datetime.partial;

import com.spinn3r.artemis.time.Time;
import com.spinn3r.artemis.time.Times;

import java.util.Date;

/**
 * A partial date time is a date and time object which is preferably
 * zoned and may just have the date + time BUT the only required time range
 * is just the day with the timezone being optional.
 *
 */
public interface PartialDateTime {

    boolean isPartial();

    Date toDate();

    default Time toTime() {
        return Times.toTime(toDate());
    }

    default String toISO8601() {
        return toTime().toISO8601();
    }

}
