package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.sequence.GlobalMutexAcquireException;
import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import org.junit.Test;

/**
 *
 */
public class ZKGlobalMutexFactoryTestWithServerClose extends BaseZookeeperTest {

    @Test(expected = GlobalMutexAcquireException.class)
    public void test1() throws Exception {

        // create a connection... then shutdown the client, make sure the locks
        // expire.

        try( ZKGlobalMutexFactory zkGlobalMutexFactory
               = new ZKGlobalMutexFactory( "localhost:12345", 5000, 5000, "artemis" ) ) {

            zkGlobalMutexFactory.acquire();
        }


    }

}
