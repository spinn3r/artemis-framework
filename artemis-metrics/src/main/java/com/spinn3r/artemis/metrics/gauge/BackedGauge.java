package com.spinn3r.artemis.metrics.gauge;

import com.codahale.metrics.Gauge;

/**
 * Provides a lookup mechanism to read the values we have in memory.
 */
class BackedGauge implements Gauge<Long> {

    private BackedGaugeIndex backedGaugeIndex;

    private String key;

    public BackedGauge(BackedGaugeIndex backedGaugeIndex, String key) {
        this.backedGaugeIndex = backedGaugeIndex;
        this.key = key;
    }

    @Override
    public Long getValue() {

        BackedGaugeIndex.Entry entry = backedGaugeIndex.get( key );

        if ( entry == null ) {
            // we used to throw an exception here but I think doing so breaks
            // metrics and they aren't reported correctly after this.  Returning
            // zero is probably correct though.
            return 0L;
        }

        return entry.getValue();

    }

    @Override
    public String toString() {
        return "" + getValue();
    }

}


