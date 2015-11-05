package com.spinn3r.artemis.metrics.gauge;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.spinn3r.artemis.metrics.init.MetricsConfig;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

/**
 *
 */
@Singleton
public class BackedGauges {

    private final Clock clock;

    private final TaggedMetrics taggedMetrics;

    private final int expirationIntervalMillis;

    @Inject
    public BackedGauges(Clock clock, TaggedMetrics taggedMetrics, MetricsConfig config ) {
        this.clock = clock;
        this.taggedMetrics = taggedMetrics;
        this.expirationIntervalMillis = config.getReporter().getInterval();
    }

    /**
     * Simple way to create gauges.
     */
    public <T> BackedGaugeIndex<T> gauge( GaugeProvider<T> gaugeProvider,
                                          Class clazz,
                                          String name0 ) {

        return new BackedGaugeIndex<>( clock, expirationIntervalMillis, taggedMetrics, gaugeProvider, clazz, name0 );

    }

}
