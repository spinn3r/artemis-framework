package com.spinn3r.artemis.init.config;

import java.net.URL;

/**
 * A ConfigLoader that doesn't actually load any files.  Can be used when
 * there isn't a config directory.
 */
public class NullConfigLoader implements ConfigLoader {

    @Override
    public URL getResource(Class clazz, String path) {
        return null;
    }

}
