package com.spinn3r.artemis.init.services;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.util.threads.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * A background service which holds a runnable and continues running it at a
 * fixed rate.  We handle all details like using a unique thread name, the
 * interval, etc.
 */
public abstract class BackgroundScheduledService extends BaseService {

    protected ThreadFactory threadFactory = new NamedThreadFactory( getClass() );

    protected ScheduledExecutorService executorService = Executors.newScheduledThreadPool( 1, threadFactory );

    private Runnable runnable;

    private long initialDelayMillis;

    private long periodMillis;

    public void init( Runnable runnable, long initialDelayMillis, long periodMillis ) {
        this.runnable = runnable;
        this.initialDelayMillis = initialDelayMillis;
        this.periodMillis = periodMillis;
    }

    @Override
    public void start() throws Exception {

        if ( executorService == null )
            throw new NullPointerException( "executorService" );

        if ( runnable == null )
            throw new NullPointerException( "runnable" );

        executorService.scheduleAtFixedRate( runnable, initialDelayMillis, periodMillis, TimeUnit.MILLISECONDS );

    }

    @Override
    public void stop() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination( Long.MAX_VALUE, TimeUnit.MILLISECONDS );
    }

}
