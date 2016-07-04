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

        ConfigLoader configLoader = new FileConfigLoader( "/tmp" );

        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                    .build();

        launcher.launch( ref( MockFirstProviderService.class ) );

        FirstProvider firstProvider = launcher.getInstance(FirstProvider.class);

        assertEquals( "mock", firstProvider.getTest() );

    }

}
