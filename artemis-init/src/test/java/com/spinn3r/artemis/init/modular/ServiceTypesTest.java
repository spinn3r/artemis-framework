package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ServiceTypesTest {

    @Test
    public void testDetermineServiceType() throws Exception {

        assertEquals(HostnameServiceType.class, ServiceTypes.determineServiceType(HostnameService.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedServiceType() throws Exception {

        ServiceTypes.determineServiceType(String.class);

    }

}