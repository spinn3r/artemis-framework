package com.spinn3r.artemis.init.config;

import com.google.common.collect.Lists;

/**
 *
 */
public class MultiConfigLoaders {

    /**
     * Get an instance of a multi config loader from the given config loaders.
     *
     */
    public static ConfigLoader create(ConfigLoader... configLoaders ) {
        return new MultiConfigLoader( Lists.newArrayList( configLoaders ) );
    }

}
