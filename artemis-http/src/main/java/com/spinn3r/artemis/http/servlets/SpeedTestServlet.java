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

    private static final int WRITE_LENGTH = 4096;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO: consider reading the length from the HTTP request.
        int length = WRITE_LENGTH * 200000;

        int nrWrites = length / WRITE_LENGTH;

        byte[] data = new byte[4096];

        int contentLength = data.length * nrWrites;

        response.setContentLength( contentLength );

        try( OutputStream out = response.getOutputStream() ) {

            for (int i = 0; i < nrWrites; i++) {
                out.write( data );
            }

        }

    }

}
