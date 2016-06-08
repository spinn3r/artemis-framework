package com.spinn3r.artemis.util.misc;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ExecutorServices {

    private static final Logger log = Logger.getLogger( ExecutorServices.class );

    /**
     *
     */
    public static ShutdownAwaiter shutdownAndAwaitTermination( ExecutorService executorService ) {
        return new ShutdownAwaiter( executorService, false );
    }

    public static ShutdownAwaiter shutdownNowAndAwaitTermination( ExecutorService executorService ) {
        return new ShutdownAwaiter( executorService, true );
    }


    public static class ShutdownAwaiter {

        private long softTimeout = 15;

        private TimeUnit softTimeUnit = TimeUnit.SECONDS;

        private long hardTimeout = 15;

        private TimeUnit hardTimeUnit = TimeUnit.SECONDS;

        private final ExecutorService executorService;

        private final boolean now;

        public ShutdownAwaiter(ExecutorService executorService, boolean now) {
            this.executorService = executorService;
            this.now = now;
        }

        public ShutdownAwaiter setSoftTimeout( long timeout, TimeUnit timeUnit ) {
            this.softTimeout = timeout;
            this.softTimeUnit = timeUnit;
            return this;
        }

        public ShutdownAwaiter setHardTimeout( long timeout, TimeUnit timeUnit ) {
            this.hardTimeout = timeout;
            this.hardTimeUnit = timeUnit;
            return this;
        }

        public void await() {

            if ( executorService == null ) {
                // we're done... nothing to do.
                return;
            }

            if (now) {
                // disable new tasks from being submitted and interrupt existing tasks.
                executorService.shutdownNow();
            } else {
                // disable new tasks from being submitted
                executorService.shutdown();
            }

            try {

                // Wait a while for existing tasks to terminate
                if ( ! executorService.awaitTermination( softTimeout, softTimeUnit ) ) {

                    // Cancel currently executing tasks
                    executorService.shutdownNow();

                    // Wait a while for tasks to respond to being cancelled
                    if ( ! executorService.awaitTermination( hardTimeout, hardTimeUnit ) ) {
                        log.error( "Pool did not terminate." );
                    }

                }

            } catch (InterruptedException ie) {

                // (Re-)Cancel if current thread also interrupted
                executorService.shutdownNow();

                // Preserve interrupt status
                Thread.currentThread().interrupt();

            }

        }

    }

}
