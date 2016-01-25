package com.spinn3r.artemis.sequence.zookeeper;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexFactory;
import com.spinn3r.artemis.sequence.zookeeper.init.ZKGlobalMutexService;
import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import com.spinn3r.artemis.zookeeper.embedded.EmbeddedZookeeperService;
import com.spinn3r.artemis.zookeeper.init.ZookeeperService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ZKGlobalMutexFactoryTestWithClientClose {

    private int connectionTimeoutMs = 60000;
    private int sessionTimeoutMs = 60000;

    @Inject
    GlobalMutexFactory zkGlobalMutexFactory;

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
    public void test1() throws Exception {

        GlobalMutex globalMutex = zkGlobalMutexFactory.acquire();
        launcher.stop();

        assertTrue( globalMutex.isExpired() );

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( EmbeddedZookeeperService.class );
            add( ZookeeperService.class );
            add( ZKGlobalMutexService.class );
        }

    }

}
