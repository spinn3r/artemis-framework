package com.spinn3r.artemis.metrics.gauge;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spinn3r.artemis.metrics.tags.TagMap;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.log5j.Logger;
import com.spinn3r.metrics.kairosdb.Tag;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.spinn3r.artemis.metrics.tags.Tags.toArray;

/**
 * Maintain an in-memory index for our gauges...
 */
public class BackedGaugeIndex<T> {

    private static final Logger log = Logger.getLogger();

    private final Clock clock;

    private final long expirationIntervalMillis;

    private final TaggedMetrics taggedMetrics;

    private final Class<?> clazz;

    private final String name0;

    private final GaugeProvider<T> gaugeProvider;

    private final Map<String,Metric> metrics;

    protected Map<String,Entry> entries = new ConcurrentHashMap<>();

    public BackedGaugeIndex( Clock clock,
                             long expirationIntervalMillis,
                             TaggedMetrics taggedMetrics,
                             GaugeProvider<T> gaugeProvider,
                             Class<?> clazz,
                             String name0 ) {

        this.clock = clock;
        this.expirationIntervalMillis = expirationIntervalMillis;
        this.taggedMetrics = taggedMetrics;
        this.clazz = clazz;
        this.name0 = name0;
        this.gaugeProvider = gaugeProvider;

        this.metrics = taggedMetrics.getMetricRegistry().getMetrics();

    }

    /**
     * Update the in-memory index with the given object.
     */
    public void update( T value ) {

        try {

            String key = gaugeProvider.getKey( value );

            if (key == null) {
                throw new NullPointerException( "key" );
            }

            List<Tag> tags = gaugeProvider.getTags( value );

            List<TagMap> denormalizedTags = gaugeProvider.denormalize( value, tags );

            if ( ! gaugeProvider.getDenormalizedOnly() ) {

                String metricName = taggedMetrics.name( clazz, name0, toArray( tags ) );

                Entry entry = new Entry( key,
                                         gaugeProvider.getValue( value ),
                                         metricName,
                                         tags,
                                         clock.currentTimeMillis() );

                entries.put( key, entry );

                registerWhenNecessary( metricName, new BackedGauge( this, key ) );

            }

            // **** now handle denormalized tags.

            for( TagMap current : denormalizedTags ) {

                // right now we only support one tag.
                if ( current.size() != 1 )
                    continue;

                String suffix = String.format( "%s/%s", name0, current.formatKeys() );

                String denormalizedMetricName = taggedMetrics.name( clazz, suffix, toArray( Lists.newArrayList( current.values() ) ) );

                Tag tag = Lists.newArrayList( current.values() ).get( 0 );

                registerWhenNecessary( denormalizedMetricName, new DenormalizedBackedGauge( this, tag ) );

            }

        } catch ( RuntimeException e ) {

            // catch any misc exceptions and log them.  This shouldn't happen
            // in practice but could happen with our provider implementations

            log.error( "Unable to update gauge: ", e );

        }

    }

    public int size() {
        return entries.size();
    }

    private void registerWhenNecessary(String metricName, Gauge<Long> gauge ) {

        try {

            if ( ! metrics.containsKey( metricName ) ) {
                taggedMetrics.register( gauge, metricName );
            }

        } catch (IllegalArgumentException e) {
            // noop for now... the codahale metrics prevents us from registering
            // duplicate metrics so we only care that the first succeeds
        }

    }


    protected Entry get( String key ) {

        return entries.get( key );

    }

    /**
     * Expire any value that are no long valid.
     */
    public void expire() {

        for( Entry entry : findExpiredEntries() ) {
            taggedMetrics.getMetricRegistry().remove( entry.getMetric() );
            entries.remove( entry.getKey() );
        }

    }

    /**
     * Find all metrics that need expiration.
     *
     * @return
     */
    protected Set<Entry> findExpiredEntries() {

        long now = clock.currentTimeMillis();

        Set<Entry> result = Sets.newHashSet();

        for( Entry entry : entries.values() ) {

            if ( now - entry.getTimestamp() > expirationIntervalMillis ) {
                result.add( entry );
            }

        }

        return result;

    }

    public class Entry {

        // the name of the metric

        private final String key;

        private final long value;

        private final String metric;

        private final List<Tag> tags;

        private final long timestamp;

        public Entry(String key, long value, String metric, List<Tag> tags, long timestamp) {
            this.key = key;
            this.value = value;
            this.metric = metric;
            this.tags = tags;
            this.timestamp = timestamp;
        }

        public String getKey() {
            return key;
        }

        public long getValue() {
            return value;
        }

        public String getMetric() {
            return metric;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

}
