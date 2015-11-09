package com.spinn3r.artemis.sequence.zookeeper;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZKGlobalMutexTest {

    @Test
    public void testNextPotentialValue() throws Exception {

        Assert.assertEquals( 0, ZKGlobalMutexFactory.nextPotentialValue( 0.0F ) );
        assertEquals( ZKGlobalMutexFactory.RANGE / 2, ZKGlobalMutexFactory.nextPotentialValue( 0.5F ) );
        assertEquals( ZKGlobalMutexFactory.RANGE, ZKGlobalMutexFactory.nextPotentialValue( 1.0F ) );

    }

    @Test
    public void testCreatePath() throws Exception {

        assertEquals( "/artemis/mutexes/0", ZKGlobalMutexFactory.createPath( "/artemis/mutexes", 0 ) );
        assertEquals( "/artemis/mutexes/0", ZKGlobalMutexFactory.createPath( "/artemis/mutexes/", 0 ) );

    }
}