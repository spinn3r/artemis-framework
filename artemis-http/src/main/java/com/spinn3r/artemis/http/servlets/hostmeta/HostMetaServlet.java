package com.spinn3r.artemis.http.servlets.hostmeta;

import com.google.common.base.Charsets;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet to provide an overview of the current host.
 */
public class HostMetaServlet extends DefaultServlet {

    private HostMeta hostMeta;

    public HostMetaServlet(HostMeta hostMeta) {
        this.hostMeta = hostMeta;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try( OutputStream out = response.getOutputStream() ) {

            out.write( hostMeta.toJSON().getBytes( Charsets.UTF_8 ) );

        }

    }

}
