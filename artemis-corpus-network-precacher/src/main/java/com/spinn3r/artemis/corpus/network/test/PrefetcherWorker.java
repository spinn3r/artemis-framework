package com.spinn3r.artemis.corpus.network.test;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.parallel.Worker;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.BlockingQueue;

/**
 *
 */
public class PrefetcherWorker implements Worker {

    private static final Logger log = Logger.getLogger();

    private final CachedHttpRequestBuilder cachedHttpRequestBuilder;

    private final BlockingQueue<String> workQueue;

    PrefetcherWorker(CachedHttpRequestBuilder cachedHttpRequestBuilder, BlockingQueue<String> workQueue) {
        this.cachedHttpRequestBuilder = cachedHttpRequestBuilder;
        this.workQueue = workQueue;
    }

    @Override
    public void work() throws Exception {

        while(true) {

            String link = workQueue.poll();

            if (link == null)
                break;

            work0(link);

        }

    }

    private void work0(String link) {

        try {

            CachedHttpRequestMethod cachedHttpRequestMethod = cachedHttpRequestBuilder.get(link);

            if(cachedHttpRequestMethod.isCached()) {
                // already cached so nothing left to do.
                return;
            }

            cachedHttpRequestMethod.execute().connect();

        } catch (NetworkException e) {
            log.error("Could not precache link: ", e, link);
        }

    }

}
