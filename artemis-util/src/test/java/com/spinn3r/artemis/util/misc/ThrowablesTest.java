package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThrowablesTest {

    @Test
    public void testToString() throws Exception {

        String value = Throwables.toString( new NullPointerException( "" ) );

        //System.out.printf( value );

        assertTrue( value != null );

    }
}