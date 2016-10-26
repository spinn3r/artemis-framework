package com.spinn3r.artemis.datetime.durations;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple/liberal date time format for representing durations.
 *
 * ISO8601 defines a spec for durations but it's not really human readable.
 *
 * This is designed to be both human readable AND easy to parse.
 *
 * The format is an double followed by a unit of time.
 *
 * For example:
 *
 * 1month
 * 2months
 * 5minutes
 * 1minute
 *
 * We do not support multiple parts.  For example.  If you want 1 month and 15 days
 * you must put 45 days or 1.5 months.
 *
 * Units of time are standard except for:
 *
 * months: these are exactly 30 days
 * years: these are exactly 365 days
 *
 * Thus leap year and gregorian calendar are not factored into the interval.
 *
 * Note that technically we support ZERO duration.  It's a bit weird and
 * unusual but completely valid so we support it.
 *
 */
public class LiberalDurationParser {

    private static Pattern PATTERN = Pattern.compile("(?i)^([0-9.]+)((micros|millis|second|minute|hour|day|week|month|year)s?)$");

    public static Optional<Duration> parse(String text) {

        Matcher matcher = PATTERN.matcher(text);

        if (matcher.find()) {

            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2).toUpperCase();

            if ( ! unit.endsWith("S") ) {
                unit = unit + "S";
            }

            ChronoUnit chronoUnit = ChronoUnit.valueOf(unit);

            switch (unit) {

                case "WEEKS":
                    value *= 7;
                    chronoUnit = ChronoUnit.DAYS;
                    break;

                case "MONTHS":
                    value *= 30;
                    chronoUnit = ChronoUnit.DAYS;
                    break;

                case "YEARS":
                    value *= 365;
                    chronoUnit = ChronoUnit.DAYS;
                    break;

            }

            try {
                Duration duration = Duration.of(value, chronoUnit);
                return Optional.of(duration);
            } catch (UnsupportedTemporalTypeException e) {
                throw new UnsupportedTemporalTypeException("Unable to parse: " + text, e);
            }

        }

        return Optional.empty();

    }

}
