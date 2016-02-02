package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.advertisements.Version;
import com.spinn3r.artemis.init.advertisements.VersionServiceType;
import com.spinn3r.artemis.init.modular.ModularService;

/**
 *
 */
public class MockVersionService extends BaseService implements ModularService, VersionServiceType {

    AtomicReferenceProvider<Version> versionProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( Version.class, versionProvider );
    }

    @Override
    protected void configure() {
        bind( Version.class ).toProvider( versionProvider );
    }

    @Override
    public void start() throws Exception {
        versionProvider.set( new Version( "1.0.0" ) );
    }

}
