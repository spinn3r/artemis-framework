package com.spinn3r.artemis.corpus.network.test;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.network.builder.CrawlingHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.DirectHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;

/**
 * Used with the Cached HTTP request builder to ALSO replace the direct HTTP
 * request builder with caching.
 */
public class CachedCrawlingNetworkService extends BaseService {

    @Override
    public void init() {
        replace( CrawlingHttpRequestBuilder.class, CachedHttpRequestBuilder.class );
    }

}
