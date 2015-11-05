package com.spinn3r.artemis.http;

import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

/**
 * Reference to a servlet that we can pass to the webserver via dependency
 * injection.
 */
public class ServletReference {

    private final String pathSpec;

    private final ServletHolder servletHolder;

    public ServletReference( String pathSpec, Servlet servlet )  {
        this( pathSpec, new ServletHolder( servlet ) );
    }

    public ServletReference( String pathSpec, Class<? extends Servlet> servletClazz )  {
        this( pathSpec, new ServletHolder( servletClazz ) );
    }

    public ServletReference(String pathSpec, ServletHolder servletHolder) {
        this.pathSpec = pathSpec;
        this.servletHolder = servletHolder;
    }

    public String getPathSpec() {
        return pathSpec;
    }

    public ServletHolder getServletHolder() {
        return servletHolder;
    }

}
