package com.spinn3r.artemis.metrics.gauge;

import com.codahale.metrics.Gauge;
import com.google.common.collect.Sets;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A gauge that looks at all current gauges, and builds a denormalized value
 * using SUM.  In the future we should support functions other than SUM because
 * it doesn't always make sense to use SUM
 */
public class DenormalizedBackedGauge implements Gauge<Long> {

    private BackedGaugeIndex backedGaugeIndex;

    private Tag tag;

    public DenormalizedBackedGauge(BackedGaugeIndex backedGaugeIndex, Tag tag) {
        this.backedGaugeIndex = backedGaugeIndex;
        this.tag = tag;
    }

    @Override
    public Long getValue() {

        Collection<BackedGaugeIndex.Entry> entries = backedGaugeIndex.entries.values();

        long sum = 0;

        for ( BackedGaugeIndex.Entry entry : entries ) {

            Set<String> tagNames = tagNames( entry.getTags() );

            if ( tagNames.contains( tag.getName() ) ) {
                sum += entry.getValue();
            }

        }

        return sum;

    }

    private Set<String> tagNames( List<Tag> tags ) {

        Set<String> result = Sets.newHashSet();

        for (Tag tag : tags) {
            result.add( tag.getName() );
        }

        return result;

    }

    @Override
    public String toString() {
        return "" + getValue();
    }

}


