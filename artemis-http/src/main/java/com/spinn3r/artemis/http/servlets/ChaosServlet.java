package com.spinn3r.artemis.http.servlets;

import com.spinn3r.artemis.http.parameters.Parameters;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.spinn3r.artemis.http.parameters.Parameters.*;

/**
 * A servlet that takes GET request parameters and returns output based on the
 * GET requests.  This can be used to generate synthetic results for testing
 * or debugging applications.
 */
public class ChaosServlet extends DefaultServlet {

    private static final int OK = 200;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Parameters params = fromRequest( request.getParameterMap() );

        int status =
            params.get( "status" )
                .first()
                .asInt( OK );

        List<String> headers = params.get( "header" ).values();

        for (String header : headers) {

            String[] pair = header.split( ":" );

            if ( pair.length != 2 ) {
                throw new RuntimeException( "Could not use header: " + header );
            }

            String name = pair[0];
            String value = pair[1];

            response.setHeader( name, value );

        }

        /*
        try ( ServletOutputStream out = response.getOutputStream() ) {

            out.write( "hello world".getBytes() );

        }
        */

        response.setStatus( status );

    }

}
