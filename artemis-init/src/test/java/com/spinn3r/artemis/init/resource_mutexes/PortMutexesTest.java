package com.spinn3r.artemis.init.resource_mutexes;

import org.junit.Test;

import static org.junit.Assert.*;

public class PortMutexesTest {

    @Test
    public void testAcquire() throws Exception {

        PortMutexes portMutexes = new PortMutexes();

        try( PortMutex portMutex = portMutexes.acquire( 8080, 9080 ) ) {

            System.out.printf( "acquired!\n" );
            assertEquals( "/tmp/named-mutexes/ports/" + portMutex.getPort(), portMutex.backing.getAbsolutePath() );
            assertTrue( portMutex.backing.exists() );

        }

    }

    @Test(expected = ResourceMutexException.ExhaustedException.class)
    public void testExhaustion() throws Exception {

        PortMutexes portMutexes = new PortMutexes();

        try( PortMutex portMutex0 = portMutexes.acquire( 8080, 8081 );
             PortMutex portMutex1 = portMutexes.acquire( 8080, 8081 );
             PortMutex portMutex2 = portMutexes.acquire( 8080, 8081 ); ) {


        }

    }

}