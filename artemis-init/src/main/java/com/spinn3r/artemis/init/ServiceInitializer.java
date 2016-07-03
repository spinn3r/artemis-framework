package com.spinn3r.artemis.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory;
import com.spinn3r.artemis.init.config.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Load the config of a service.
 */
public class ServiceInitializer {

    private Launcher launcher;

    public ServiceInitializer(Launcher launcher) {
        this.launcher = launcher;
    }

    @SuppressWarnings( "unchecked" )
    public void init( ServiceReference serviceReference  ) {

        Config config = Configs.readConfigAnnotation( serviceReference.getBacking() );

        // we are done if we've already advertised the config

        if ( config == null ) {
            // the config is optional now ...
            return;
        }

        if ( config.implementation() == null ) {
            throw new NullPointerException( "config" );
        }

        if ( launcher.advertised.find( config.implementation() ) != null ) {
            return;
        }

        ConfigLoader configLoader = launcher.getConfigLoader();

        if ( configLoader == null ) {
            throw new RuntimeException( "configLoader" );
        }

        URL resource = null;

        if ( ! "".equals( config.path() ) ) {
            resource = configLoader.getResource( serviceReference.getBacking(), config.path() );
        }

        Class<?> configClazz = config.implementation();

        try {

            if ( resource == null ) {
                throw new IOException( String.format( "Config file not found: %s (loaded from %s)",
                                                      config.path(), serviceReference.getBacking() ) );
            }

            try( InputStream inputStream = resource.openStream(); ) {

                byte[] data = ByteStreams.toByteArray( inputStream );
                String content = new String( data, Charsets.UTF_8 );

                Loader<?> loader = new Loader( content, configClazz, launcher.advertised );

                try {
                    loader.load();
                } catch (Exception e) {
                    throw new RuntimeException( "Could not load config: ", e );
                }

                launcher.info( "Using %s for config %s for service %s", resource, configClazz, serviceReference );

            }

        } catch ( IOException e ) {

            if ( config.required() ) {

                throw new RuntimeException( e );

            } else {

                try {

                    Loader<?> loader = new Loader( "{}", configClazz, launcher.advertised );

                    loader.load();

                } catch (Exception e1) {
                    throw new RuntimeException( "Unable to create default config object: ", e1 );
                }

            }

        }

    }

    /**
     * Take the config and parse it into an instance then advertise it.
     */
    static class Loader<C> {

        private final String data;

        private final Class<C> clazz;

        private final Advertised advertised;

        public Loader(String data, Class<C> clazz, Advertised advertised) {
            this.data = data;
            this.clazz = clazz;
            this.advertised = advertised;
        }

        public void load() throws Exception {

            C instance = parse( data, clazz );

            advertised.advertise( this, clazz, instance );

        }

        private C parse(String data, Class<C> clazz ) throws Exception {

            try {

                ObjectMapper mapper = new ObjectMapper( new HoconFactory() );

                return mapper.readValue( data, clazz );

            } catch ( Exception e ) {

                // TODO: don't print this to stdout .. use the tracer
                System.out.printf( "Unable to parse: \n" );
                System.out.printf( "=====\n" );
                System.out.printf( "%s", data );
                System.out.printf( "=====\n" );
                throw e;

            }

        }

    }

}

