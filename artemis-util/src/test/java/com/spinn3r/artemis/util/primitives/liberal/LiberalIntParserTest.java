package com.spinn3r.artemis.util.primitives.liberal;

import org.junit.Test;

import static org.junit.Assert.*;

public class LiberalIntParserTest {

    @Test
    public void testParse() throws Exception {

        assertEquals( 53400000, LiberalIntParser.parse( "53.4M") );
        assertEquals( 1000, LiberalIntParser.parse( "1k" ) );
        assertEquals( 0, LiberalIntParser.parse( "0" ) );
        assertEquals( 1000, LiberalIntParser.parse( "1,000" ) );
        assertEquals( 12000, LiberalIntParser.parse( "12K" ) );

    }

    @Test
    public void testParseLongLiteralString() throws Exception {

        assertEquals( 53456123, LiberalIntParser.parse( "53456123" ) );

    }

    @Test
    public void testParseLongLiteralStringWithCommas() throws Exception {

        assertEquals( 53456123, LiberalIntParser.parse( "53,456,123" ) );

    }

    @Test
    public void testParseSpacingNumber() throws Exception {

        assertEquals( 15_000_000, LiberalIntParser.parse( "15.0 Mi" ) );

    }
    
    @Test(expected = NumberFormatException.class)
    public void testParseBrokenString() throws Exception {

        LiberalIntParser.parse( "asdf" );

    }

}