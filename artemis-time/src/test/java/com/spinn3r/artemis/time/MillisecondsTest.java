package com.spinn3r.artemis.time;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class MillisecondsTest {

    @Test
    public void testToMinutes() throws Exception {

        assertEquals( 5, Milliseconds.toMinutes( 5 * 60 * 1000 ) );

    }

}