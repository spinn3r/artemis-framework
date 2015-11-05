package com.spinn3r.artemis.metrics.tags;

import com.spinn3r.metrics.kairosdb.Tag;

import java.util.List;

/**
 *
 */
public interface TagsProvider<T> {

    /**
     * Get a list of tags to use for a metric.
     *
     * @return
     */
    public List<Tag> getTags( T value );

    /**
     * Given a set of tags, determine which ones to denormalize.
     *
     * @param value
     * @param tags
     * @return
     */
    public List<TagMap> denormalize( T value, List<Tag> tags );

    /**
     * Only write the denormalized form of the metric as the metric is too
     * sparse to be used performantly.  This will probably be the default
     * moving forward.
     * @return
     */
    public boolean getDenormalizedOnly();

}
