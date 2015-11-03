package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class BigIntegersTest {

    @Test
    public void testMean() throws Exception {

        assertEquals( 50, BigIntegers.mean( 0L, 100L ) );
        assertEquals( 50, BigIntegers.mean( 50L, 50L ) );

    }

}