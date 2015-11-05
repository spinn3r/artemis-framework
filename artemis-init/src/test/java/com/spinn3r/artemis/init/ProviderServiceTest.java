package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.init.example.DefaultFirstService;
import com.spinn3r.artemis.init.example.DefaultSecondProviderService;
import com.spinn3r.artemis.init.example.DefaultThirdService;
import com.spinn3r.artemis.init.example.Third;
import org.junit.Test;

import static com.spinn3r.artemis.init.Services.ref;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProviderServiceTest {

    @Test
    public void testProviderService() throws Exception {

        ResourceConfigLoader configLoader = new ResourceConfigLoader();

        Launcher launcher =
          Launcher.forConfigLoader( configLoader )
                  .build();

        launcher.launch( ref( DefaultFirstService.class ),
                         ref( DefaultSecondProviderService.class ),
                         ref( DefaultThirdService.class )
                         );

        Third third = launcher.getInstance( Third.class );

        assertNotNull( third.getSecond() );

        launcher.stop();

        assertNull( third.getSecond() );

    }

}