package com.spinn3r.artemis.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.util.List;

/**
 *
 */
public class AutoServiceModule extends AbstractModule {

    List<AutoService> autoServices = Lists.newArrayList();

    @Override
    protected void configure() {

        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {

                        if ( injectee instanceof AutoService) {
                            AutoService autoService = (AutoService) injectee;
                            autoServices.add(autoService);
                        }

                    }
                });

            }
        });

    }

    public ImmutableList<AutoService> getAutoServices() {
        return ImmutableList.copyOf(autoServices);
    }

}
