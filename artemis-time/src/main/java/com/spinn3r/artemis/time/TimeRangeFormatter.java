package com.spinn3r.artemis.time;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Format a time range into a human date.
 */
public class TimeRangeFormatter {

    public static String formatMillis(long millis) {
        return format(new TimeRange(millis, TimeUnit.MILLISECONDS));
    }

    public static String formatSeconds(long seconds) {
        return format(new TimeRange(seconds, TimeUnit.SECONDS));
    }

    public static String format( TimeRange timeRange ) {

        long millis = timeRange.getTimeMillis();

        boolean negative = millis < 0;

        millis = Math.abs( millis );

        Duration duration = new Duration( millis ); // in milliseconds

        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();
        periodFormatterBuilder
          //.appendYears()
          //.appendSuffix("y")
          .appendDays()
          .appendSuffix("d")
          .appendHours()
          .appendSuffix( "h" )
          .appendMinutes()
          .appendSuffix( "m" )
          .appendSeconds()
          .appendSuffix("s");

        if ( millis < 1000 ) {

            periodFormatterBuilder
              .appendMillis()
              .appendSuffix("ms")
            ;

        }

        PeriodFormatter formatter = periodFormatterBuilder.toFormatter();

        String result = formatter.print(duration.toPeriod());

        if ( negative ) {
            return "-" + result;
        } else {
            return result;
        }

    }

}
