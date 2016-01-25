package com.spinn3r.artemis.init.resource_mutexes;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PortMutexesTest {

    @Test
    public void testAcquire() throws Exception {

        PortMutexes portMutexes = new PortMutexes();

        try( ResourceMutex resourceMutex = portMutexes.acquire( 8080, 9080 ) ) {

            System.out.printf( "acquired!\n" );
            assertEquals( "", resourceMutex.backing.getAbsolutePath() );

        }

    }

    @Test(expected = ResourceMutexException.ExhaustedException.class)
    public void testExhaustion() throws Exception {

        PortMutexes portMutexes = new PortMutexes();

        portMutexes.acquire( 8080, 8081 );
        portMutexes.acquire( 8080, 8081 );
        portMutexes.acquire( 8080, 8081 );

    }

}