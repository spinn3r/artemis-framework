package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexAcquireException;
import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZKGlobalMutexFactoryTest extends BaseZookeeperTest {

    private int connectionTimeoutMs = 60000;
    private int sessionTimeoutMs = 60000;

    @Test
    public void testAcquire() throws Exception {

        String namespace = "artemis";
        String connectString = getZookeeperConnectString();

        ZKGlobalMutexFactory zkGlobalMutexFactory = new ZKGlobalMutexFactory( connectString,
                                                                              connectionTimeoutMs,
                                                                              sessionTimeoutMs,
                                                                              namespace );

        assertNotNull( zkGlobalMutexFactory.acquire( 0 ) );
        assertNull( zkGlobalMutexFactory.acquire( 0 ) );

        assertNotNull( zkGlobalMutexFactory.acquire( 1 ) );

        GlobalMutex mutex = zkGlobalMutexFactory.acquire();

        assertNotNull( mutex );

        assertFalse( mutex.isExpired() );

        zkGlobalMutexFactory.close();

        assertTrue( mutex.isExpired() );

    }

    /**
     * This is a good test in practice each round trip to ZK takes time and it's
     * 16384^2 potential requests so it could take forever to run this test.
     * @throws Exception
     */
    @Test(expected = GlobalMutexAcquireException.class)
    @Ignore
    public void testLocksExhausted() throws Exception {

        String namespace = "artemis";
        String connectString = getZookeeperConnectString();

        try( ZKGlobalMutexFactory zkGlobalMutexFactory = new ZKGlobalMutexFactory( connectString, connectionTimeoutMs, sessionTimeoutMs, namespace ) ) {

            for (int i = 0; i < ZKGlobalMutexFactory.RANGE * 2; ++i) {
                zkGlobalMutexFactory.acquire();
            }
        }

    }

    @Test
    public void testFullLockRange() throws Exception {

        String namespace = "artemis";
        String connectString = getZookeeperConnectString();

        try( ZKGlobalMutexFactory zkGlobalMutexFactory = new ZKGlobalMutexFactory( connectString, connectionTimeoutMs, sessionTimeoutMs, namespace ) ) {

            for (int i = 0; i < ZKGlobalMutexFactory.RANGE; ++i) {
                zkGlobalMutexFactory.acquire(i);
            }

        }

    }

    @Test(expected = GlobalMutexAcquireException.class)
    public void testTestFullLockRangeWithFailure() throws Exception {

        // test the full lock range and one more...
        String namespace = "artemis";
        String connectString = getZookeeperConnectString();

        try( ZKGlobalMutexFactory zkGlobalMutexFactory = new ZKGlobalMutexFactory( connectString, connectionTimeoutMs, sessionTimeoutMs, namespace ) ) {

            for (int i = 0; i < ZKGlobalMutexFactory.RANGE; ++i) {
                zkGlobalMutexFactory.acquire(i);
            }

            zkGlobalMutexFactory.acquire();
        }


    }

    @Test
    public void testCreatePath() throws Exception {

    }

}