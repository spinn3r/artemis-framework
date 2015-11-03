package com.spinn3r.artemis.util.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class PercentageTest {

    @Test
    public void testPercValues() throws Exception {

        assertEquals( 0, Percentage.perc( 0, 100 ), 0.0 );
        assertEquals( 5, Percentage.perc( 5, 100 ), 0.0 );
        assertEquals( 50, Percentage.perc( 50, 100 ), 0.0 );
        assertEquals( 100, Percentage.perc( 100, 100 ), 0.0 );

    }

    @Test( expected = NoPercentException.class )
    public void testFailed() throws Exception {
        Percentage.perc( 100, 2 );
    }

    @Test( expected = NoPercentException.class )
    public void testZeroTotal() throws Exception {
        Percentage.perc( 100, 0 );
    }

    @Test
    public void testFormat() throws Exception {

        assertEquals( "0.00%", Percentage.format( 0, 100 ) );
        assertEquals( "50.00%", Percentage.format( 50, 100 ) );
        assertEquals( "100.00%", Percentage.format( 100, 100 ) );

    }

}