package com.spinn3r.artemis.init.services;

import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Version;
import com.spinn3r.artemis.init.advertisements.VersionServiceType;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.util.misc.Files;

public class VersionService extends BaseService implements Provider<Version>, VersionServiceType, ModularService {

    private Version version;

    @Override
    public void init() {
        provider( Version.class, this );
    }

    @Override
    protected void configure() {
        bind( Version.class ).toProvider( this );
    }

    @Override
    public void start() throws Exception {

        // this file is always in resources and we will never load it from the filesystem.
        System.getProperties().load( getClass().getResourceAsStream( "/artemis-version.properties" ) );

        String version = System.getProperty( "artemis.version" );

        if ( version == null )
            throw new Exception( "No version defined in system properties" );

        info( "Running artemis version: %s", version );

        this.version = new Version( version );

    }

    @Override
    public Version get() {
        return version;
    }

}
