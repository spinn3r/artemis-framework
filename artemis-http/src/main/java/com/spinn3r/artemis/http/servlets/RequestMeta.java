package com.spinn3r.artemis.http.servlets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.json.JSON;
import com.spinn3r.artemis.network.cookies.Cookie;

import java.util.List;
import java.util.Map;

/**
 * This is actually
 */
public class RequestMeta {

    protected String pathInfo;

    protected String queryString;

    protected Map<String, String> headers = Maps.newLinkedHashMap();

    protected Map<String, String> parameters = Maps.newLinkedHashMap();

    protected List<Cookie> cookies = Lists.newArrayList();

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

    public List<Cookie> getCookies() {
        return cookies;
    }

    public String toJSON() {
        return JSON.toJSON( this );
    }

    public static RequestMeta fromJSON(String json) {
        return JSON.fromJSON( RequestMeta.class, json );
    }

}
