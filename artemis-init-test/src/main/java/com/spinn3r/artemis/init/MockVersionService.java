package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Version;

/**
 *
 */
public class MockVersionService extends BaseService {

    AtomicReferenceProvider<Version> versionProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( Version.class, versionProvider );
    }

    @Override
    public void start() throws Exception {
        versionProvider.set( new Version( "1.0.0" ) );
    }

}
