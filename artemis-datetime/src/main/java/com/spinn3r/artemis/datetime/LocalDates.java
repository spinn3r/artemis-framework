package com.spinn3r.artemis.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 *
 */
public class LocalDates {

    public static Date toDate(LocalDate localDate ) {
        return Date.from(localDate.atStartOfDay( ZoneOffset.UTC ).toInstant());
    }

}
