package com.spinn3r.artemis.init.modular;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.modular.ModularServiceReferences;
import com.spinn3r.artemis.init.services.HostnameService;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LauncherTest {

    @Test
    public void testWithServiceTypes() throws Exception {

        ModularServiceReferences modularServiceReferences
          = new ModularServiceReferences()
                .put(HostnameService.class);

        assertEquals(1, modularServiceReferences.size());

        modularServiceReferences.put(MockHostnameService.class);

        assertEquals(1, modularServiceReferences.size());

        Launcher launcher = Launcher.forResourceConfigLoader().build();
        launcher.launch(modularServiceReferences.toServiceReferences());

        Injector injector = launcher.getInjector();

        ServiceMapping serviceMapping = modularServiceReferences.get(HostnameServiceType.class);
        assertEquals(MockHostnameService.class, serviceMapping.getTarget());

        Hostname hostname = injector.getInstance(Hostname.class);

        assertEquals("localhost", hostname.getValue());

    }

}