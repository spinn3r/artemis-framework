package com.spinn3r.artemis.metrics.gauge;

import com.google.inject.Inject;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

/**
 *
 */
public class BackedGaugeIndexFactory {

    private static final int EXPIRATION_INTERVAL_MILLIS = 30000;

    private final TaggedMetrics taggedMetrics;

    private final Clock clock;

    private final BackedGaugeRegistry backedGaugeRegistry;

    @Inject
    BackedGaugeIndexFactory(TaggedMetrics taggedMetrics,
                            Clock clock,
                            BackedGaugeRegistry backedGaugeRegistry) {
        this.taggedMetrics = taggedMetrics;
        this.clock = clock;
        this.backedGaugeRegistry = backedGaugeRegistry;
    }

    public <T> BackedGaugeIndex<T> create( GaugeProvider<T> gaugeProvider,
                                           Class<?> clazz,
                                           String name0 ) {

        return new BackedGaugeIndex<>( clock,
                                       EXPIRATION_INTERVAL_MILLIS,
                                       taggedMetrics,
                                       gaugeProvider,
                                       clazz,
                                       name0 );

    }

    public <T> BackedGaugeIndex<T> createAndRegister( GaugeProvider<T> gaugeProvider,
                                                      Class<?> clazz,
                                                      String name0 ) {

        BackedGaugeIndex<T> result = create( gaugeProvider, clazz, name0 );

        backedGaugeRegistry.register( result );

        return result;

    }

}
