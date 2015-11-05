package com.spinn3r.artemis.init.config;

/**
 *
 */
public class TestResourcesConfigLoader extends FileConfigLoader {

    public TestResourcesConfigLoader() {
        this( "src/test/resources" );
    }

    public TestResourcesConfigLoader( String path ) {
        super( path );
    }

}


