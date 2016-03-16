package com.spinn3r.artemis.network.builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.Cookie;

/**
 *
 */
public class DefaultHttpContentResponseMeta extends DefaultHttpResponseMeta implements HttpContentResponseMeta  {

    private final String contentWithEncoding;

    public DefaultHttpContentResponseMeta( String resource,
                                           HttpResponseMeta httpResponseMeta,
                                           String contentWithEncoding) {

        super( resource, httpResponseMeta );
        this.contentWithEncoding = contentWithEncoding;

    }

    public DefaultHttpContentResponseMeta( @JsonProperty("resource") String resource,
                                           @JsonProperty("resourceFromRedirect") String resourceFromRedirect,
                                           @JsonProperty("responseCode") int responseCode,
                                           @JsonProperty("responseHeadersMap") ImmutableMap<String,ImmutableList<String>> responseHeaderMap,
                                           @JsonProperty("cookies") ImmutableMap<String, Cookie> cookies,
                                           @JsonProperty("contentWithEncoding") String contentWithEncoding) {
        super( resource, resourceFromRedirect, responseCode, responseHeaderMap, cookies );
        this.contentWithEncoding = contentWithEncoding;
    }

    @Override
    public String getContentWithEncoding() {
        return contentWithEncoding;
    }

    @Override
    public String toString() {
        return "DefaultHttpContentResponseMeta{" +
                 "contentWithEncoding='" + contentWithEncoding + '\'' +
                 "}, " + super.toString();
    }

}


