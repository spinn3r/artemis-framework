package com.spinn3r.artemis.init.config;

import java.net.URL;

/**
 * Main interface that we can inject that supports loading from the filesystem
 * or from resources.  Can support other providers in the future as well.
 */
public interface ConfigLoader {

    /**
     * Read a given resource from the relative path.
     *
     * @param clazz The class loading the config. Used to load from resources.
     * @param path The path of the file we are attempting to load
     *
     * @return The resources loaded as a URL or null if it's not found.
     */
    URL getResource(Class clazz, String path);

}
