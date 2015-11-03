package com.spinn3r.artemis.util.primitives.liberal;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntParserTest {

    @Test
    public void testParse() throws Exception {

        assertEquals( 53_400_000, IntParser.parse( "53.4M") );
        assertEquals( 1000, IntParser.parse( "1k" ) );
        assertEquals( 0, IntParser.parse( "0" ) );
        assertEquals( 1000, IntParser.parse( "1,000" ) );
        assertEquals( 12000, IntParser.parse( "12K" ) );

    }

    @Test(expected = NumberFormatException.class)
    public void test2() throws Exception {

        IntParser.parse( "asdf" );

    }

}