package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.advertisements.Hostname;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIncludeService {

    @Inject
    Hostname hostname;

    static List<Class<? extends Service>> serviceInitOrder = Lists.newArrayList();

    static boolean STOPPED = false;

    @Test
    public void testLauncherAddingService() throws Exception {

        Launcher launcher = Launcher.newBuilder().build();
        launcher.launch( new TestServiceReferences() );

        launcher.getInjector().injectMembers( this );

        assertEquals( "fake.example.com", hostname.getValue() );

        assertEquals( "[class com.spinn3r.artemis.init.TestIncludeService$FirstService, class com.spinn3r.artemis.init.TestIncludeService$FakeHostnameService, class com.spinn3r.artemis.init.TestIncludeService$LastService]",
                      serviceInitOrder.toString() );

        // assert the ServiceReferences by calling launcher.getServiceReferences

        assertEquals( "[com.spinn3r.artemis.init.TestIncludeService$FirstService, com.spinn3r.artemis.init.TestIncludeService$FakeHostnameService, com.spinn3r.artemis.init.TestIncludeService$LastService]",
                      launcher.getServiceReferences().toString() );

        launcher.stop();

        assertTrue( STOPPED );

    }

    static class TestServiceReferences extends ServiceReferences {
        public TestServiceReferences() {
            add( FirstService.class );
            add( LastService.class );
        }
    }

    static class FirstService extends BaseService {

        @Override
        public void init() {
            include( FakeHostnameService.REF );
            serviceInitOrder.add( this.getClass() );
        }

    }

    static class FakeHostnameService extends BaseService {

        public static final ServiceReference REF = new ServiceReference( FakeHostnameService.class );

        @Override
        public void init() {
            advertise( Hostname.class, new Hostname( "fake.example.com" ) );
            serviceInitOrder.add( this.getClass() );
        }

        @Override
        public void stop() throws Exception {
            STOPPED = true;
        }

    }

    static class LastService extends BaseService {

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
        }
    }

}
