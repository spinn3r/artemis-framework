package com.spinn3r.artemis.http;

import com.google.common.collect.Lists;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.Servlet;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 */
public class ServerBuilder {

    private static final String LOCAL = "127.0.0.1";
    private int port = 8080;

    private String contextPath = "/";

    private boolean useLocalhost = false;

    private int maxThreads = 500;

    private int requestHeaderSize = 64 * 1024;

    private int responseHeaderSize = 64 * 1024;

    private ServletReferences servletReferences = new ServletReferences();

    private FilterReferences filterReferences = new FilterReferences();

    private RequestLogReferences requestLogReferences = new RequestLogReferences();

    public ServerBuilder setUseLocalhost( boolean useLocalhost ) {
        this.useLocalhost = useLocalhost;
        return this;
    }

    public boolean getUseLocalhost() {
        return useLocalhost;
    }

    public ServerBuilder setPort( int port ) {
        this.port = port;
        return this;
    }

    public ServerBuilder addServlet( String path, Servlet servlet ) {
        servletReferences.add( path, new ServletHolder( servlet ) );
        return this;
    }

    public ServerBuilder addServlet( String path, ServletHolder servletHolder ) {
        servletReferences.add( path, servletHolder );
        return this;
    }

    public ServerBuilder addFilter( FilterReference filterReference ) {
        filterReferences.add( filterReference );
        return this;
    }

    public ServletReferences getServletReferences() {
        return servletReferences;
    }

    public RequestLogReferences getRequestLogReferences() {
        return requestLogReferences;
    }

    public void setRequestLogReferences(RequestLogReferences requestLogReferences) {
        this.requestLogReferences = requestLogReferences;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public ServerBuilder setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    public ServerBuilder setRequestHeaderSize(int requestHeaderSize) {
        this.requestHeaderSize = requestHeaderSize;
        return this;
    }

    public ServerBuilder setResponseHeaderSize(int responseHeaderSize) {
        this.responseHeaderSize = responseHeaderSize;
        return this;
    }

    public Server build() {

        // http://www.eclipse.org/jetty/documentation/9.1.1.v20140108/embedding-jetty.html

        Server server = createServer();

        if ( useLocalhost ) {

            Connector[] connectors = server.getConnectors();

            if ( connectors.length != 1 ) {
                throw new RuntimeException( "Wrong number of connectors." );
            }

            ServerConnector serverConnector = (ServerConnector) connectors[ 0 ];

            serverConnector.setHost( LOCAL );

        }

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath( contextPath );


        // accrding to this: http://www.eclipse.org/jetty/documentation/9.1.1.v20140108/embedding-jetty.html
        //
        // we call setVirtualHosts here to set specific vhosts which we can then
        // use to set servlets.
        //
        // context.setVirtualHosts(  );
        //

        HandlerCollection handlers = new HandlerCollection();
        server.setHandler(handlers);

        // add the main context to the handlers
        handlers.addHandler( context );

        // **** now setup logging

        List<RequestLog> requestLogs = Lists.newArrayList();
        requestLogs.add( createSlf4jRequestLog() );

        for (RequestLogReference requestLogReference : requestLogReferences) {
            requestLogs.add( requestLogReference.getRequestLog() );
        }

        for (RequestLog requestLog : requestLogs) {

            RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setRequestLog(requestLog);

            handlers.addHandler( requestLogHandler );

        }

        // **** now add servlets

        for (ServletReference servletReference : servletReferences) {

            String pathSpec = servletReference.getPathSpec();
            ServletHolder servletHolder = servletReference.getServletHolder();

            context.addServlet( servletHolder, pathSpec );

        }

        // I can call context.addFilter() to add any filters I need
        // to check for either cookies or X-spinn3r-auth headers or vendor= headers

        for (FilterReference filterReference : filterReferences) {

            context.addFilter( filterReference.getFilterHolder(),
                               filterReference.getPathSpec(),
                               filterReference.getDispatches() );

        }

        // **** Now setup JMX support

        // http://www.eclipse.org/jetty/documentation/current/jmx-chapter.html

        MBeanContainer mbContainer = new MBeanContainer( ManagementFactory.getPlatformMBeanServer() );
        server.addEventListener( mbContainer );
        server.addBean( mbContainer );

        // Register loggers as MBeans
        server.addBean( Log.getLog() );

        //server.getThreadPool().

        return server;

    }

    private Server createServer() {

        QueuedThreadPool queuedThreadPool = new QueuedThreadPool( maxThreads );

        Server server = new Server( queuedThreadPool );

        HttpConfiguration httpConfiguration = new HttpConfiguration();

        httpConfiguration.setRequestHeaderSize( requestHeaderSize );
        httpConfiguration.setResponseHeaderSize( responseHeaderSize );

        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory( httpConfiguration );

        ServerConnector connector = new ServerConnector(server, httpConnectionFactory );
        connector.setPort(port);
        server.setConnectors( new Connector[]{ connector } );

        return server;

    }

    private RequestLog createNCSARequestLog() {

        NCSARequestLog requestLog = new NCSARequestLog( "/tmp/jetty-yyyy_mm_dd.request.log" );
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");

        return requestLog;

    }

    private RequestLog createSlf4jRequestLog() {

        Slf4jRequestLog requestLog = new Slf4jRequestLog();
        requestLog.setExtended(true);

        return requestLog;

    }

}

