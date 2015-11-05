package com.spinn3r.artemis.metrics.decorators;

import com.codahale.metrics.Gauge;
import com.spinn3r.artemis.metrics.Counter;

/**
*
*/
public class CounterGaugeDecorator implements Gauge<Long> {

    private Counter counter;

    public CounterGaugeDecorator(Counter counter) {
        this.counter = counter;
    }

    @Override
    public Long getValue() {
        return counter.get();
    }

}
