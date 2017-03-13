package com.spinn3r.artemis.corpus.network.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.spinn3r.artemis.parallel.ParallelException;
import com.spinn3r.artemis.parallel.ParallelExecutor;
import com.spinn3r.artemis.parallel.Worker;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class Prefetcher {

    private final PrefetcherWorkerFactory prefetcherWorkerFactory;

    @Inject
    Prefetcher(PrefetcherWorkerFactory prefetcherWorkerFactory) {
        this.prefetcherWorkerFactory = prefetcherWorkerFactory;
    }

    /**
     * Prefetch the given links and block until complete.
     */
    public void prefetch(ImmutableList<String> links, int concurrency) throws ParallelException {

        LinkedBlockingQueue<String> workQueue = new LinkedBlockingQueue<>();
        workQueue.addAll(links);

        ParallelExecutor parallelExecutor = new ParallelExecutor(concurrency);

        List<Worker> workers = Lists.newArrayList();

        for (int i = 0; i < concurrency; i++) {
            PrefetcherWorker prefetcherWorker = prefetcherWorkerFactory.create(workQueue);
            workers.add(prefetcherWorker);
        }

        parallelExecutor.execute(ImmutableList.copyOf(workers));

    }

}
