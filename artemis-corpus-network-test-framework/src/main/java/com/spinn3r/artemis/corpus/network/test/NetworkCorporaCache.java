package com.spinn3r.artemis.corpus.network.test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.spinn3r.artemis.corpus.test.CorporaCache;
import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.json.JSON;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.fetcher.ContentFetcher;
import com.spinn3r.artemis.util.crypto.SHA1;
import com.spinn3r.artemis.util.misc.Base64;
import com.spinn3r.artemis.util.misc.Stack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class NetworkCorporaCache implements ContentFetcher {

    private static final Map<String,String> EMPTY_MAP = new HashMap<>();

    private static final String UPDATE_MODE_PROPERTY_NAME = "network-corpora-cache.update_mode";

    public static boolean DEFAULT_UPDATE_MODE = "true".equals( System.getProperty( UPDATE_MODE_PROPERTY_NAME ) );

    private final DirectHttpRequestBuilder directHttpRequestBuilder;

    private CorporaCache cache;

    private boolean updateMode = DEFAULT_UPDATE_MODE;

    @Inject
    NetworkCorporaCache(DirectHttpRequestBuilder directHttpRequestBuilder, Caller caller) {
        this( directHttpRequestBuilder, classForCaller( caller ) );
    }

    NetworkCorporaCache(DirectHttpRequestBuilder directHttpRequestBuilder, Class<?> callerClazz) {
        this.directHttpRequestBuilder = directHttpRequestBuilder;
        this.cache = new CorporaCache( callerClazz, "/network-corpora" );
    }

    @Override
    public String fetch( String link ) throws NetworkException {

        return fetch( link, EMPTY_MAP, EMPTY_MAP );

    }

    @Override
    public String fetch(String link, Map<String, String> requestHeaders) throws NetworkException {
        return fetch( link, requestHeaders, EMPTY_MAP );
    }

    @Override
    public String fetch(String link, Map<String, String> requestHeaders, Map<String, String> cookies) throws NetworkException {

        checkNotNull( link, "link" );

        String key = Base64.encode( SHA1.encode( link ) );

        // the key here is raw... so we can add a suffix to include the metadata
        // we want to include.. .

        try {

            if ( ! cache.contains( key ) ) {

                if ( updateMode ) {

                    HttpRequest httpRequest =
                      directHttpRequestBuilder
                        .get( link )
                        .withRequestHeaders( requestHeaders )
                        .withCookies( cookies )
                        .execute();

                    String contentWithEncoding =
                      httpRequest
                        .getContentWithEncoding();

                    cache.write( key, contentWithEncoding );

                    // now write the extended metadata...

                    HttpResponseMeta httpResponseMeta = httpRequest.getHttpResponseMeta();

                    cache.write( key + "-meta", JSON.toJSON(httpResponseMeta) );

                    return contentWithEncoding;

                } else {
                    throw new IOException( String.format( "URL is not in the cache: %s (Use -D%s=true to force update)",
                                           link, UPDATE_MODE_PROPERTY_NAME ) );
                }

            }

            return cache.read( key );

        } catch (IOException e) {
            throw new NetworkException( e );
        }

    }

    /**
     * Get just the metadata for a link (if it's present in the cache) or null
     * if it's absent.
     */
    public HttpResponseMeta meta( String link ) throws IOException {

        String key = Base64.encode( SHA1.encode( link ) ) + "-meta";

        if ( cache.contains( key ) ) {
            String json = cache.read( key );
            return JSON.fromJSON( DefaultHttpResponseMeta.class, json );
        }

        return null;

    }

    private static Class<?> classForCaller( Caller caller ) {

        try {

            return Class.forName( caller.get() );

        } catch (ClassNotFoundException e) {
            throw new RuntimeException( e );
        }

    }

}
