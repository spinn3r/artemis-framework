package com.spinn3r.artemis.network.builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.Cookie;
import com.spinn3r.artemis.network.Cookies;

/**
 * Metadata about an HTTP response including the HTTP status code, etc.  This does
 * NOT include any methods that can operate on the HTTP response directly as
 * this is an immutable object.
 */
public class DefaultHttpResponseMeta implements HttpResponseMeta {

    private final String resource;

    private final String resourceFromRedirect;

    private final int responseCode;

    private final ImmutableMap<String,ImmutableList<String>> responseHeadersMap;

    private final ImmutableMap<String,Cookie> cookies;

    public DefaultHttpResponseMeta( @JsonProperty("resource") String resource,
                                    @JsonProperty("resourceFromRedirect") String resourceFromRedirect,
                                    @JsonProperty("responseCode") int responseCode,
                                    @JsonProperty("responseHeadersMap") ImmutableMap<String, ImmutableList<String>> responseHeadersMap,
                                    @JsonProperty("cookies") ImmutableMap<String, Cookie> cookies) {
        this.resource = resource;
        this.resourceFromRedirect = resourceFromRedirect;
        this.responseCode = responseCode;
        this.responseHeadersMap = responseHeadersMap;
        this.cookies = cookies;
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
    public ImmutableMap<String, ImmutableList<String>> getResponseHeadersMap() {
        return responseHeadersMap;
    }

    @Override
    public ImmutableMap<String,Cookie> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return "DefaultHttpResponseMeta{" +
                 "resource='" + resource + '\'' +
                 ", resourceFromRedirect='" + resourceFromRedirect + '\'' +
                 ", responseCode=" + responseCode +
                 ", responseHeadersMap=" + responseHeadersMap +
                 ", cookies=" + cookies +
                 '}';
    }

}
