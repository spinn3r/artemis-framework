package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class StackTest {

    @Test
    public void testCaller() throws Exception {

        assertEquals( "org.junit.runners.model.FrameworkMethod$1",
                      Stack.caller().getClassName() );

        assertEquals( "org.junit.runners.model.FrameworkMethod$1",
                      fromWithinSameClass().getClassName() );

    }

    private StackTraceElement fromWithinSameClass() {
        return Stack.caller();
    }

}