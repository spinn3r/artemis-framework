package com.spinn3r.artemis.http.servlets;

import com.google.common.collect.Maps;
import com.spinn3r.artemis.json.JSON;

import java.util.Map;

/**
 *
 */
public class RequestMeta {

    protected String pathInfo;

    protected String queryString;

    protected Map<String, String> headers = Maps.newLinkedHashMap();

    protected Map<String, String> parameters = Maps.newLinkedHashMap();

    public String getPathInfo() {
        return pathInfo;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String toJSON() {
        return JSON.toJSON( this );
    }

    public static RequestMeta fromJSON(String json) {
        return JSON.fromJSON( RequestMeta.class, json );
    }

}
