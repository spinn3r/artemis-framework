package com.spinn3r.artemis.init.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.init.advertisements.Version;

/**
 * Load properties into the system from a config file...
 */
@Config( path = "properties.conf",
         required = true,
         implementation = PropertiesConfig.class )
public class PropertiesService extends BaseService implements PropertiesServiceType {

    private PropertiesConfig config;

    @Inject
    public PropertiesService(PropertiesConfig config) {
        this.config = config;
    }

    @Override
    public void start() throws Exception {

        for( String key : config.keySet() ) {

            String value = config.get( key );

            info( "Setting system property %s=%s", key, value );
            System.setProperty( key, value );

        }

    }

}
