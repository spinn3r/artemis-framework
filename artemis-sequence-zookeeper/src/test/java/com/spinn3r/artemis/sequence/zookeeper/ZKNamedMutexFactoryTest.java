package com.spinn3r.artemis.sequence.zookeeper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.sequence.NamedMutex;
import com.spinn3r.artemis.sequence.NamedMutexException;
import com.spinn3r.artemis.sequence.NamedMutexFactory;
import com.spinn3r.artemis.sequence.zookeeper.init.ZKGlobalMutexService;
import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import com.spinn3r.artemis.zookeeper.init.ZookeeperService;
import org.junit.Before;
import org.junit.Test;

public class ZKNamedMutexFactoryTest extends BaseZookeeperTest {

    private int connectionTimeoutMs = 60000;
    private int sessionTimeoutMs = 60000;

    @Inject
    Provider<NamedMutexFactory> namedMutexFactoryProvider;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Launcher launcher = Launcher.forResourceConfigLoader().build();
        launcher.launch( new TestServiceReferences() );
        launcher.getInjector().injectMembers( this );

    }

    @Test
    public void testBasicAcquireThenRelease() throws Exception {

        NamedMutexFactory namedMutexFactory = namedMutexFactoryProvider.get();

        try( NamedMutex namedMutex = namedMutexFactory.acquire( "testmutex" ); ) {

            System.out.printf( "within the mutex... \n" );

        }

        try( NamedMutex namedMutex = namedMutexFactory.acquire( "testmutex" ); ) {

            System.out.printf( "within the mutex... second time. \n" );

        }

    }

    @Test( expected = NamedMutexException.AcquireException.class )
    public void testDoubleAcquireWithFailure() throws Exception {

        NamedMutexFactory namedMutexFactory = namedMutexFactoryProvider.get();

        namedMutexFactory.acquire( "testmutex" );
        namedMutexFactory.acquire( "testmutex" );

    }

    @Test( expected = NamedMutexException.AlreadyReleasedException.class )
    public void testAcquireWithFailureDueToDoubleRelease() throws Exception {

        NamedMutexFactory namedMutexFactory = namedMutexFactoryProvider.get();

        NamedMutex namedMutex = namedMutexFactory.acquire( "testmutex" );
        namedMutex.close();
        namedMutex.close();

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( ZookeeperService.class );
            add( ZKGlobalMutexService.class );
        }
    }

}