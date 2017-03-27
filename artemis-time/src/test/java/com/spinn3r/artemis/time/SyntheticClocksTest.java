package com.spinn3r.artemis.time;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class SyntheticClocksTest {

    @Test
    public void testRecentClock() throws Exception {

        assertThat( SyntheticClocks.recentClock().getTime().toISO8601(),
                    equalTo( "2014-06-22T01:08:52Z" ) );

    }

    @Test
    public void testTimeRounded() throws Exception {

        SyntheticClock syntheticClock = SyntheticClocks.recentClock();

        Time time = syntheticClock.getTime();

        assertEquals( 1403399332247L, time.toMillis() );
        assertEquals( 1403399332000L, time.withRoundedMillis().toMillis() );

    }

}