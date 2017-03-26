package com.spinn3r.artemis.time;

import com.spinn3r.artemis.time.TimeRange;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TimeRangeTest {

    @Test
    public void testGetTimeMillis() throws Exception {

        TimeRange tr = new TimeRange( 5, TimeUnit.MINUTES );

        assertEquals( 5 * 60 * 1000, tr.getTimeMillis() );

        tr = new TimeRange( 5 * 60 * 1000, TimeUnit.MILLISECONDS );

        assertEquals( 5 * 60 * 1000, tr.getTimeMillis() );

    }

    @Test
    public void testFormat() throws Exception {

        assertEquals( "5m",
                      new TimeRange( 300010, TimeUnit.MILLISECONDS ).format() );

        assertEquals( "5m30s",
                      new TimeRange( 330000, TimeUnit.MILLISECONDS ).format() );

        assertEquals( "-5m30s",
                      new TimeRange( -330000, TimeUnit.MILLISECONDS ).format() );

        //assertEquals( "7d",
        //              new TimeRange( 7, TimeUnit.DAYS ).format() );

        assertEquals( "100ms",
                      new TimeRange( 100, TimeUnit.MILLISECONDS ).format() );

    }


    @Test
    public void testGet() throws Exception {

        assertEquals(60, TimeRange.ofMinutes(1).get(TimeUnit.SECONDS));
        assertEquals(60_000, TimeRange.ofMinutes(1).get(TimeUnit.MILLISECONDS));

    }

}