package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Hostname;

/**
 *
 */
public class MockHostnameService extends BaseService {

    AtomicReferenceProvider<Hostname> hostnameProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( Hostname.class, hostnameProvider );
    }

    @Override
    public void start() throws Exception {
        hostnameProvider.set( new Hostname( "localhost" ) );
    }

}
