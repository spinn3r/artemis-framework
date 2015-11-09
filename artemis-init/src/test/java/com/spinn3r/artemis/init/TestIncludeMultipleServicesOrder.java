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
public class TestIncludeMultipleServicesOrder {

    static List<Class<? extends Service>> serviceInitOrder = Lists.newArrayList();

    @Test
    public void testLauncherAddingService() throws Exception {

        Launcher launcher = Launcher.forResourceConfigLoader().build();
        launcher.launch( new TestServiceReferences() );

        launcher.getInjector().injectMembers( this );

        assertEquals( "[class com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$FirstService, class com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$SecondService, class com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$ThirdService, class com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$LastService]",
                      serviceInitOrder.toString() );

        assertEquals( "[com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$FirstService, com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$SecondService, com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$ThirdService, com.spinn3r.artemis.init.TestIncludeMultipleServicesOrder$LastService]",
                      launcher.getServiceReferences().toString() );

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
            include( SecondService.REF, ThirdService.REF );
            serviceInitOrder.add( this.getClass() );
        }

    }

    static class SecondService extends BaseService {

        public static final ServiceReference REF = new ServiceReference( SecondService.class );

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
        }

    }

    static class ThirdService extends BaseService {

        public static final ServiceReference REF = new ServiceReference( ThirdService.class );

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
        }

    }

    static class LastService extends BaseService {

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
        }
    }

}
