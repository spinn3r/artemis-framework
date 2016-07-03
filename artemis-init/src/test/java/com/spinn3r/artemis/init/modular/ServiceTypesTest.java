package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.ServiceTypes;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import org.junit.Assert;
import org.junit.Test;

public class ServiceTypesTest {

    @Test
    public void testDetermineServiceType() throws Exception {

        Assert.assertEquals(HostnameServiceType.class, ServiceTypes.determineServiceType(HostnameService.class));

    }

}