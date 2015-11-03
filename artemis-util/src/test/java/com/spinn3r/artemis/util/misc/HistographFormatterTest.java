package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class HistographFormatterTest {

    @Test
    public void testChart() throws Exception {

        Histograph<String> histograph = new Histograph<>();

        histograph.incr( "foo" );
        histograph.incr( "foo" );
        histograph.incr( "foo" );
        histograph.incr( "foo" );
        histograph.incr( "bar" );
        histograph.incr( "bar" );
        histograph.incr( "bar" );
        histograph.incr( "cat" );
        histograph.incr( "cat" );

        System.out.printf( "%s\n", HistographFormatter.chart( histograph ) );

    }

}