package com.spinn3r.artemis.metrics.decorators;

import com.codahale.metrics.Gauge;
import com.spinn3r.artemis.metrics.Counter;

/**
*
*/
public class LongGaugeDecorator implements Gauge<Long> {

    private final long value;

    public LongGaugeDecorator(long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

}
