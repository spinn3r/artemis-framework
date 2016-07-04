package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIncludeExistingServices {

    static List<Class<? extends Service>> serviceInitOrder = Lists.newArrayList();

    @Test
    public void testLauncher() throws Exception {

        Launcher launcher = Launcher.newBuilder().build();
        launcher.launch( new TestServiceReferences() );

        launcher.getInjector().injectMembers( this );

        assertEquals( "[class com.spinn3r.artemis.init.TestIncludeExistingServices$FirstService, class com.spinn3r.artemis.init.TestIncludeExistingServices$SecondService]",
                      serviceInitOrder.toString() );

        assertEquals( "[com.spinn3r.artemis.init.TestIncludeExistingServices$FirstService, com.spinn3r.artemis.init.TestIncludeExistingServices$SecondService]",
                      launcher.getServiceReferences().toString() );

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( FirstService.class );
            add( FirstService.class );
            add( FirstService.class );
        }

    }

    static class FirstService extends BaseService {

        public static final ServiceReference REF = new ServiceReference( FirstService.class );

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
            include( FirstService.REF, SecondService.REF, SecondService.REF, FirstService.REF );
        }

    }

    static class SecondService extends BaseService {

        public static final ServiceReference REF = new ServiceReference( SecondService.class );

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
        }

    }


}
