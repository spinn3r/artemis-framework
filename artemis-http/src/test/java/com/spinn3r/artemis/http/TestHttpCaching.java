package com.spinn3r.artemis.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public class TestHttpCaching {


    public static void main(String[] args) throws Exception {

        Server server = new ServerBuilder()
                        .addServlet( "/", new CacheServlet() )
                        .build();

        server.start();

    }

}

class CacheServlet extends DefaultServlet {

    private long cacheAge = 30;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long expiry = System.currentTimeMillis() + (cacheAge * 1000);

        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setDateHeader("Expires", expiry);
        httpResponse.setHeader("Cache-Control", "public" );
        //httpResponse.setHeader( "ETag", "9999" );

        System.out.printf( "Got a request\n" );

        try( OutputStream out = response.getOutputStream() ) {
            out.write( "hello world".getBytes() );
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.printf( "Got a HEAD request.\n" );

        super.doHead( req, resp );
    }
}