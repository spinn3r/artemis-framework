package com.spinn3r.artemis.http.builder;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.http.parameters.Parameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Records params from the tests...
 */
public class ParamServlet extends HttpServlet {

    protected List<Parameters> requestParameters = Lists.newArrayList();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestParameters.add( Parameters.fromRequest( req.getParameterMap() ) );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestParameters.add( Parameters.fromRequest( req.getParameterMap() ) );
    }

}
