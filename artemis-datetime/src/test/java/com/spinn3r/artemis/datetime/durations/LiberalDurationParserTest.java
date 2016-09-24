package com.spinn3r.artemis.datetime.durations;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.awt.SystemColor.text;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LiberalDurationParserTest {

    @Test
    public void parse() throws Exception {

        assertEquals("Optional[PT1M]", LiberalDurationParser.parse("1minute").toString());
        assertEquals("Optional[PT1M]", LiberalDurationParser.parse("1minutes").toString());
        assertEquals("Optional[PT2M]", LiberalDurationParser.parse("2minutes").toString());
        assertEquals("Optional[PT24H]", LiberalDurationParser.parse("1day").toString());
        assertEquals("Optional[PT48H]", LiberalDurationParser.parse("2day").toString());
        assertEquals("Optional[PT48H]", LiberalDurationParser.parse("2days").toString());
        assertEquals("Optional[PT168H]", LiberalDurationParser.parse("1weeks").toString());
        assertEquals("Optional[PT720H]", LiberalDurationParser.parse("1months").toString());
        assertEquals("Optional[PT8760H]", LiberalDurationParser.parse("1year").toString());

        ArrayList<ChronoUnit> chronoUnits = Lists.newArrayList(MILLIS, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);

        for (ChronoUnit chronoUnit : chronoUnits) {

            for (long value = 0; value < 40; value++) {

                long millis = value * toUnitMillis(chronoUnit);

                Duration expected = Duration.of(millis, MILLIS);

                String text = String.format("%s%s", value, chronoUnit.toString().toLowerCase() );

                test(expected, text);
                test(expected, toSingular(text));

                test(expected, text.toUpperCase());
                test(expected, toSingular(text).toUpperCase());

            }

        }

    }

    private void test(Duration expected, String text) {

        System.out.printf("Testing: %s\n", text);
        Optional<Duration> duration = LiberalDurationParser.parse(text);
        assertTrue("Unable to parse: " + text, duration.isPresent());
        assertEquals(expected.toMillis(), duration.get().toMillis());

    }

    private String toSingular(String value) {

        if (value.toUpperCase().endsWith("MILLIS"))
            return value;

        return value.replaceAll("s$", "");

    }

    private long toUnitMillis(ChronoUnit chronoUnit) {

        switch (chronoUnit) {

            case MILLIS:
                return 1L;

            case SECONDS:
                return 1000L;

            case MINUTES:
                return 60L * 1000L;

            case HOURS:
                return 60L * 60L * 1000;

            case DAYS:
                return 24L * 60L * 60L * 1000;

            case WEEKS:
                return 7L * 24L * 60L * 60L * 1000;

            case MONTHS:
                return 30L * 24L * 60L * 60L * 1000;

            case YEARS:
                return 365L * 24L * 60L * 60L * 1000;

        }

        throw new RuntimeException("Invalid unit: " + chronoUnit);

    }

}