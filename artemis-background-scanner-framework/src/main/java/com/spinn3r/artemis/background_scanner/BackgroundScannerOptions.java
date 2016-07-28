package com.spinn3r.artemis.background_scanner;

/**
 *
 */
public class BackgroundScannerOptions {

    protected int scanIntervalMillis = 60 * 1000;

    public int getScanIntervalMillis() {
        return scanIntervalMillis;
    }

    @Override
    public String toString() {
        return "BackgroundScannerConfig{" +
                 "scanIntervalMillis=" + scanIntervalMillis +
                 '}';
    }

    public static class Builder {

        BackgroundScannerOptions backgroundScannerOptions = new BackgroundScannerOptions();

        public Builder setScanIntervalMillis(int scanIntervalMillis) {
            backgroundScannerOptions.scanIntervalMillis = scanIntervalMillis;
            return this;
        }

        public BackgroundScannerOptions build() {
            return backgroundScannerOptions;
        }

    }

}
