package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.ServiceType;
import org.junit.Test;

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