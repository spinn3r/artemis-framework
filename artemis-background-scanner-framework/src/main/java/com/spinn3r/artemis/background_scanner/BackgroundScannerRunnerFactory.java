package com.spinn3r.artemis.background_scanner;

import com.google.inject.Inject;

/**
 *
 */
public class BackgroundScannerRunnerFactory {

    private final RunnerFactory runnerFactory;

    @Inject
    BackgroundScannerRunnerFactory(RunnerFactory runnerFactory) {
        this.runnerFactory = runnerFactory;
    }

    public BackgroundScannerRunner create(BackgroundScanner backgroundScanner, BackgroundScannerOptions backgroundScannerOptions) {
        return new BackgroundScannerRunner(runnerFactory, backgroundScanner, backgroundScannerOptions);
    }

}
