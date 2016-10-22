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
import com.spinn3r.artemis.init.tracer.TracerFactorySupplier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Component used to read {@link AutoConfiguration} objects and then read their
 * config data.
 */
public class AutoConfigurationLoader {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private final ConfigLoader configLoader;

    private final TracerFactorySupplier tracerFactorySupplier;

    @Inject
    AutoConfigurationLoader(ConfigLoader configLoader, TracerFactorySupplier tracerFactorySupplier) {
        this.configLoader = configLoader;
        this.tracerFactorySupplier = tracerFactorySupplier;
    }

    public void load(Object config) throws IOException {
        load(config, config.getClass().getAnnotation(AutoConfiguration.class));
    }

    public void load(Object config, AutoConfiguration autoConfiguration) throws IOException {

        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(autoConfiguration);

        Tracer tracer = tracerFactorySupplier.get().create(config);

        if ("".equals(autoConfiguration.path().trim())) {
            throw new AutoConfigurationException.InvalidPathException();
        }

        URL resource = configLoader.getResource( config.getClass(), autoConfiguration.path() );

        if ( resource != null ) {

            try(InputStream inputStream = resource.openStream(); ) {

                byte[] data = ByteStreams.toByteArray(inputStream );
                String content = new String(data, Charsets.UTF_8 );

                MAPPER.readerForUpdating(config).readValue(content);

                tracer.info("Using %s for AutoConfiguration: %s", resource, config.getClass().getName());

            }

        } else {

            if(autoConfiguration.required()) {
                // the configuration we're working with is required to load
                // from a config file.
                String msg = String.format("Config file not found: %s (loaded from %s)", autoConfiguration.path(), config.getClass());
                throw new AutoConfigurationException.MissingConfigException(msg);
            }

        }

    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(new HoconFactory() );
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new GuavaModule());
        return mapper;
    }

}
