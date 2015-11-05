package com.spinn3r.artemis.metrics.decorators;

import com.codahale.metrics.Gauge;
import com.spinn3r.artemis.metrics.Value;

/**
*
*/
public class ValueGaugeDecorator implements Gauge<Long> {

    private Value value;

    public ValueGaugeDecorator(Value value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value.get();
    }

}
