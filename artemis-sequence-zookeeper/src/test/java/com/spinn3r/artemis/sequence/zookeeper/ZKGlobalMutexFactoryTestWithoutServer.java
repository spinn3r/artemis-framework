package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.sequence.GlobalMutexAcquireException;
import org.junit.Test;

/**
 *
 * Test for situations where the server isn't running or where we can't connect.
 */
public class ZKGlobalMutexFactoryTestWithoutServer {

    @Test(expected = GlobalMutexAcquireException.class)
    public void test1() throws Exception {

        try( ZKGlobalMutexFactory zkGlobalMutexFactory
               = new ZKGlobalMutexFactory( "localhost:12345", 5000, 5000, "artemis" ) ) {

        }

    }
}
