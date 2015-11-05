package com.spinn3r.artemis.init.findservice;

import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.FileConfigLoader;
import org.junit.Test;

import static org.junit.Assert.*;

import static com.spinn3r.artemis.init.Services.*;

/**
 *
 */
public class TestFindService {

    @Test
    public void test1() throws Exception {

        State state = new State();

        ConfigLoader configLoader = new FileConfigLoader( "/tmp" );

        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                    .build();

        launcher.launch( ref( MockFirstProviderService.class ) );

        assertNotNull( launcher.getAdvertised() );
        assertNotNull( launcher.getAdvertised().find( FirstProvider.class ) );

        assertEquals( "mock",
                      launcher.getAdvertised().find( FirstProvider.class ).getTest() );

    }

}
