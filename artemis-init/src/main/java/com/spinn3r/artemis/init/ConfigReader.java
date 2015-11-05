package com.spinn3r.artemis.init;

import com.spinn3r.artemis.json.JSON;

/**
 * <p>
 * Given a specific object, look for the @Config annotation, read its advertised
 * config object and then convert it to JSON.
 * </p>
 *
 * <p>
 * Used primarily to debug the config of an object at runtime.
 * </p>
 *
 */
public class ConfigReader {

    private final Advertised advertised;

    public ConfigReader(Advertised advertised) {
        this.advertised = advertised;
    }

    /**
     * Read the config of this object, in JSON, or return null if it's not
     * available.
     *
     * @return
     */
    public String read( Service service ) {

        return JSON.toJSON( readObject( service ) );

    }

    public Object readObject( Service service ) {

        Config config = Configs.readConfigAnnotation( service.getClass() );

        if ( config == null ) {
            return null;
        }

        return advertised.require( config.implementation() );

    }

}
