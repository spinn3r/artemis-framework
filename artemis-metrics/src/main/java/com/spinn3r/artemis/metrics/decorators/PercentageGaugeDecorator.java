package com.spinn3r.artemis.metrics.decorators;

import com.codahale.metrics.Gauge;
import com.spinn3r.artemis.metrics.Counter;
import com.spinn3r.artemis.metrics.Percentage;

/**
*
*/
public class PercentageGaugeDecorator implements Gauge<Double> {

    private Percentage percentage;

    public PercentageGaugeDecorator(Percentage percentage) {
        this.percentage = percentage;
    }

    @Override
    public Double getValue() {
        return percentage.get();
    }

}
