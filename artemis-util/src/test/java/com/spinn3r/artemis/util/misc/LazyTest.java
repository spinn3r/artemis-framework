package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import static org.junit.Assert.*;

public class LazyTest {

    @Test
    public void test1() throws Exception {

        Lazy<String> lazy = new Lazy<String>() {
            @Override
            protected String read() {
                return "hello";
            }
        };

        assertEquals( "hello", lazy.get() );
        assertTrue( lazy.initialized );

    }

    @Test(expected = RuntimeException.class )
    public void test2() throws Exception {

        Lazy<String> lazy = new Lazy<String>() {
            @Override
            protected String read() {
                throw new RuntimeException( "failed" );
            }
        };

        lazy.get();
    }

}