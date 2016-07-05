package com.spinn3r.artemis.init.servlets;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.init.*;
import com.spinn3r.artemis.json.JSON;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * For all services in the system, dump their config as a JSON object.  This
 * way I can go to /config and see all the configs that are loaded.
 */
public class ConfigServlet extends DefaultServlet {

    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";

    private final Services services;

    private final ConfigReader configReader;

    public ConfigServlet(Services services, ConfigReader configReader) {
        this.configReader = configReader;
        this.services = services;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType( APPLICATION_JSON );
        response.setCharacterEncoding( UTF_8 );

        try ( ServletOutputStream out = response.getOutputStream() ) {
            out.write( JSON.toJSON( toMap() ).getBytes( Charsets.UTF_8 ) );
        }

    }

    private Map<String,Object> toMap() {

        Map<String, Object> result = new TreeMap<>();

        for (Service service : services) {

            Object config = configReader.readObject( service );

            if (service != null ) {

                result.put( service.getClass().getName(), config );

            }

        }

        return result;

    }

}
