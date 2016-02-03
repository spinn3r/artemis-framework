package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class BackingMapFacadeTest {

    @Test
    public void testPut() throws Exception {

        BackingMapFacade<ServiceType> backingMapFacade = new BackingMapFacade<>();
        //backingMapFacade.put( HostnameServiceType.class, MockHostnameService.class );

        BackingMap<ServiceType> backing = new BackingMap<>();

        //backingMapFacade.put( HostnameServiceType.class, MockHostnameService.class );

        //ClassMapping<ServiceType> classMapping = new ClassMapping<>( HostnameServiceType.class, HostnameService.class );

    }


}