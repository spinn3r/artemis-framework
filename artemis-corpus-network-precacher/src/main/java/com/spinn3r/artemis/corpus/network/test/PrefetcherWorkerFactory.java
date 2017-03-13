package com.spinn3r.artemis.corpus.network.test;

import com.google.inject.Inject;

import java.util.concurrent.BlockingQueue;

/**
 *
 */
public class PrefetcherWorkerFactory {

    private final CachedHttpRequestBuilder cachedHttpRequestBuilder;

    @Inject
    PrefetcherWorkerFactory(CachedHttpRequestBuilder cachedHttpRequestBuilder) {
        this.cachedHttpRequestBuilder = cachedHttpRequestBuilder;
    }

    public PrefetcherWorker create(BlockingQueue<String> workQueue) {
        return new PrefetcherWorker(cachedHttpRequestBuilder, workQueue);
    }

}
