package com.spinn3r.artemis.http.init;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.http.*;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import org.eclipse.jetty.server.Server;

/**
 * HTTP webserver for our admin interface.
 */
@Config( path = "webserver.conf",
         required = true,
         implementation = WebserverConfig.class )
public class WebserverService extends BaseService {

    private Server server;

    protected ServerBuilder serverBuilder = new ServerBuilder();

    protected final WebserverConfig webserverConfig;

    protected final ServletReferences servletReferences;

    protected final FilterReferences filterReferences;

    protected final RequestLogReferences requestLogReferences;

    protected final AtomicReferenceProvider<WebserverPort> webserverPortProvider = new AtomicReferenceProvider<>( null );

    @Inject
    WebserverService(WebserverConfig webserverConfig, ServletReferences servletReferences, FilterReferences filterReferences, RequestLogReferences requestLogReferences) {
        this.webserverConfig = webserverConfig;
        this.servletReferences = servletReferences;
        this.filterReferences = filterReferences;
        this.requestLogReferences = requestLogReferences;
    }

    @Override
    public void init() {
        provider( WebserverPort.class, webserverPortProvider );
    }

    @Override
    public void start() throws Exception {

        int port = webserverConfig.getPort();

        info( "Starting HTTP server on port %s with maxThreads=%s requestHeaderSize=%s, responseHeaderSize=%s...",
              port, webserverConfig.getMaxThreads(), webserverConfig.getRequestHeaderSize(), webserverConfig.getResponseHeaderSize() );

        webserverPortProvider.set( new WebserverPort( port ) );

        serverBuilder = new ServerBuilder()
            .setPort( port )
            .setMaxThreads( webserverConfig.getMaxThreads() );

        for (ServletReference servletReference : servletReferences) {

            info( "Adding %s as %s", servletReference.getPathSpec(), servletReference.getServletHolder().getClassName() );

            serverBuilder.addServlet( servletReference.getPathSpec(), servletReference.getServletHolder() );

        }

        for (FilterReference filterReference : filterReferences) {

            info( "Adding filter %s as %s", filterReference.getPathSpec(), filterReference.getFilterHolder().getClassName() );

            serverBuilder.addFilter( filterReference );

        }

        serverBuilder.setRequestLogReferences( requestLogReferences );
        serverBuilder.setUseLocalhost( webserverConfig.getUseLocalHost() );
        serverBuilder.setRequestHeaderSize( webserverConfig.getRequestHeaderSize() );
        serverBuilder.setResponseHeaderSize( webserverConfig.getResponseHeaderSize() );

        server = serverBuilder.build();

        try {
            server.start();
        } catch ( Exception e ) {
            throw new Exception( "Unable to start webserver on port: " + port, e );
        }

    }

    @Override
    public void stop() throws Exception {

        if ( server != null ) {
            server.stop();
        }

    }

}
