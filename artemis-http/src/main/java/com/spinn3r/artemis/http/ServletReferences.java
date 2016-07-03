package com.spinn3r.artemis.http;

import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class ServletReferences extends CopyOnWriteArrayList<ServletReference> {

    private ServletReferences(ServletReference... servletReferences) {
        super(servletReferences);
    }

    public void add( String pathSpec, Servlet servlet ) {
        add( new ServletReference( pathSpec, servlet ) );
    }

    public void add( String pathSpec, Class<? extends Servlet> servlet ) {
        add( new ServletReference( pathSpec, servlet ) );
    }

    public void add( String pathSpec, ServletHolder servletHolder ) {
        add( new ServletReference( pathSpec, servletHolder ) );
    }

    public static ServletReferences of(ServletReference... servletReferences) {
        return new ServletReferences(servletReferences);
    }

}
