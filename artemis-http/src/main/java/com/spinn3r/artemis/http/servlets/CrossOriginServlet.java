package com.spinn3r.artemis.http.servlets;

import com.google.inject.Provider;
import com.spinn3r.artemis.init.advertisements.Version;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Set headers for access control, encoding, etc.
 */
public class CrossOriginServlet extends DefaultServlet {

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    private static final String X_POWERED_BY = "X-Powered-By";
    private static final String X_SERVER = "X-server";
    private static final String X_VERSION = "X-version";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    protected final Provider<Version> versionProvider;

    protected CrossOriginServlet(Provider<Version> versionProvider) {
        this.versionProvider = versionProvider;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader( ACCESS_CONTROL_ALLOW_ORIGIN, "*" );
        response.setHeader( X_POWERED_BY, "Spinn3r" );
        response.setHeader( X_SERVER, "Spinn3r" );
        response.setHeader( X_VERSION, versionProvider.get().toString() );

        response.setCharacterEncoding("UTF-8");

    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader( ACCESS_CONTROL_ALLOW_ORIGIN, "*" );
        response.setHeader( ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, OPTIONS" );
        response.setHeader( ACCESS_CONTROL_ALLOW_HEADERS, "X-vendor, X-vendor-auth" );

    }

}
