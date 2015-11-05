package com.spinn3r.artemis.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Simple interface to using Joda's DateTime system for parsing dates.
 */
public class ISO8601DateTimeParser {

    // http://joda-time.sourceforge.net/apidocs/org/joda/time/format/ISODateTimeFormat.html
    //
    // ISODateTimeFormat is thread-safe and immutable, and the formatters it
    // returns are as well.

    private static DateTimeFormatter FMT = ISODateTimeFormat.dateTime();

    private static DateTimeFormatter YEAR_MONTH_DAY_FMT = ISODateTimeFormat.yearMonthDay();

    public static DateTime parse( String value ) {
        return FMT.parseDateTime( value );
    }

    public static DateTime yearMonthDay( String value ) {
        return YEAR_MONTH_DAY_FMT.parseDateTime( value );
    }

}
