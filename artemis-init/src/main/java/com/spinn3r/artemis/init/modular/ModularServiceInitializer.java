package com.spinn3r.artemis.init.modular;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.google.inject.AbstractModule;
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.init.Configs;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReference;
import com.spinn3r.artemis.init.config.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 */
public class ModularServiceInitializer {

    // steal the NEW code from ServiceInitializer...


    private ModularLauncher modularLauncher;

    ModularServiceInitializer(ModularLauncher modularLauncher) {
        this.modularLauncher = modularLauncher;
    }

    @SuppressWarnings( "unchecked" )
    public void init( ServiceReference serviceReference  ) {

        Config config = Configs.readConfigAnnotation( serviceReference.getBacking() );

        // we are done if we've already advertised the config

        if ( config == null ) {
            // the config is optional on services so if we don't have a
            // configuration to read we have no work left
            return;
        }

        if ( config.implementation() == null ) {
            throw new NullPointerException( "config" );
        }

        //if ( modularLauncher.getAdvertised().find( config.implementation() ) != null ) {
        //    return;
        //}

        ConfigLoader configLoader = modularLauncher.getConfigLoader();

        if ( configLoader == null ) {
            throw new RuntimeException( "configLoader" );
        }

        URL resource = null;

        if ( ! "".equals( config.path() ) ) {
            resource = configLoader.getResource( serviceReference.getBacking(), config.path() );
        }

        try {

            if ( resource == null ) {
                throw new IOException( String.format( "Config file not found: %s (loaded from %s)",
                  config.path(), serviceReference.getBacking() ) );
            }

            try(InputStream inputStream = resource.openStream(); ) {

                Object instance = parse( inputStream, config.implementation() );
//
//                modularLauncher.info( "Using %s for config %s for service %s", resource, instance.getClass().getName(), serviceReference );
//
//                modularLauncher.getAdvertised().advertise( this, config.implementation(), instance );

            }

        } catch ( IOException e ) {

            if ( config.required() ) {

                throw new RuntimeException( e );

            } else {

                try {

//                    Object instance = parse( "{}", config.implementation() );
//
//                    modularLauncher.getAdvertised().advertise( this, config.implementation(), instance );

                } catch (Exception e1) {
                    throw new RuntimeException( "Unable to create default config object: ", e1 );
                }

            }

        }

    }

    public static <T> T parse( InputStream inputStream, Class<T> configClass ) {

        try {

            try {

                InputStreamReader reader = new InputStreamReader( inputStream );

                String data = CharStreams.toString( reader );

                return parse( data, configClass );

            } finally {

                inputStream.close();

            }

        } catch (Exception e) {
            throw new RuntimeException( e );
        }

    }

    public static <T> T parse( String data, Class<T> configClass ) throws Exception {

        try {

            ObjectMapper mapper = new ObjectMapper( new HoconFactory() );

            return mapper.readValue( data, configClass );

        } catch ( Exception e ) {

            // TODO: don't print this to stdout .. use the tracer
            System.out.printf( "Unable to parse: \n" );
            System.out.printf( "=====\n" );
            System.out.printf( "%s", data );
            System.out.printf( "=====\n" );
            throw e;

        }

    }

    /**
     * Module to use to inject this config into Guice.
     */
    class ConfigModule extends AbstractModule {

        private Class<?> clazz;

        private Object instance;

        @Override
        protected void configure() {
//            bind( clazz ).toInstance( instance );

        }

    }

}
