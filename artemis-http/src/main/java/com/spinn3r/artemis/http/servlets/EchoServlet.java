package com.spinn3r.artemis.http.servlets;

import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A servlet that tries to echo what it's given for testing purposes.
 */
public class EchoServlet extends DefaultServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        echo( request, response );
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        echo( request, response );
    }

    protected void echo(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {

        resp.setContentType( req.getContentType() );

        byte[] buff = new byte[1024];

        try( InputStream is = req.getInputStream();
             OutputStream out = resp.getOutputStream() ) {

            int count = is.read( buff );

            if ( count == -1 )
                return;

            out.write( buff, 0, count );

        }

    }

}

