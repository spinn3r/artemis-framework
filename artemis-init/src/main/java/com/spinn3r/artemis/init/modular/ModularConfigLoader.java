package com.spinn3r.artemis.init.modular;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory;
import com.spinn3r.artemis.init.*;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.tracer.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 */
public class ModularConfigLoader {

    private final ModularLauncher modularLauncher;

    private final Tracer tracer;

    public ModularConfigLoader(ModularLauncher modularLauncher, Tracer tracer) {
        this.modularLauncher = modularLauncher;
        this.tracer = tracer;
    }

    @SuppressWarnings( "unchecked" )
    public Module load( ServiceReference serviceReference ) {

        Config config = Configs.readConfigAnnotation( serviceReference.getBacking() );

        // we are done if we've already advertised the config

        if ( config == null ) {
            // the config is optional now ...
            return null;
        }

        if ( config.implementation() == null ) {
            throw new NullPointerException( "config" );
        }

        //if ( modularLauncher.getAdvertised().find( config.implementation() ) != null ) {
        //    return null;
        //}

        ConfigLoader configLoader = modularLauncher.getConfigLoader();

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

                Loader<?> loader = new Loader( content, configClazz );

                try {

                    ConfigModule<?> result = loader.load();

                    tracer.info( "Using %s for config %s for service %s", resource, configClazz, serviceReference );

                    return result;

                } catch (Exception e) {
                    throw new RuntimeException( "Could not load config: ", e );
                }

            }

        } catch ( IOException e ) {

            if ( config.required() ) {

                throw new RuntimeException( e );

            } else {

                try {

                    Loader<?> loader = new Loader( "{}", configClazz );

                    return loader.load();

                } catch (Exception e1) {
                    throw new RuntimeException( "Unable to create default config object: ", e1 );
                }

            }

        }

    }

    /**
     * Take the config and parse it into an instance then advertise it.
     */
    class Loader<C> {

        private final String data;

        private final Class<C> clazz;

        public Loader(String data, Class<C> clazz) {
            this.data = data;
            this.clazz = clazz;
        }

        public ConfigModule<C> load() throws Exception {

            C instance = parse( data, clazz );
            return new ConfigModule<>( clazz, instance );

        }

        private C parse(String data, Class<C> clazz ) throws Exception {

            try {

                ObjectMapper mapper = new ObjectMapper( new HoconFactory() );

                return mapper.readValue( data, clazz );

            } catch ( Exception e ) {
                tracer.error( "Unable to parse: \n" );
                tracer.error( "=====\n" );
                tracer.error( "%s", data );
                tracer.error( "=====\n" );
                throw e;

            }

        }

    }

    /**
     * Module to use to inject this config into Guice.
     */
    class ConfigModule<C> extends AbstractModule {

        private final Class<C> clazz;

        private final C instance;

        public ConfigModule(Class<C> clazz, C instance) {
            this.clazz = clazz;
            this.instance = instance;
        }

        @Override
        protected void configure() {
            bind( clazz ).toInstance( instance );
        }

    }

}
