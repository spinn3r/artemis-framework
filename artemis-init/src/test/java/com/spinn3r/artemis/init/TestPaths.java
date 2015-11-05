package com.spinn3r.artemis.init;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class TestPaths {

    @Test
    public void test1() throws Exception {

        assertEquals( "/", Paths.get( "/").toString() );

        assertEquals( "/bar", Paths.get( "/", "/bar" ).toString() );


    }

}
