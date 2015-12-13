package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class DefaultHttpContentResponseMeta extends DefaultHttpResponseMeta implements HttpContentResponseMeta  {

    private final String contentWithEncoding;

    public DefaultHttpContentResponseMeta(String resource, String resourceFromRedirect, int responseCode, ImmutableMap<String,ImmutableList<String>> responseHeaderMap, String contentWithEncoding) {
        super( resource, resourceFromRedirect, responseCode, responseHeaderMap );
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


