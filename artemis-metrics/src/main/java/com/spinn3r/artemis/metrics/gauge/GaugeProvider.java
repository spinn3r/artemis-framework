package com.spinn3r.artemis.metrics.gauge;

import com.spinn3r.artemis.metrics.tags.TagsProvider;

/**
 * Provides both the tags for a gauge as well as its current value from a
 * given object.
 *
 * <p>
 * This usually involved parsing out the command object for its value as well
 * as any specific tags.
 *
 */
public interface GaugeProvider<T> extends TagsProvider<T> {

    /**
     * Get key for this gauge. Usually this would be a simple key like 'acme' or
     * a vendor code.  The collection of tags and their values should also work
     * well as a key.
     *
     */
    public String getKey( T value );

    /**
     * Get the value for the gauge from the given object.
     */
    public long getValue( T value );

}
