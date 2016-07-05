package com.spinn3r.artemis.init;

import com.google.inject.Inject;
import com.google.inject.Injector;
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
@SuppressWarnings( "unchecked" )
public class ConfigReader {

    private final Injector injector;

    @Inject
    ConfigReader(Injector injector) {
        this.injector = injector;
    }

    /**
     * Read the config of this object, in JSON, or return null if it's not
     * available.
     */
    public String read( Service service ) {

        return JSON.toJSON( readObject( service ) );

    }

    public Object readObject( Service service ) {

        Config config = Configs.readConfigAnnotation( service.getClass() );

        if ( config == null ) {
            return null;
        }

        return injector.getInstance( config.implementation() );

    }

}
