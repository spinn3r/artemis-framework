package com.spinn3r.artemis.network.fetcher;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;

import java.util.Map;

/**
 *
 */
public class DefaultContentFetcher implements ContentFetcher {

    private final HttpRequestBuilder httpRequestBuilder;

    @Inject
    DefaultContentFetcher(HttpRequestBuilder httpRequestBuilder) {
        this.httpRequestBuilder = httpRequestBuilder;
    }

    @Override
    public String fetch(String link) throws NetworkException {
        return httpRequestBuilder.get( link ).execute().getContentWithEncoding();
    }

    @Override
    public String fetch(String link, ImmutableMap<String, String> requestHeaders) throws NetworkException {
        return httpRequestBuilder.get( link )
                 .withRequestHeaders( requestHeaders )
                 .execute()
                 .getContentWithEncoding();
    }

//    @Override
//    public String fetch(String link, ImmutableMap<String, String> requestHeaders, ImmutableMap<String, String> cookies) throws NetworkException {
//        return httpRequestBuilder.get( link )
//                 .withRequestHeaders( requestHeaders )
//                 .withCookies( cookies )
//                 .execute()
//                 .getContentWithEncoding();
//    }

}
