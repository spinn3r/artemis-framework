package com.spinn3r.artemis.init.config;

import java.net.URL;
import java.nio.file.Paths;

/**
 * Load files from resources.
 */
public class ResourceConfigLoader implements ConfigLoader {

    private String prefix;

    public ResourceConfigLoader() {
        this( "/" );
    }

    public ResourceConfigLoader(String prefix) {
        this.prefix = prefix;
    }

    public URL getResource(Class<?> clazz, String path) {

        path = Paths.get( prefix , "/" , path ).toString();

        return clazz.getResource( path );

    }

    @Override
    public String toString() {
        return "ResourceConfigLoader{" +
                 "prefix='" + prefix + '\'' +
                 '}';
    }

}
