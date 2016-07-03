package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.modular.ModularService;

/**
 *
 */
public class MockHostnameService extends BaseService implements ModularService, HostnameServiceType {

    AtomicReferenceProvider<Hostname> hostnameProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( Hostname.class, hostnameProvider );
    }

    @Override
    protected void configure() {
        bind( Hostname.class ).toProvider( hostnameProvider );
    }

    @Override
    public void start() throws Exception {
        hostnameProvider.set( new Hostname( "localhost" ) );
    }

}
