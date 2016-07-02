package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.MockCallerService;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.ServiceTypeReferences;
import com.spinn3r.artemis.init.advertisements.CallerServiceType;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.advertisements.VersionServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import com.spinn3r.artemis.init.services.VersionService;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTypeReferencesTest {

    @Test
    public void testInclude() throws Exception {

        ServiceTypeReferences m0
          = new ServiceTypeReferences()
            .put( HostnameServiceType.class, MockHostnameService.class )
            .put( VersionServiceType.class, MockVersionService.class );

        ServiceTypeReferences m1
          = new ServiceTypeReferences()
            .put( CallerServiceType.class, MockCallerService.class );

        m0.include( HostnameServiceType.class, m1 );

        assertEquals( "               com.spinn3r.artemis.init.advertisements.HostnameServiceType = com.spinn3r.artemis.init.MockHostnameService                          \n" +
                        "                 com.spinn3r.artemis.init.advertisements.CallerServiceType = com.spinn3r.artemis.init.MockCallerService                            \n" +
                        "                com.spinn3r.artemis.init.advertisements.VersionServiceType = com.spinn3r.artemis.init.MockVersionService                           \n",
                      m0.format() );

    }

    @Test
    public void testReplace() throws Exception {

        ServiceTypeReferences m0
          = new ServiceTypeReferences()
              .put( HostnameServiceType.class, HostnameService.class )
              .put( VersionServiceType.class, VersionService.class );

        ServiceTypeReferences m1
          = new ServiceTypeReferences()
              .put( HostnameServiceType.class, MockHostnameService.class )
              .put( VersionServiceType.class, MockVersionService.class );


        m0.replace( m1 );

        assertEquals( 2, m0.size() );
        assertEquals( "ClassMapping{source=interface com.spinn3r.artemis.init.advertisements.HostnameServiceType, target=class com.spinn3r.artemis.init.MockHostnameService}",
                      m0.get( HostnameServiceType.class ).toString() );

        assertEquals( "ClassMapping{source=interface com.spinn3r.artemis.init.advertisements.VersionServiceType, target=class com.spinn3r.artemis.init.MockVersionService}",
                      m0.get( VersionServiceType.class ).toString() );

    }

}