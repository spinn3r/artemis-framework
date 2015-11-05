package com.spinn3r.artemis.metrics.tags;

import com.spinn3r.artemis.metrics.init.MetricsConfig;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.ArrayList;

import static com.spinn3r.metrics.kairosdb.TaggedMetrics.tag;

/**
 * Represents a list of tags.  The add method supports sparse tags directly so
 * that if we add a tag that's not sparse, it won't be emitted.
 *
 */
public class TagList extends ArrayList<Tag> {

    private final MetricsConfig metricsConfig;

    private final TagsProvider tagsProvider;

    /**
     * Create a tag list using the given metric config.
     *
     * @param metricsConfig
     */
    public TagList(MetricsConfig metricsConfig, TagsProvider tagProvider ) {
        super();
        this.metricsConfig = metricsConfig;
        this.tagsProvider = tagProvider;
    }

    public void add( String tagName, String tagValue ) {

        if ( isEnabled( tagName, tagValue ) )
            add( tag( tagName, tagValue ) );

    }

    public void add( String tagName, Object tagValue ) {
        add( tagName, tagValue.toString() );
    }

    public void add( String tagName, int tagValue ) {

        // TODO: sparse not supported yet.
        add( tag( tagName, tagValue ) );

    }

    public void add( String tagName, long tagValue ) {

        // TODO: sparse not supported yet.
        add( tag( tagName, tagValue ) );

    }

    public void add( String tagName, boolean tagValue ) {

        // TODO: sparse not supported yet.
        add( tag( tagName, tagValue ) );

    }

    private boolean isEnabled( String tagName, String tagValue ) {

        MetricsConfig.SparseMetricTags sparseMetricTags =
          metricsConfig.getSparseMetricsIndex().get( tagsProvider.getClass().getName() );

        // there is no map for this tag provider so by default this tag is enabled.
        if ( sparseMetricTags == null )
            return true;

        MetricsConfig.SparseMetricTagValues tagValues = sparseMetricTags.get( tagName );

        // there is no tag entry so by default it's enabled.
        if ( tagValues == null )
            return true;

        // return true if the tag value is in the index.
        return tagValues.contains( tagValue );

    }

}
