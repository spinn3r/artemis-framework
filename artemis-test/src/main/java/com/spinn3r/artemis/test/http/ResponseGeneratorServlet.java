package com.spinn3r.artemis.test.http;

import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 *
 * A servlet that handles path to response generation and uses ResponseGenerators
 * so that the responses can be programatic.
 *
 */
public class ResponseGeneratorServlet extends DefaultServlet {

    private DefaultResponseManager responseManager;

    public ResponseGeneratorServlet(DefaultResponseManager responseManager) {
        this.responseManager = responseManager;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getPathInfo();

        System.out.printf( "path: %s\n", path );

        ResponseGenerator responseGenerator = responseManager.getResponseGenerator(path);

        if ( responseGenerator == null )
            throw new FileNotFoundException( path );

        response.setStatus( responseGenerator.getResponseCode() );

        try( OutputStream out = response.getOutputStream() ) {

            out.write( responseGenerator.getResponse() );

        }

    }


}
