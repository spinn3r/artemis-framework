package com.spinn3r.artemis.http.builder;

import com.spinn3r.artemis.http.ServletReferences;
import com.spinn3r.artemis.http.servlets.EchoServlet;
import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
public class TestServletReferencesServices extends BaseService {

    protected ParamServlet paramServlet = new ParamServlet();

    @Override
    public void start() throws Exception {

        ServletReferences servletReferences = new ServletReferences();
        servletReferences.add( "/params", paramServlet );
        servletReferences.add( "/post", new EchoServlet() );
        servletReferences.add( "/put", new EchoServlet() );

        advertise( ParamServlet.class, paramServlet );
        advertise( ServletReferences.class, servletReferences );

    }

}
