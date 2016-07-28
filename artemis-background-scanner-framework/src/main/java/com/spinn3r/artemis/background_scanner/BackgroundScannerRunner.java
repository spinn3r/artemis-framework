package com.spinn3r.artemis.background_scanner;

import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.threads.ExecutorServices;
import com.spinn3r.artemis.threads.Shutdownable;
import com.spinn3r.artemis.threads.ShutdownableIndex;
import com.spinn3r.artemis.threads.Shutdownables;
import com.spinn3r.artemis.util.threads.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.spinn3r.artemis.threads.ExecutorServices.shutdownAndAwaitTermination;

/**
 * Handles starting all the background services and hiding all the complexities
 * around the ExecutorService and stopping the scanner, even if the scanner blocks.
 */
public class BackgroundScannerRunner implements Shutdownable {

    private final RunnerFactory runnerFactory;

    private final BackgroundScanner backgroundScanner;

    protected final Runner runner;

    private final ExecutorService executorService;

    BackgroundScannerRunner(RunnerFactory runnerFactory, BackgroundScanner backgroundScanner, BackgroundScannerOptions backgroundScannerOptions) {
        this.backgroundScanner = backgroundScanner;
        this.runnerFactory = runnerFactory;
        NamedThreadFactory threadFactory = new NamedThreadFactory(backgroundScanner.getClass());

        // technically we could use a ScheduledExecutorService but the problem
        // with this is that you the times us a fixed system clock so you can't
        // mock out sleeping between iterations easily.
        this.executorService = Executors.newSingleThreadExecutor(threadFactory);

        this.runner = runnerFactory.create(backgroundScanner, backgroundScannerOptions);

        executorService.submit(runner);

    }

    @Override
    public void shutdown() throws Exception {

        Shutdownables.shutdown(new ShutdownableIndex(BackgroundScannerRunner.class,
                                                     ImmutableMap.of("runner", this.runner,
                                                                     "executorService", shutdownAndAwaitTermination(executorService))));

    }

}
