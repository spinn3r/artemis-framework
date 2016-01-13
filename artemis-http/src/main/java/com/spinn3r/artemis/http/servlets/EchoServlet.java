package com.spinn3r.artemis.http.servlets;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType( "text/plain" );
        response.setCharacterEncoding( "UTF-8" );

        try ( OutputStream out = response.getOutputStream() ) {

            String content = request.getParameter( "message" );

            if ( content == null ) {
                content = "no message param";
            }

            out.write( content.getBytes( Charsets.UTF_8 ) );
        }

    }

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

        try( InputStream is = req.getInputStream();
             OutputStream out = resp.getOutputStream() ) {

            ByteStreams.copy(is, out);

        }

    }

}

