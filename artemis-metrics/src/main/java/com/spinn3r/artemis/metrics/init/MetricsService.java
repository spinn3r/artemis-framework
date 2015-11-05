package com.spinn3r.artemis.metrics.init;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.metrics.kairosdb.DuplicateTagPolicy;
import com.spinn3r.metrics.kairosdb.InvalidTagPolicy;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * MetricsService with no reporter for use with basic stats and threads for
 * servlets
 */
@Config( path = "metrics.conf",
         required = true,
         implementation = MetricsConfig.class )
public class MetricsService extends BaseService {

    public static final String ROLE = "role";

    protected MetricRegistry metricRegistry;

    protected TaggedMetrics taggedMetrics;

    protected final MetricsConfig config;

    @Inject
    public MetricsService(MetricsConfig config) {
        this.config = config;
    }

    @Override
    public void init() {

        metricRegistry = new MetricRegistry();

        taggedMetrics = new TaggedMetrics( metricRegistry,
                                          InvalidTagPolicy.MANGLE_AND_LOG,
                                          DuplicateTagPolicy.IGNORE_AND_LOG );

        // we need advertise tagged metrics so other systems can use them.
        advertise( TaggedMetrics.class, taggedMetrics );

    }

    @Override
    public void start() throws Exception {

        if ( ! config.isEnabled() ) {
            warn( "Not initializing metrics (disabled)" );
            return;
        }

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        taggedMetrics.registerAll( "jvm.buffer-pool", new BufferPoolMetricSet( mBeanServer ) );
        taggedMetrics.registerAll( "jvm.threads", new ThreadStatesGaugeSet() );
        taggedMetrics.registerAll( "jvm.memory", new MemoryUsageGaugeSet() );
        taggedMetrics.registerAll( "jvm.gc", new GarbageCollectorMetricSet() );

        // ***  Add a gauge for the number of metrics so that I can see if we have any leaks.
        metricRegistry.register(metricRegistry.name( MetricRegistry.class, "size" ), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metricRegistry.getMetrics().size();
            }
        });

    }

}
