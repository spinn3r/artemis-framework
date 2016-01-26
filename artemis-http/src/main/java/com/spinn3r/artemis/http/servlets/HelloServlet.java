package com.spinn3r.artemis.http.servlets;

import com.google.inject.Provider;
import com.spinn3r.artemis.init.advertisements.Version;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public class HelloServlet extends CrossOriginServlet {

    private Provider<Version> versionProvider;

    private String message;

    HelloServlet(Provider<Version> versionProvider, String message) {
        super( versionProvider );
        this.versionProvider = versionProvider;
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
