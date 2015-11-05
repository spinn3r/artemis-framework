package com.spinn3r.artemis.test.http;

import com.spinn3r.artemis.http.ServletReference;
import com.spinn3r.artemis.http.ServletReferences;
import com.spinn3r.artemis.http.servlets.ChaosServlet;
import com.spinn3r.artemis.http.ServerBuilder;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for testing with HTTP by serving files locally.
 */
public class EmbeddedJettyTest {

    protected static final int PORT = 8081;

    private Server server;

    private DefaultResponseManager responseManager = new DefaultResponseManager();

    protected ServletReferences servletReferences = new ServletReferences();

    public EmbeddedJettyTest() { }

    public EmbeddedJettyTest(DefaultResponseManager responseManager) {
        this.responseManager = responseManager;
    }

    public DefaultResponseManager getResponseManager() {
        return responseManager;
    }

    @Before
    public void setUp() throws Exception {

        System.out.println( "Starting jetty... " );

        ServerBuilder serverBuilder = new ServerBuilder()
            .setPort( PORT )
            .addServlet( "/*", new ResponseGeneratorServlet( responseManager ) )
            .addServlet( "/chaos/*", new ChaosServlet() )
            ;

        for (ServletReference servletReference : servletReferences) {
            serverBuilder.addServlet( servletReference.getPathSpec(), servletReference.getServletHolder() );
        }

        this.server = serverBuilder.build();

        this.server.start();

        System.out.println( "Starting jetty... done" );

    }

    @After
    public void tearDown() throws Exception {
        System.out.println( "Shutting down jetty..." );
        this.server.stop();
        System.out.println( "Shutting down jetty...done" );
    }

}
