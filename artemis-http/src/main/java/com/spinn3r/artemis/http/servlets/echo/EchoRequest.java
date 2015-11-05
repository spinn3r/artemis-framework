package com.spinn3r.artemis.http.servlets.echo;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class EchoRequest {

    private String pathInfo;

    private Map<String, String[]> parameters;

    private Map<String,List<String>> headers;

    public EchoRequest(String pathInfo, Map<String, String[]> parameters, Map<String, List<String>> headers) {
        this.pathInfo = pathInfo;
        this.parameters = parameters;
        this.headers = headers;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "EchoRequest{" +
                 "pathInfo='" + pathInfo + '\'' +
                 ", parameters=" + parameters +
                 ", headers=" + headers +
                 '}';
    }

}
