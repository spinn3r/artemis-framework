package com.spinn3r.artemis.metrics.gauge;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class BackedGaugeRegistry {

    protected List<BackedGaugeIndex> indexes = new CopyOnWriteArrayList<>();

    public void register( BackedGaugeIndex backedGaugeIndex ) {
        indexes.add( backedGaugeIndex );
    }

}
