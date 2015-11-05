package com.spinn3r.artemis.metrics;

import static java.lang.Double.*;

/**
 * Returns the percentage from 0-100 between two values.
 */
public class Percentage {

    private final Counter counter;
    private final Value value;

    public Percentage(Counter counter, Value value) {
        this.counter = counter;
        this.value = value;
    }

    public double get() {

        double numerator = counter.get();
        double denominator = value.get();

        if (isNaN(denominator) || isInfinite(denominator) || denominator == 0) {
            return Double.NaN;
        }

        return 100 * (numerator / denominator);

    }

}
