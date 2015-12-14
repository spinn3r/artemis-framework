package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableMap;

/**
 *
 */
public interface HttpRequestMeta {

    String getResource();

    ImmutableMap<String, String> getRequestHeadersMap();

    ImmutableMap<String, String> getCookies();

    String getOutputContent();

    String getOutputContentEncoding();

    String getOutputContentType();

}
