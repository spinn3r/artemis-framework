package com.spinn3r.artemis.http.servlets.echo;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.http.servlets.BaseServlet;
import com.spinn3r.artemis.json.JSON;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet that takes the current GET request and writes it back out in JSON form
 * for debug purposes.
 * @Deprecated use RequestMetaServlet
 */
@Deprecated
public class GetEchoServlet extends DefaultServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try( OutputStream out = response.getOutputStream() ) {

            EchoRequest echo = new EchoRequest( request.getPathInfo(), request.getParameterMap(), headers( request ) );

            out.write( JSON.toJSON( echo ).getBytes( Charsets.UTF_8 ) );

        }

    }

    private Map<String,List<String>> headers( HttpServletRequest httpServletRequest ) {

        Map<String,List<String>> result = new HashMap<>();

        for (String headerName : Collections.list( httpServletRequest.getHeaderNames() )) {
            result.put( headerName, Collections.list( httpServletRequest.getHeaders( headerName ) ) );
        }

        return result;

    }

}


