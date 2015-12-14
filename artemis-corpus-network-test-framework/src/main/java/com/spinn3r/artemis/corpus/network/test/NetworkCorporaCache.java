package com.spinn3r.artemis.corpus.network.test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
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
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class NetworkCorporaCache implements ContentFetcher {

    private static final ImmutableMap<String,String> EMPTY_MAP = ImmutableMap.copyOf( Maps.newHashMap() );

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
    public String fetch(String link, ImmutableMap<String, String> requestHeaders) throws NetworkException {
        return fetch( link, requestHeaders, EMPTY_MAP );
    }

    @Override
    public String fetch(String link, ImmutableMap<String, String> requestHeaders, ImmutableMap<String, String> cookies) throws NetworkException {
        return fetchCachedContent( HttpMethod.GET, link, requestHeaders, cookies, null, null, null ).getContent();
    }

    public CachedContent fetchCachedContent( HttpMethod httpMethod,
                                             String link,
                                             ImmutableMap<String, String> requestHeaders,
                                             ImmutableMap<String, String> cookies,
                                             String outputContent,
                                             String outputContentEncoding,
                                             String outputContentType ) throws NetworkException {

        checkNotNull( link, "link" );

        String key = computeKey( httpMethod, link, requestHeaders, cookies, outputContent, outputContentEncoding, outputContentType );

        // the key here is raw... so we can add a suffix to include the metadata
        // we want to include.. .

        try {

            if ( ! cache.contains( key ) ) {

                if ( updateMode ) {

                    HttpRequest httpRequest;

                    switch ( httpMethod ) {

                        case GET:

                            httpRequest =
                              directHttpRequestBuilder
                                .get( link )
                                .withRequestHeaders( requestHeaders )
                                .withCookies( cookies )
                                .execute();

                            break;

                        case POST:
                            httpRequest =
                              directHttpRequestBuilder
                                .post( link, outputContent, outputContentEncoding, outputContentType )
                                .withRequestHeaders( requestHeaders )
                                .withCookies( cookies )
                                .execute();
                            break;

                        default:
                            throw new NetworkException( "HTTP method not yet supported: " + httpMethod );
                    }

                    String contentWithEncoding = httpRequest.getContentWithEncoding();

                    cache.write( key, contentWithEncoding );

                    // now write the extended metadata...

                    HttpRequestMeta httpRequestMeta = httpRequest.getHttpRequestMeta();
                    HttpResponseMeta httpResponseMeta = httpRequest.getHttpResponseMeta();

                    cache.write( key + "-request-meta", JSON.toJSON( httpRequestMeta ) );
                    cache.write( key + "-response-meta", JSON.toJSON( httpResponseMeta ) );

                    return new CachedContent( key, contentWithEncoding, httpRequestMeta, httpResponseMeta );

                } else {
                    throw new IOException( String.format( "URL is not in the cache: %s (Use -D%s=true to force update)",
                                           link, UPDATE_MODE_PROPERTY_NAME ) );
                }

            }

            String contentWithEncoding = cache.read( key );
            HttpRequestMeta httpRequestMeta = requestMeta( key );
            HttpResponseMeta httpResponseMeta = responseMeta( key );

            return new CachedContent( key, contentWithEncoding, httpRequestMeta, httpResponseMeta );

        } catch (IOException e) {
            throw new NetworkException( e );
        }

    }

    private String computeKey( HttpMethod httpMethod,
                               String link,
                               ImmutableMap<String, String> requestHeaders,
                               ImmutableMap<String, String> cookies,
                               String outputContent,
                               String outputContentEncoding,
                               String outputContentType ) {

        StringBuilder data = new StringBuilder();

        if ( ! httpMethod.equals( HttpMethod.GET ) ) {
            data.append( httpMethod.toString() );
        }

        data.append( link );

        if ( requestHeaders != null && requestHeaders.size() > 0 ) {
            data.append( requestHeaders.toString() );
        }

        if ( cookies != null && cookies.size() > 0 ) {
            data.append( cookies.toString() );
        }

        if ( outputContent != null ) {
            data.append( outputContent );
            data.append( outputContentEncoding );
            data.append( outputContentType );
        }

        return Base64.encode( SHA1.encode( data.toString() ) );

    }

    private HttpResponseMeta responseMeta( String key ) throws NetworkException {
        return parseMeta( key, "-response-meta", DefaultHttpResponseMeta.class );
    }

    private HttpRequestMeta requestMeta( String key ) throws NetworkException {
        return parseMeta( key, "-request-meta", DefaultHttpRequestMeta.class );
    }

    public <T> T parseMeta( String key, String keySuffix, Class<T> clazz ) throws NetworkException {

        try {

            key = key + keySuffix;

            if ( cache.contains( key ) ) {
                String json = cache.read( key );
                return JSON.fromJSON( clazz, json );
            }

            return null;

        } catch (IOException e) {
            throw new NetworkException( e );
        }

    }

    private static Class<?> classForCaller( Caller caller ) {

        try {

            return Class.forName( caller.get() );

        } catch (ClassNotFoundException e) {
            throw new RuntimeException( e );
        }

    }

}
