package com.spinn3r.artemis.metrics;

import com.spinn3r.artemis.metrics.tags.TagMap;
import com.spinn3r.artemis.metrics.tags.TagsProvider;
import com.spinn3r.log5j.Logger;
import com.spinn3r.metrics.kairosdb.Tag;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

import java.util.List;

import static com.spinn3r.artemis.metrics.tags.Tags.toArray;

/**
 * A counter that takes tags, but also denormalizes them and broadcasts additional tags.
 */
public class Stat<T> {

    private static final Logger log = Logger.getLogger();

    private TaggedMetrics taggedMetrics;

    private TagsProvider<T> tagsProvider;

    private String namePrefix = null;

    private String nameSuffix;

    public Stat(TaggedMetrics taggedMetrics,
                TagsProvider<T> tagsProvider,
                String namePrefix,
                String nameSuffix) {

        this.taggedMetrics = taggedMetrics;
        this.tagsProvider = tagsProvider;
        this.namePrefix = namePrefix;
        this.nameSuffix = nameSuffix;

    }


    public Stat(TaggedMetrics taggedMetrics,
                TagsProvider<T> tagsProvider,
                String nameSuffix) {

        this.taggedMetrics = taggedMetrics;
        this.tagsProvider = tagsProvider;
        this.nameSuffix = nameSuffix;

    }

    /**
     * Increase the given value by 1.
     */
    public void incr( T value ) {
        incr( value, 1 );
    }

    /**
     * Increase the given value by the given delta.  Must be greater than zero.
     *
     * @param value
     * @param delta
     */
    public void incr( T value, long delta ) {

        // NOTE: in the past we didn't increment on zero but this is incorrect
        // due to our shared use of gauges and accuracy metrics..  If an accuracy
        // value emits zero, we need to record this as this is a BIG error and
        // not recording zero would mean some of our alerting would fail

        if ( delta < 0 ) {
            throw new IllegalArgumentException( "Value must not be negative " + delta );
        }

        try {

            List<Tag> tags = tagsProvider.getTags( value );
            List<TagMap> denormalizedTags = tagsProvider.denormalize( value, tags );

            String namePrefix = this.namePrefix;

            if ( namePrefix == null ) {
                namePrefix = value.getClass().getName();
            }

            if ( ! tagsProvider.getDenormalizedOnly() ) {
                taggedMetrics.counter( namePrefix, nameSuffix, toArray( tags ) ).inc( delta );
            }

            // *** create the denormalized version of our metrics which are faster.

            for( TagMap current : denormalizedTags ) {

                String suffix = String.format( "%s/%s", nameSuffix, current.formatKeys() );

                taggedMetrics.counter( namePrefix,
                                       suffix,
                                       toArray( current.getTags() ) ).inc( delta );

            }

        } catch ( Exception e ) {
            log.error( "Unable to update metric data: ", e );
        }

    }

    public static <T> Stat<T> stat( TaggedMetrics taggedMetrics,
                                    TagsProvider<T> tagsProvider,
                                    Class namePrefixClass,
                                    String nameSuffix ) {

        return new Stat<>( taggedMetrics, tagsProvider, namePrefixClass.getName(), nameSuffix );

    }

    public static <T> Stat<T> stat( TaggedMetrics taggedMetrics,
                                    TagsProvider<T> tagsProvider,
                                    String namePrefix,
                                    String nameSuffix ) {

        return new Stat<>( taggedMetrics, tagsProvider, namePrefix, nameSuffix );

    }

    public static <T> Stat<T> stat( TaggedMetrics taggedMetrics,
                                    TagsProvider<T> tagsProvider,
                                    String nameSuffix ) {

        return new Stat<>( taggedMetrics, tagsProvider, nameSuffix );

    }

}
