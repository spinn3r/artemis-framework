package com.spinn3r.artemis.http.servlets;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.http.ServletReference;
import com.spinn3r.artemis.http.ServletReferences;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servlet which keeps track of registered servlets and builds a web index in
 * HTML for us.
 */
public class IndexServlet extends DefaultServlet {

    private ServletReferences servletReferences;

    private String hostname;

    private String role;

    public IndexServlet( ServletReferences servletReferences, String hostname, String role) {
        this.servletReferences = servletReferences;
        this.hostname = hostname;
        this.role = role;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        IndexWriter indexWriter = new IndexWriter( filtered( servletReferences ), hostname, role );

        try( ServletOutputStream out = response.getOutputStream() ) {
            out.write( indexWriter.toHTML().getBytes( Charsets.UTF_8 ) );
        }

    }

    private static Map<String,ServletHolder> filtered( ServletReferences servletReferences ) {

        Map<String,ServletHolder> result = new LinkedHashMap<>();

        for (ServletReference servletReference : servletReferences) {

            if ( servletReference.getServletHolder().getClassName().equals( IndexServlet.class.getName() ) ) {
                continue;
            }

            result.put( servletReference.getPathSpec(), servletReference.getServletHolder() );

        }

        return result;

    }

}
