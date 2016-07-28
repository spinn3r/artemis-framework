package com.spinn3r.artemis.background_scanner;

import com.spinn3r.artemis.threads.Shutdownable;
import com.spinn3r.artemis.time.Sleeper;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
class Runner implements Runnable, Shutdownable {

    private static final Logger log = Logger.getLogger();

    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    private final Sleeper sleeper;

    private final BackgroundScanner backgroundScanner;

    private final BackgroundScannerOptions backgroundScannerOptions;

    public Runner(Sleeper sleeper, BackgroundScanner backgroundScanner, BackgroundScannerOptions backgroundScannerOptions) {
        this.sleeper = sleeper;
        this.backgroundScanner = backgroundScanner;
        this.backgroundScannerOptions = backgroundScannerOptions;
    }

    @Override
    public void run() {

        while( true ) {

            try {

                if (shutdown.get()) {
                    log.info("Background scanner shutting down: %s", backgroundScanner.getClass());
                    break;
                }

                runOnce();

            } catch ( Throwable t ) {
                log.error( "Caught exception while performing scan: ", t );
            } finally {
                sleepOnce();
            }

        }

    }

    @Override
    public void shutdown() throws Exception {
        this.shutdown.set(true);
    }

    private void sleepOnce() {

        int sleepInterval = backgroundScannerOptions.getScanIntervalMillis();
        log.info("Sleeping until next polling interval: %s ms", sleepInterval);
        sleeper.sleepUninterruptibly(sleepInterval, TimeUnit.MILLISECONDS);

    }

    private void runOnce() throws BackgroundScannerException {

        backgroundScanner.scan();

    }

}
