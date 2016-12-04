package com.spinn3r.artemis.http;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.servlets.EchoServlet;
import com.spinn3r.artemis.http.servlets.HelloServletFactory;
import com.spinn3r.artemis.http.servlets.RequestMetaServlet;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.init.resource_mutexes.PortMutex;
import com.spinn3r.artemis.init.resource_mutexes.PortMutexes;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.test.BaseTestWithCapturedOutput;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.util.misc.Strings;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import static com.jayway.awaitility.Awaitility.*;

public class ServerBuilderTest extends BaseTestWithCapturedOutput {

    private Server server;

    Launcher launcher;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    HelloServletFactory helloServletFactory;

    @Inject
    PortMutexes portMutexes;

    PortMutex portMutex;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();

        launcher = Launcher.newBuilder().build();

        launcher.launch( new TestServiceReferences() );

        launcher.getInjector().injectMembers( this );

        portMutex = portMutexes.acquire(8081, 9081);

    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();

        System.out.printf( "Stopping server..." );

        this.server.stop();

        portMutex.close();

    }

    @Test
    @Ignore
    public void testRequestMeta() throws Exception {

        this.server = new ServerBuilder()
                        .setPort( portMutex.getPort() )
                        .addServlet( "/", new RequestMetaServlet() )
                        .build();

        this.server.start();

        String link = String.format( "http://localhost:%s/", portMutex.getPort() );
        String content = httpRequestBuilder.get( link ).execute().getContentWithEncoding();

        System.out.printf( "content: %s\n", content );

        await().until( () -> {
            assertTrue( out.toString().contains( "GET / HTTP/1.1" ) );
            assertTrue( out.toString().contains( "org.eclipse.jetty.server.RequestLog 127.0.0.1" ) );
            assertTrue( out.toString().contains( "{HTTP/1.1}{0.0.0.0:8081}" ) );

        } );

    }

    @Test
    public void test1() throws Exception {

        this.server = new ServerBuilder()
                        .setPort( portMutex.getPort() )
                        .addServlet( "/", helloServletFactory.create() )
                        .build();

        this.server.start();

        String link = String.format( "http://localhost:%s/", portMutex.getPort() );
        String content = httpRequestBuilder.get( link ).execute().getContentWithEncoding();

        System.out.printf( "content: %s\n", content );

        assertEquals( "hello world", content );

        await().until( () -> {

            String out = tokenizeResponse(this.out.toString());

            assertTrue( out.contains( "GET //localhost:xxxx/ HTTP/1.1" ) );
            assertTrue( out.contains( "org.eclipse.jetty.server.RequestLog 127.0.0.1" ) );
            assertTrue( out.contains( "{HTTP/1.1,[http/1.1]}{0.0.0.0:xxxx}" ) );
        } );

    }

    private String tokenizeResponse(String content) {
        return content.replaceAll(":[0-9]+", ":xxxx");
    }

    @Test
    public void testWithLargePost() throws Exception {

        this.server = new ServerBuilder()
                        .setPort( portMutex.getPort() )
                        .setRequestHeaderSize( 64 * 1024 )
                        .setResponseHeaderSize( 64 * 1024 )
                        .addServlet( "/post", new EchoServlet() )
                        .build();

        this.server.start();

        int dataLength = 32_000;

        String data = Strings.repeat( "x", dataLength );

        assertEquals( dataLength, data.length() );

        String link = String.format( "http://localhost:%s/post", portMutex.getPort() );
        String content = httpRequestBuilder.post( link, data, "UTF-8", "text/plain" ).execute().getContentWithEncoding();

        assertEquals( data.length(), content.length() );

    }

    @Test
    public void testUseLocalhost() throws Exception {

        this.server = new ServerBuilder()
                        .setPort( portMutex.getPort() )
                        .setUseLocalhost( true )
                        .addServlet( "/", helloServletFactory.create() )
                        .build();

        this.server.start();

        String link = String.format( "http://localhost:%s/", portMutex.getPort() );
        String content = httpRequestBuilder.get( link ).execute().getContentWithEncoding();

        System.out.printf( "content: %s\n", content );

        assertEquals( "hello world", content );

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( MockVersionService.class );
            add( MockHostnameService.class );
            add( ConsoleLoggingService.class );
            add( DirectNetworkService.class );
        }

    }

}