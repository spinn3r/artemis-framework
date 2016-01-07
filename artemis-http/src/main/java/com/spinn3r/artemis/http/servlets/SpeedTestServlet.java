package com.spinn3r.artemis.http.servlets;

import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *  Write a bunch of data to the output stream for running a speed test on our
 *  bandwidth.
 */
public class SpeedTestServlet extends DefaultServlet {

    private static final int BLOCK_SIZE = 4096;

    private static final int LENGTH = BLOCK_SIZE * 200000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int length = LENGTH;

        if ( request.getParameter( "length" ) != null ) {
            length = Integer.parseInt( request.getParameter( "length" ) );
        }

        int nrWrites = length / BLOCK_SIZE;

        byte[] data = new byte[4096];

        response.setContentLength( length );

        try( OutputStream out = response.getOutputStream() ) {

            for (int i = 0; i < nrWrites; i++) {
                out.write( data );
            }

        }

    }

}
