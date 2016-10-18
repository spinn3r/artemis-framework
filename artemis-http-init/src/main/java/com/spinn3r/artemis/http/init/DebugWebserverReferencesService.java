package com.spinn3r.artemis.http.init;

import com.codahale.metrics.servlets.PingServlet;
import com.google.inject.Inject;
import com.spinn3r.artemis.http.ServletReferences;
import com.spinn3r.artemis.http.servlets.ChaosServlet;
import com.spinn3r.artemis.http.servlets.EchoServlet;
import com.spinn3r.artemis.http.servlets.evaluate.EvaluateServlet;
import com.spinn3r.artemis.init.BaseService;

/**
 * Extended servlets for use in debugging and testing
 */
public class DebugWebserverReferencesService extends BaseService {

    private final ServletReferences servletReferences;

    private final EchoServlet echoServlet;

    private final ChaosServlet chaosServlet;

    @Inject
    DebugWebserverReferencesService(ServletReferences servletReferences, EchoServlet echoServlet, ChaosServlet chaosServlet) {
        this.servletReferences = servletReferences;
        this.echoServlet = echoServlet;
        this.chaosServlet = chaosServlet;
    }

    @Override
    public void init() {

        servletReferences.add( "/chaos", chaosServlet );
        servletReferences.add( "/ping", new PingServlet() );
        servletReferences.add( "/evaluate", new EvaluateServlet() );
        servletReferences.add( "/echo", new EchoServlet() );

    }

}
