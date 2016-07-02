package com.spinn3r.artemis.init.modular;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import org.junit.Test;

import static org.junit.Assert.*;

public class LauncherTest {

    @Test
    public void testWithServiceTypes() throws Exception {

        ServiceTypeReferences serviceTypeReferences
          = new ServiceTypeReferences()
                .put(HostnameService.class);

        assertEquals(1, serviceTypeReferences.size());

        serviceTypeReferences.put(MockHostnameService.class);

        assertEquals(1, serviceTypeReferences.size());

        Launcher launcher = Launcher.forResourceConfigLoader().build();
        launcher.launch(serviceTypeReferences.toServiceReferences());

        Injector injector = launcher.getInjector();

        ServiceTypeReference serviceTypeReference = serviceTypeReferences.get(HostnameServiceType.class);
        assertEquals(MockHostnameService.class, serviceTypeReference.getTarget());

        Hostname hostname = injector.getInstance(Hostname.class);

        assertEquals("localhost", hostname.getValue());

    }

}