package com.spinn3r.artemis.init;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.io.IOException;

/**
 *
 */
public class AutoConfigurationModule extends AbstractModule {

    private final AutoConfigurationLoader autoConfigurationLoader;

    AutoConfigurationModule(AutoConfigurationLoader autoConfigurationLoader) {
        this.autoConfigurationLoader = autoConfigurationLoader;
    }

    @Override
    protected void configure() {

        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {

                        if ( injectee.getClass().isAnnotationPresent(AutoConfiguration.class)) {
                            try {
                                autoConfigurationLoader.load(injectee);
                            } catch (IOException e) {
                                throw new RuntimeException("Unable to load config: ", e);
                            }
                        }

                    }
                });

            }
        });

    }

}
