package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class HistographsTest {

    @Test
    public void testIntersection() throws Exception {

        Histograph<String> h0 = new Histograph<>();
        Histograph<String> h1 = new Histograph<>();

        h0.incr( "a" );
        h0.incr( "b" );

        h1.incr( "b" );
        h1.incr( "c" );

        Histograph<String> intersection = Histographs.intersection( h0, h1 );

        System.out.printf( "%s\n", intersection );

        assertEquals( "{b=2}", intersection.toString() );

    }

    @Test
    public void testUnion() throws Exception {

        Histograph<String> h0 = new Histograph<>();
        Histograph<String> h1 = new Histograph<>();

        h0.incr( "a" );
        h0.incr( "b" );

        h1.incr( "b" );
        h1.incr( "c" );

        Histograph<String> union = Histographs.union( h0, h1 );

        System.out.printf( "%s\n", union );

        assertEquals( "{b=2, a=1, c=1}", union.toString() );


    }

    @Test
    public void testSum() throws Exception {

    }
}