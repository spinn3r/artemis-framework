package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ZKGlobalMutexFactoryTestWithClientClose extends BaseZookeeperTest {

    private int connectionTimeoutMs = 60000;
    private int sessionTimeoutMs = 60000;

    @Test
    public void test1() throws Exception {
        String namespace = "artemis";
        String connectString = getZookeeperConnectString();

        try( ZKGlobalMutexFactory zkGlobalMutexFactory
               = new ZKGlobalMutexFactory( connectString, connectionTimeoutMs, sessionTimeoutMs, namespace ) ) {

            GlobalMutex globalMutex = zkGlobalMutexFactory.acquire();
            //zkGlobalMutexFactory.getCuratorClient().getZookeeperClient().close();

            // now shut down the server...
            testingServer.close();

            assertTrue( globalMutex.isExpired() );

        }

    }

}
