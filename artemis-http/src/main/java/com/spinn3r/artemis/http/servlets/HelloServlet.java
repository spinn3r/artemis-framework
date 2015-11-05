package com.spinn3r.artemis.http.servlets;

import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public class HelloServlet extends DefaultServlet {

    private String message;

    public HelloServlet() {
        this( "hello world" );
    }

    public HelloServlet(String message) {
        this.message = message;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        byte[] data = message.getBytes();

        response.setHeader( "Content-Length", Integer.toString( data.length ) );

        try( OutputStream out = response.getOutputStream() ) {
            out.write( data );
        }

    }

}
