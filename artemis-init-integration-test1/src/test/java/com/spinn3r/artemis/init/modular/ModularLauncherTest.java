package com.spinn3r.artemis.init.modular;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModularLauncherTest {

    @Inject
    Hostname hostname;

    @Test
    public void testBasic() throws Exception {

        ModularServiceReferences serviceReferences
          = new ModularServiceReferences()
            .put( HostnameServiceType.class, MockHostnameService.class );

        ModularLauncher modularLauncher =
          ModularLauncher.create( serviceReferences ).build();

        modularLauncher.start();

        assertNotNull( modularLauncher.getInjector() );

        modularLauncher.getInjector().injectMembers( this );

    }

}



