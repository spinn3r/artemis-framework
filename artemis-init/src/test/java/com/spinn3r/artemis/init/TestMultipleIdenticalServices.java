package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TestMultipleIdenticalServices {

    static List<Class<? extends Service>> serviceInitOrder = Lists.newArrayList();

    @Test
    public void testLauncher() throws Exception {

        Launcher launcher = Launcher.newBuilder().build();
        launcher.launch( new TestServiceReferences() );

        launcher.getInjector().injectMembers( this );

        assertEquals( "[class com.spinn3r.artemis.init.TestMultipleIdenticalServices$FirstService]",
                      serviceInitOrder.toString() );

        assertEquals( "[com.spinn3r.artemis.init.TestMultipleIdenticalServices$FirstService]",
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

        @Override
        public void init() {
            serviceInitOrder.add( this.getClass() );
        }

    }

}
