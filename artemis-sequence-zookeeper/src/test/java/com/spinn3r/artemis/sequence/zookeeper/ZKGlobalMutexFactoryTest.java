package com.spinn3r.artemis.sequence.zookeeper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexAcquireException;
import com.spinn3r.artemis.sequence.GlobalMutexFactory;
import com.spinn3r.artemis.sequence.zookeeper.init.ZKGlobalMutexService;
import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import com.spinn3r.artemis.zookeeper.embedded.EmbeddedZookeeperService;
import com.spinn3r.artemis.zookeeper.init.ZookeeperService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZKGlobalMutexFactoryTest {

    @Inject
    Provider<ZKGlobalMutexFactory> zkGlobalMutexFactoryProvider;

    Launcher launcher;

    @Before
    public void setUp() throws Exception {

        launcher = Launcher.forResourceConfigLoader().build();
        launcher.launch( new TestServiceReferences() );
        launcher.getInjector().injectMembers( this );

    }

    @After
    public void tearDown() throws Exception {

        if ( launcher != null )
            launcher.stop();

    }

    @Test
    public void testAcquire() throws Exception {

        ZKGlobalMutexFactory zkGlobalMutexFactory = zkGlobalMutexFactoryProvider.get();

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

        ZKGlobalMutexFactory zkGlobalMutexFactory = zkGlobalMutexFactoryProvider.get();

        for (int i = 0; i < ZKGlobalMutexFactory.RANGE * 2; ++i) {
            zkGlobalMutexFactory.acquire();
        }

    }

    @Test
    public void testFullLockRange() throws Exception {

        ZKGlobalMutexFactory zkGlobalMutexFactory = zkGlobalMutexFactoryProvider.get();

        for (int i = 0; i < ZKGlobalMutexFactory.RANGE; ++i) {
            zkGlobalMutexFactory.acquire(i);
        }

    }

    @Test(expected = GlobalMutexAcquireException.class)
    public void testTestFullLockRangeWithFailure() throws Exception {

        ZKGlobalMutexFactory zkGlobalMutexFactory = zkGlobalMutexFactoryProvider.get();

        for (int i = 0; i < ZKGlobalMutexFactory.RANGE; ++i) {
            zkGlobalMutexFactory.acquire(i);
        }

        zkGlobalMutexFactory.acquire();

    }

    @Test
    public void testCreatePath() throws Exception {

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( EmbeddedZookeeperService.class );
            add( ZookeeperService.class );
            add( ZKGlobalMutexService.class );
        }

    }

}