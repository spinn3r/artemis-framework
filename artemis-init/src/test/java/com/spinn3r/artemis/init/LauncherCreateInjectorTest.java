package com.spinn3r.artemis.init;

import com.google.inject.CreationException;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.init.example.DefaultFirstService;
import com.spinn3r.artemis.init.example.DefaultSecond;
import com.spinn3r.artemis.init.example.DefaultSecondService;
import com.spinn3r.artemis.init.example.Second;
import org.junit.Test;

import static com.spinn3r.artemis.init.Services.ref;

public class LauncherCreateInjectorTest {

    @Test
    public void testFirstOnly() throws Exception {

        ResourceConfigLoader configLoader = new ResourceConfigLoader();

        Launcher launcher =
          Launcher.newBuilder(configLoader )
                  .build();

        launcher.launch( ref( DefaultFirstService.class ) );

        launcher.stop();

    }

    @Test(expected = Exception.class)
    public void testSecondOnlyWithMissingDependencies() throws Exception {

        ResourceConfigLoader configLoader = new ResourceConfigLoader();

        Launcher launcher =
          Launcher.newBuilder(configLoader )
            .build();

        launcher.launch( ref( DefaultSecondService.class ) );

        launcher.getInstance( Second.class );

        launcher.stop();

    }

    @Test(expected = CreationException.class)
    public void testVerify() throws Exception {

        ResourceConfigLoader configLoader = new ResourceConfigLoader();

        Launcher launcher =
          Launcher.newBuilder(configLoader )
            .build();

        launcher.advertise( Second.class, DefaultSecond.class );
        launcher.verify();

    }

}