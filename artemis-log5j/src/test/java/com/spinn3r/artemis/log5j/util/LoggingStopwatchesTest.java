package com.spinn3r.artemis.log5j.util;

import com.spinn3r.artemis.log5j.MockLogTarget;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LoggingStopwatchesTest {

    @Test
    public void testCreate() throws Exception {

        MockLogTarget mockLogTarget = new MockLogTarget();

        LoggingStopwatch loggingStopwatch = LoggingStopwatches.create( mockLogTarget, getClass(), 100, TimeUnit.MILLISECONDS );

        loggingStopwatch.stop( "testCreate" );

        assertEquals( "[]", mockLogTarget.messages.toString() );

        // now add some sleep time in thee.

        Thread.sleep( 120 );

        loggingStopwatch.stop( "testCreate" );

        assertEquals( 1, mockLogTarget.messages.size() );

        String first = mockLogTarget.messages.get( 0 );

        assertTrue( first.startsWith( "Call to testCreate in com.spinn3r.artemis.log5j.util.LoggingStopwatchesTest took too long (" ) );

    }

}