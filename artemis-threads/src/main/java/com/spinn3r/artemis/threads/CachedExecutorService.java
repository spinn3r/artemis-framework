package com.spinn3r.artemis.threads;

import com.spinn3r.artemis.util.threads.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class CachedExecutorService {

    private final ExecutorService executorService;

    public CachedExecutorService(Class<?> threadFactoryClass) {

        NamedThreadFactory namedThreadFactory = new NamedThreadFactory( threadFactoryClass );
        this.executorService = Executors.newCachedThreadPool( namedThreadFactory );

    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void submit( Runnable runnable ) {
        executorService.submit( runnable );
    }

    public void stop() {
        ExecutorServices.shutdownAndAwaitTermination( executorService ).await();
    }

}
