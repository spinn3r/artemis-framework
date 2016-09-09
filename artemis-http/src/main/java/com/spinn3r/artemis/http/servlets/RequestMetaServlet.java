package com.spinn3r.artemis.http.servlets;

import com.google.common.base.Charsets;
import com.spinn3r.log5j.Logger;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

/**
 * A servlet that prints HTTP request metadata as JSON
 */
public class RequestMetaServlet extends DefaultServlet {

    private static final Logger log = Logger.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle( request, response );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle( request, response );
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle( request, response );
    }

    protected void handle(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {

        try {

            RequestMeta requestMeta = new RequestMeta();

            requestMeta.pathInfo = req.getPathInfo();
            requestMeta.queryString = req.getQueryString();

            for( String headerName : Collections.list( req.getHeaderNames() ) ) {
                requestMeta.headers.put( headerName, req.getHeader( headerName ) );
            }

            for( String parameterName : Collections.list( req.getParameterNames() ) ) {
                requestMeta.parameters.put( parameterName, req.getParameter( parameterName ) );
            }

            if ( req.getCookies() != null) {

                for (Cookie cookie : req.getCookies()) {

                    requestMeta.cookies.add(new RequestMeta.Cookie(cookie.getName(),
                                                                   cookie.getValue(),
                                                                   cookie.getPath(),
                                                                   cookie.getDomain(),
                                                                   cookie.isHttpOnly()));
                }

            }

            resp.setContentType( "application/json" );
            resp.setCharacterEncoding( Charsets.UTF_8.name().toUpperCase() );

            try( InputStream is = req.getInputStream();
                 OutputStream out = resp.getOutputStream() ) {

                out.write( requestMeta.toJSON().getBytes( Charsets.UTF_8 ) );

            }

        } catch (RuntimeException e) {
            log.error( "Unable to handle request: ", e);
            throw e;
        }

    }

}

