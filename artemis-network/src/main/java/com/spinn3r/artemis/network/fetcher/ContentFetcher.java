package com.spinn3r.artemis.network.fetcher;

import com.spinn3r.artemis.network.NetworkException;

import java.util.Map;

/**
 * Simple interface for fetching content form the network but without the
 * complexity of our regular HTTP support.  Our regular HTTP request builder
 * supports a LOT of options and mocks for this are more difficult to build.
 */
public interface ContentFetcher {

    String fetch( String link ) throws NetworkException;

    String fetch( String link, Map<String,String> requestHeaders ) throws NetworkException;

    String fetch( String link, Map<String,String> requestHeaders, Map<String,String> cookies ) throws NetworkException;

}
