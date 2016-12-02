package com.spinn3r.artemis.init;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.spinn3r.artemis.init.tracer.Tracer;

import java.util.List;
import java.util.function.Supplier;

import static com.spinn3r.artemis.init.Mode.*;

/**
 *
 */
public class AutoServiceModule extends AbstractModule {

    List<AutoService> startedAutoServices = Lists.newArrayList();

    private final Supplier<Mode> modeSupplier;

    private final Supplier<Tracer> tracerSupplier;

    protected AutoServiceModule(Supplier<Mode> modeSupplier, Supplier<Tracer> tracerSupplier) {
        this.modeSupplier = modeSupplier;
        this.tracerSupplier = tracerSupplier;
    }

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

                            if (autoService.getClass().getAnnotation(Singleton.class) == null) {

                                // for test purposes we don't care about mockito
                                if ( ! autoService.getClass().getName().contains("EnhancerByMockito")) {

                                    // Force @Singleton because we would end up having
                                    // too many instance references which would mean
                                    // we would run out of memory.

                                    throw new AutoServiceException.NotSingletonException("AutoService is not a singleton: " + autoService.getClass().getName());

                                }

                            }

                            if (LAUNCH.equals(modeSupplier.get())) {

                                startedAutoServices.add(autoService);

                                Tracer tracer = tracerSupplier.get();
                                Stopwatch stopwatch = Stopwatch.createStarted();

                                tracer.info( "Starting service: %s ...", autoService.getClass().getName() );

                                try {
                                    autoService.start();
                                } catch (Exception e) {
                                    throw new AutoServiceException.StartFailedException("Failed to start: " + autoService.getClass().getName(), e);
                                }

                                tracer.info( "Starting service: %s ...done (%s)", autoService.getClass().getName(), stopwatch.stop() );

                            }

                        }

                    }
                });

            }
        });

    }

    /**
     * Provide the ability to the auto services.  Needed to support stopping them.
     */
    public ImmutableList<AutoService> getStartedAutoServices() {
        return ImmutableList.copyOf(startedAutoServices);
    }

}
