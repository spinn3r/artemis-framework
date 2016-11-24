package com.spinn3r.artemis.init;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.io.IOException;
import java.util.Set;

/**
 *
 */
public class AutoConfigurationModule extends AbstractModule {

    private final AutoConfigurationLoader autoConfigurationLoader;

    // keep track of manually bound auto configurations so that we can NOT load
    // them.   This is done for testing purposes so we don't have to load
    // everything from a ConfigLoader to test crazy/custom configurations.

    private final Set<Class<?>> manuallyBoundAutoConfigurations = Sets.newHashSet();

    AutoConfigurationModule(AutoConfigurationLoader autoConfigurationLoader) {
        this.autoConfigurationLoader = autoConfigurationLoader;
    }

    @Override
    protected void configure() {

        bindListener(Matchers.any(), new ProvisionListener() {
            @Override
            public <T> void onProvision(ProvisionInvocation<T> provision) {

                Class<? super T> rawType = provision.getBinding().getKey().getTypeLiteral().getRawType();

                provision.getBinding().getSource();

                if(isAutoConfiguration(rawType)) {

                    if( ! provision.getBinding().getSource().equals(rawType)) {
                        // this is a manually bound binding because the source
                        // is itself. If the source is NOT itself then someone
                        // is binding a configuration manually.
                        manuallyBoundAutoConfigurations.add(rawType);
                    }

                }

            }

        });

        bindListener(Matchers.any(), new TypeListener() {

            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {

                encounter.register(new InjectionListener<I>() {

                    @Override
                    public void afterInjection(I injectee) {

                        if (isAutoConfiguration(injectee.getClass())) {

                            // this is an auto configuration.

                            if(! manuallyBoundAutoConfigurations.contains(injectee.getClass())) {

                                // it is NOT manually bound which meands we need to load
                                // the configuration from resources.

                                try {
                                    autoConfigurationLoader.load(injectee);
                                } catch (IOException e) {
                                    throw new RuntimeException("Unable to load config: ", e);
                                }

                            }

                        }

                    }

                });

            }
        });

    }

    private boolean isAutoConfiguration(Class<?> clazz) {
        return clazz.isAnnotationPresent(AutoConfiguration.class);
    }

}
