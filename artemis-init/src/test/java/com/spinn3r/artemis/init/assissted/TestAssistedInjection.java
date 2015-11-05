package com.spinn3r.artemis.init.assissted;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.SyntheticClock;
import com.spinn3r.artemis.time.SystemClock;
import org.junit.Before;

/**
 *
 */
public class TestAssistedInjection {

    private Launcher launcer;

    @Before
    public void setUp() throws Exception {

        launcer = Launcher.forResourceConfigLoader().build();

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            //add( SystemClock)
        }

    }

    static class TestService extends BaseService {

        @Override
        public void init() {

            advertise( Clock.class, new SystemClock() );
            // provider( TestObjectFactory.class );

        }

    }

}
