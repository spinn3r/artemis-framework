package com.spinn3r.artemis.background_scanner;

import com.google.inject.Inject;
import com.spinn3r.artemis.time.Sleeper;

/**
 *
 */
public class RunnerFactory {

    private final Sleeper sleeper;

    @Inject
    RunnerFactory(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    public Runner create(BackgroundScanner backgroundScanner, BackgroundScannerOptions backgroundScannerOptions) {
        return new Runner(sleeper, backgroundScanner, backgroundScannerOptions);
    }

}
