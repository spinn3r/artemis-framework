package com.spinn3r.artemis.datetime.partial;

import com.spinn3r.artemis.datetime.partial.decorators.DatePartialDateTimeDecorator;
import com.spinn3r.artemis.datetime.partial.decorators.LocalDatePartialDateTimeDecorator;
import com.spinn3r.artemis.datetime.partial.decorators.LocalDateTimePartialDateTimeDecorator;
import com.spinn3r.artemis.datetime.partial.decorators.ZonedDateTimePartialDateTimeDecorator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 */
public class PartialDateTimes {

    public static PartialDateTime forDate( Date date ) {
        return new DatePartialDateTimeDecorator( date, false );
    }

    public static PartialDateTime forLocalDate(LocalDate localDate) {
        return new LocalDatePartialDateTimeDecorator( localDate );
    }

    public static PartialDateTime forLocalDateTime(LocalDateTime localDateTime) {
        return new LocalDateTimePartialDateTimeDecorator( localDateTime );
    }

    public static PartialDateTime forZonedDateTime(ZonedDateTime zonedDateTime) {
        return new ZonedDateTimePartialDateTimeDecorator( zonedDateTime );
    }

}
