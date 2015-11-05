package com.spinn3r.artemis.metrics.gauge;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.util.threads.NamedThreadFactory;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.*;

/**
 * Manages BackedGaugeIndex implementations and periodically calls expire on them.
 */
public class BackedGaugeIndexManager extends BaseService {

    private static final Logger log = Logger.getLogger();

    private static final int PERIOD = 5000;

    private static final int INITIAL_DELAY = 5000;

    private BackedGaugeRegistry registry = new BackedGaugeRegistry();

    protected ThreadFactory threadFactory = new NamedThreadFactory( ExpireTask.class );

    protected ScheduledExecutorService executorService = Executors.newScheduledThreadPool( 1, threadFactory );

    @Override
    public void init() {
        advertise( BackedGaugeRegistry.class, registry );
    }

    @Override
    public void start() throws Exception {

        executorService.scheduleAtFixedRate( new ExpireTask(), INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS );

    }

    @Override
    public void stop() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination( Long.MAX_VALUE, TimeUnit.MILLISECONDS );
    }

    class ExpireTask implements Runnable {

        @Override
        public void run() {

            for( BackedGaugeIndex index : registry.indexes ) {
                try {
                    index.expire();
                } catch (Throwable t) {
                    log.error( "Unable to expire: ", t );
                }
            }

        }

    }

}


