package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.Cookie;
import com.spinn3r.artemis.network.Cookies;

import java.util.List;
import java.util.Map;

/**
 * Metadata about an HTTP response including the HTTP status code, etc.  This does
 * NOT include any methods that can operate on the HTTP response directly as
 * this is an immutable object.
 */
public class DefaultHttpResponseMeta implements HttpResponseMeta {

    private final String resource;

    private final String resourceFromRedirect;

    private final int responseCode;

    private final ImmutableMap<String,ImmutableList<String>> responseHeaderMap;

    public DefaultHttpResponseMeta(String resource, String resourceFromRedirect, int responseCode, ImmutableMap<String,ImmutableList<String>> responseHeaderMap) {
        this.resource = resource;
        this.resourceFromRedirect = resourceFromRedirect;
        this.responseCode = responseCode;
        this.responseHeaderMap = responseHeaderMap;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public String getResourceFromRedirect() {
        return resourceFromRedirect;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public ImmutableMap<String, ImmutableList<String>> getResponseHeaderMap() {
        return responseHeaderMap;
    }

    @Override
    public ImmutableMap<String,Cookie> getCookies() {
        return Cookies.fromResponseHeadersMap( getResponseHeaderMap() );
    }

    @Override
    public String toString() {
        return "DefaultHttpResponseMeta{" +
                 "resource='" + resource + '\'' +
                 ", resourceFromRedirect='" + resourceFromRedirect + '\'' +
                 ", responseCode=" + responseCode +
                 ", responseHeaderMap=" + responseHeaderMap +
                 '}';
    }

}
