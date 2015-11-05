package com.spinn3r.artemis.init.config;

import java.net.URL;
import java.util.List;

/**
 *
 */
public class MultiConfigLoader implements ConfigLoader {

    private List<ConfigLoader> configLoaders;

    public MultiConfigLoader(List<ConfigLoader> configLoaders) {
        this.configLoaders = configLoaders;
    }

    @Override
    public URL getResource(Class clazz, String path) {

        for (ConfigLoader configLoader : configLoaders) {

            URL result = configLoader.getResource( clazz, path );

            if ( result != null )
                return result;

        }

        return null;
    }

    @Override
    public String toString() {
        return "MultiConfigLoader{" +
                 "configLoaders=" + configLoaders +
                 '}';
    }

}
