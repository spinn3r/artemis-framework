package com.spinn3r.artemis.http;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.advertisements.Version;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.spinn3r.artemis.http.servlets.ResponseHeaders.*;

/**
 * Set headers for access control, encoding, etc.
 */
public class CrossOrigin {

    private static final String X_POWERED_BY = "X-Powered-By";
    private static final String X_SERVER = "X-server";
    private static final String X_VERSION = "X-version";

    protected final Provider<Version> versionProvider;

    @Inject
    CrossOrigin(Provider<Version> versionProvider) {
        this.versionProvider = versionProvider;
    }

    public void handleRequest(HttpServletResponse response) throws ServletException, IOException {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*" );
        response.setHeader( X_POWERED_BY, "Spinn3r" );
        response.setHeader( X_SERVER, "Spinn3r" );
        response.setHeader( X_VERSION, versionProvider.get().toString() );

        response.setCharacterEncoding("UTF-8");

    }

    public void handleOptions(HttpServletResponse response) throws ServletException, IOException {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*" );
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, OPTIONS" );
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, "X-vendor, X-vendor-auth" );

    }

}
