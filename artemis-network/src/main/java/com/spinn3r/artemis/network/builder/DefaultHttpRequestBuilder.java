package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.builder.listener.RequestListeners;
import com.spinn3r.artemis.network.cookies.jar.CookieJarManager;
import com.spinn3r.artemis.network.events.NetworkEventListener;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Builder interface preferred over using the ResourceRequestFactory.
 */
public class DefaultHttpRequestBuilder extends BaseHttpRequestBuilder implements HttpRequestBuilder, DirectHttpRequestBuilder, CrawlingHttpRequestBuilder {

    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";
    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final String HEAD_METHOD = "HEAD";
    private static final String DELETE_METHOD = "DELETE";
    private static final String TRACE_METHOD = "TRACE";

    protected final HttpResponseValidators httpResponseValidators;

    protected final Provider<CookieJarManager> cookieJarManagerProvider;

    protected NetworkEventListener listener = null;

    protected RequestListeners requestListeners;

    private int defaultMaxContentLength = URLResourceRequest.MAX_CONTENT_LENGTH;

    private long defaultReadTimeout = ResourceRequestFactory.DEFAULT_READ_TIMEOUT;

    private long defaultConnectTimeout = ResourceRequestFactory.DEFAULT_CONNECT_TIMEOUT;

    @Inject
    DefaultHttpRequestBuilder(HttpResponseValidators httpResponseValidators, Provider<CookieJarManager> cookieJarManagerProvider) {
        this.httpResponseValidators = httpResponseValidators;
        this.cookieJarManagerProvider = cookieJarManagerProvider;
    }

    public HttpRequestBuilder withRequestListeners( RequestListeners requestListeners ) {
        this.requestListeners = requestListeners;
        return this;
    }

    public HttpRequestBuilder withDefaultMaxContentLength( int defaultMaxContentLength ) {
        this.defaultMaxContentLength = defaultMaxContentLength;
        return this;
    }

    public HttpRequestBuilder withDefaultReadTimeout( int defaultReadTimeout ) {
        this.defaultReadTimeout = defaultReadTimeout;
        return this;
    }

    public HttpRequestBuilder withDefaultConnectTimeout( int defaultConnectTimeout ) {
        this.defaultConnectTimeout = defaultConnectTimeout;
        return this;
    }

    @Override
    public HttpRequestMethod get(String resource) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, GET_METHOD ) );
    }

    @Override
    public HttpRequestMethod get(String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, GET_METHOD, outputContent, outputContentEncoding, outputContentType ) );
    }

    @Override
    public HttpRequestMethod post(String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, POST_METHOD, outputContent, outputContentEncoding, outputContentType ) );
    }

    @Override
    public HttpRequestMethod put(String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, PUT_METHOD, outputContent, outputContentEncoding, outputContentType ) );
    }

    @Override
    public HttpRequestMethod options(String resource) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, OPTIONS_METHOD ) );
    }

    @Override
    public HttpRequestMethod head(String resource) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, HEAD_METHOD ) );
    }

    @Override
    public HttpRequestMethod delete(String resource) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, DELETE_METHOD ) );
    }

    @Override
    public HttpRequestMethod trace(String resource) throws NetworkException {
        return configure( resource, new DefaultHttpRequestMethod( this, resource, TRACE_METHOD ) );
    }

    public static CookieManager C_HANDLER = new CookieManager(new ThreadLocalCookieStore(), null);

    {
        CookieHandler.setDefault(C_HANDLER);
    }

    private DefaultHttpRequestMethod configure( String resource, DefaultHttpRequestMethod defaultHttpRequestMethod ) {
        defaultHttpRequestMethod.withMaxContentLength( defaultMaxContentLength );
        defaultHttpRequestMethod.withReadTimeout( defaultReadTimeout );
        defaultHttpRequestMethod.withConnectTimeout( defaultConnectTimeout );

//        try {
//            Map<String, String> latestCookies = getCookies(resource);
//
//            //TODO: Let cookies get serialized by Java
//            defaultHttpRequestMethod.withCookies(latestCookies);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return defaultHttpRequestMethod;

    }

    public static Map<String, String> getCookies(String resource) throws IOException {

        Map<String, List<String>> allCookies = C_HANDLER.get(URI.create(resource), new HashMap<>());

        return allCookies
                .entrySet()
                .stream()
                .filter(stringListEntry -> !stringListEntry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<String> list = entry.getValue();
                            return list.get(list.size() - 1);
                        }
                ));
    }

//    public static void putCookies1(String resource, Map<String, String> cookies) {
//
//        putCookies(resource, cookies.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> Collections.singletonList(entry.getValue()))));
//
//    }
//
//    public static void putCookies(String resource, Map<String, List<String>> cookies) {
//
//
//
//        cookies
//                .entrySet()
//                .stream()
//                .filter(stringListEntry -> !stringListEntry.getValue().isEmpty())
//
//                .forEach(stringListEntry -> {
//                    C_HANDLER.getCookieStore().add()
//                });
////                .collect(Collectors.toMap(
////                        Map.Entry::getKey,
////                        entry -> {
////                            List<String> list = entry.getValue();
////                            return list.get(list.size() - 1);
////                        }
////                ));
//    }

}
