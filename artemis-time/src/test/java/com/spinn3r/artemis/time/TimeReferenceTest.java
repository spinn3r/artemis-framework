package com.spinn3r.artemis.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeReferenceTest {

    long offset = 10000;
    Clock clock = new SyntheticClock( offset );

    @Test
    public void test1() throws Exception {

        TimeReference timeReference = new TimeReference( clock, "+1hour" );
        assertEquals( offset + ( 60 * 60 * 1000 ), timeReference.getValue() );

        System.out.printf( "%s\n", timeReference.getValue() );

        timeReference = new TimeReference( clock, "+60minutes" );
        assertEquals( offset + (60 * 60 * 1000), timeReference.getValue() );

        timeReference = new TimeReference( clock, "-60minutes" );
        assertEquals( offset - (60 * 60 * 1000), timeReference.getValue() );

        timeReference = new TimeReference( clock, "+1day" );
        assertEquals( offset + (24 * 60 * 60 * 1000), timeReference.getValue() );

        timeReference = new TimeReference( clock, "2015-04-14T13:30:00Z" );
        assertEquals( 1429018200000L , timeReference.getValue() );

    }

    @Test(expected = RuntimeException.class)
    public void test2() throws Exception {

        TimeReference timeReference = new TimeReference( clock, "asdf" );

    }
}