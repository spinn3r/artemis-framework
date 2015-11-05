package com.spinn3r.artemis.metrics.init.uptime;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.metrics.Stat;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.Started;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

import static com.spinn3r.artemis.metrics.Stat.stat;

/**
 * Keep track of system uptime and produce metrics about uptime .
 */
public class UptimeMetricsService extends BaseService {

    private static final int MILLIS_IN_MINUTE = 60 * 1000;

    private static final String STARTED = "started";
    private static final String UPTIME = "uptime";

    private final Clock clock;

    private final TaggedMetrics taggedMetrics;

    private Stat<Started> startedStat = null;

    @Inject
    UptimeMetricsService(Clock clock,
                         TaggedMetrics taggedMetrics,
                         StartedTagsProvider startedTagsProvider) {

        this.clock = clock;
        this.taggedMetrics = taggedMetrics;

        this.startedStat = stat( taggedMetrics, startedTagsProvider, STARTED );

    }

    @Override
    public void start() throws Exception {

        final Started started = new Started( clock.getTime() );

        this.startedStat.incr( started );

        MetricRegistry metricRegistry = taggedMetrics.getMetricRegistry();

        metricRegistry.register( MetricRegistry.name( UptimeMetricsService.class, UPTIME ), new Gauge<Long>() {

            @Override
            public Long getValue() {

                long uptime = clock.currentTimeMillis() - started.getTime().getTimeMillis();

                long uptimeInMinutes = uptime / MILLIS_IN_MINUTE;

                return uptimeInMinutes;

            }

        });

    }

}
