package com.spinn3r.artemis.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.tracer.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Component used to read {@link AutoConfiguration} objects and then read their
 * config data.
 */
public class AutoConfigurationLoader {

    private final ConfigLoader configLoader;

    private final Tracer tracer;

    @Inject
    AutoConfigurationLoader(ConfigLoader configLoader, Tracer tracer) {
        this.configLoader = configLoader;
        this.tracer = tracer;
    }

    public void load(Object config, AutoConfiguration autoConfiguration) throws IOException {

        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(autoConfiguration);

        URL resource = null;

        if ( ! "".equals( autoConfiguration.path() ) ) {
            resource = configLoader.getResource( config.getClass(), autoConfiguration.path() );
        }

        if ( resource == null && autoConfiguration.required() ) {
            throw new IOException(String.format("Config file not found: %s (loaded from %s)", autoConfiguration.path(), config.getClass() ) );
        }

        try(InputStream inputStream = resource.openStream(); ) {

            byte[] data = ByteStreams.toByteArray(inputStream );
            String content = new String(data, Charsets.UTF_8 );

            load(content, config);

            tracer.info("Using %s for AutoConfiguration: %s", resource, config.getClass().getName());

        }

    }

    protected void load(String content, Object instance) throws IOException {

        ObjectMapper mapper = new ObjectMapper(new HoconFactory() );
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new GuavaModule());

        mapper.readerForUpdating(instance).readValue(content);

    }

}
