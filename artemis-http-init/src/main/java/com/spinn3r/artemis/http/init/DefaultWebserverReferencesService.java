package com.spinn3r.artemis.http.init;

import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.codahale.metrics.servlets.ThreadDumpServlet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.http.FilterReferences;
import com.spinn3r.artemis.http.RequestLogReferences;
import com.spinn3r.artemis.http.ServletReferences;
import com.spinn3r.artemis.http.servlets.EchoServlet;
import com.spinn3r.artemis.http.servlets.IndexServlet;
import com.spinn3r.artemis.http.servlets.RequestMetaServlet;
import com.spinn3r.artemis.http.servlets.SpeedTestServlet;
import com.spinn3r.artemis.http.servlets.evaluate.EvaluateServlet;
import com.spinn3r.artemis.http.servlets.hostmeta.HostMeta;
import com.spinn3r.artemis.http.servlets.hostmeta.HostMetaServlet;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.advertisements.Version;
import com.spinn3r.artemis.init.servlets.ConfigServlet;
import com.spinn3r.artemis.time.Uptime;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

/**
 * The default servlets and filters we should run with.
 */
public class DefaultWebserverReferencesService extends BaseService {

    private Provider<Version> versionProvider;
    private Role role;
    private Provider<Hostname> hostnameProvider;
    private TaggedMetrics taggedMetrics;

    private ServletReferences servletReferences = new ServletReferences();
    private FilterReferences filterReferences = new FilterReferences();
    private RequestLogReferences requestLogReferences = new RequestLogReferences();

    private final Uptime uptime;

    @Inject
    DefaultWebserverReferencesService(Provider<Version> versionProvider,
                                      Role role,
                                      Provider<Hostname> hostnameProvider,
                                      TaggedMetrics taggedMetrics,
                                      Uptime uptime) {

        this.versionProvider = versionProvider;
        this.role = role;
        this.hostnameProvider = hostnameProvider;
        this.taggedMetrics = taggedMetrics;
        this.uptime = uptime;

    }

    @Override
    public void init() {

        advertise( ServletReferences.class, servletReferences );
        advertise( FilterReferences.class, filterReferences );
        advertise( RequestLogReferences.class, requestLogReferences );

    }

    @Override
    public void start() throws Exception {

        HostMeta hostMeta = new HostMeta( versionProvider.get().getValue(),
                                          role.getValue(),
                                          hostnameProvider.get().getValue(),
                                          uptime.format() );

        //servletReferences.add( "/threads", new ThreadDumpServlet() );
        //servletReferences.add( "/metrics", new MetricsServlet( taggedMetrics.getMetricRegistry() ) );
        servletReferences.add( "/ping", new PingServlet() );
        servletReferences.add( "/speed-test", new SpeedTestServlet() );

        // TODO: removed /config during refactor.  We shouldn't expose the raw
        // services but should instead expose a map from ServiceClass to the
        // config object it loaded via an dependency that we inject later
        //servletReferences.add( "/config", new ConfigServlet( getAdvertised(), getServices() ) );
        servletReferences.add( "/host-meta", new HostMetaServlet( hostMeta ) );
        servletReferences.add( "/", new IndexServlet( servletReferences, hostnameProvider.get().getValue(), role.getValue() ) );
        servletReferences.add( "/request-meta", new RequestMetaServlet() );
        servletReferences.add( "/evaluate", new EvaluateServlet() );
        servletReferences.add( "/echo", new EchoServlet() );

    }

}
