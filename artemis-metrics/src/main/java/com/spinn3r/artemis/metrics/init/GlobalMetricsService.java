package com.spinn3r.artemis.metrics.init;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.metrics.init.MetricsConfig;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexExpiredException;
import com.spinn3r.metrics.kairosdb.KairosDb;
import com.spinn3r.metrics.kairosdb.KairosDbReporter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Metrics service which includes a global mutex to avoid two nodes using the
 * same metric name.
 */
@Config( path = "metrics.conf",
         required = true,
         implementation = MetricsConfig.class )
public class GlobalMetricsService extends MetricsService {

    public static final String HOSTNAME = "hostname";
    public static final String GLOBAL_MUTEX = "global-mutex";
    public static final String ROLE = "role";
    private static final String KAIROSDB = "kairosdb";

    private KairosDbReporter kairosDbReporter = null;

    private JmxReporter jmxReporter = null;

    private final Provider<Hostname> hostnameProvider;

    private final Role role;

    private final Provider<GlobalMutex> globalMutexProvider;

    @Inject
    GlobalMetricsService(MetricsConfig metricConfig, Provider<Hostname> hostnameProvider, Role role, Provider<GlobalMutex> globalMutexProvider) {
        super( metricConfig );
        this.hostnameProvider = hostnameProvider;
        this.role = role;
        this.globalMutexProvider = globalMutexProvider;
    }

    @Override
    public void start() throws Exception {

        // TODO: refactor this so that it doesn't extend MetricsService but
        // requires it to be BEFORE it in the services and uses a injected
        // dependency.

        super.start();

        for ( MetricsConfig.Reporter reporter : config.getReporters() ) {

            if ( KAIROSDB.equals( reporter.getType() ) ) {
                startKairosDbReporter( reporter );
            }

        }

        // DO NOT use this for now as I believe it's causing us to lock up
        // the robot and this was only ever used by datadog which I'm not
        // aggressively using at the moment.
        //startJMXReporter( metricRegistry );

    }

    private void startKairosDbReporter( MetricsConfig.Reporter reporter ) throws IOException, GlobalMutexExpiredException {

        InetSocketAddress addr = new InetSocketAddress( reporter.getHostname(), reporter.getPort() );

        info( "Starting reporter for %s:%s", reporter.getHostname(), reporter.getPort() );

        KairosDb kairosDb = new KairosDb( addr );

        KairosDbReporter.Builder builder =
          KairosDbReporter.forRegistry( metricRegistry )
            .withTag( HOSTNAME, hostnameProvider.get().getValue() )
            .withTag( ROLE, role.getValue() )
            .garbageCollectAndDeriveCounters( true );

        if ( globalMutexProvider.get() == null ) {
            warn( "No global mutex.  Not using %s tag for metrics.", GLOBAL_MUTEX );
        } else {
            builder.withTag( GLOBAL_MUTEX, Integer.toString( globalMutexProvider.get().getValue() ) );
        }

        // we have to write metrics with hostname and global mutex or two
        // machines will overwrite the same value as they're using the same
        // metric.

        kairosDbReporter = builder.build( kairosDb );

        kairosDbReporter.start( reporter.getInterval(), TimeUnit.MILLISECONDS );

    }

    private void startJMXReporter( MetricRegistry metricRegistry ) {

        jmxReporter = JmxReporter.forRegistry( metricRegistry ).build();

        jmxReporter.start();

    }

    @Override
    public void stop() throws Exception {

        if ( kairosDbReporter != null ) {
            kairosDbReporter.stop();
        }

        if ( jmxReporter != null ) {
            jmxReporter.stop();
        }

    }

}
