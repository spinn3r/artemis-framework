package com.spinn3r.artemis.http.servlets;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.json.JSON;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

/**
 * A servlet that prints HTTP request metadata as JSON
 */
public class RequestMetaServlet extends DefaultServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle( request, response );
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle( request, response );
    }

    protected void handle(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {

        RequestMeta requestMeta = new RequestMeta();

        requestMeta.pathInfo = req.getPathInfo();
        requestMeta.queryString = req.getQueryString();

        for( String headerName : Collections.list( req.getHeaderNames() ) ) {
            requestMeta.headers.put( headerName, req.getHeader( headerName ) );
        }

        for( String parameterName : Collections.list( req.getParameterNames() ) ) {
            requestMeta.headers.put( parameterName, req.getParameter( parameterName ) );
        }

        resp.setContentType( "application/json" );
        resp.setCharacterEncoding( Charsets.UTF_8.name().toUpperCase() );

        try( InputStream is = req.getInputStream();
             OutputStream out = resp.getOutputStream() ) {

            out.write( requestMeta.toJSON().getBytes( Charsets.UTF_8 ) );

        }

    }

    static class RequestMeta {

        protected String pathInfo;

        protected String queryString;

        protected Map<String,String> headers = Maps.newLinkedHashMap();

        protected Map<String,String> parameters = Maps.newLinkedHashMap();

        public String toJSON() {
            return JSON.toJSON(this);
        }

    }

}

