package com.spinn3r.artemis.metrics.tags;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * An index of tag name to Tag for support in denormalization.
 */
public class TagMap extends LinkedHashMap<String,Tag> {

    private Set<String> suffixLiteralTags = new LinkedHashSet<>();

    public TagMap() {
        super();
    }

    public TagMap( List<Tag> tags ) {

        for (Tag tag : tags) {
            put( tag.getName(), tag );
        }

    }

    public Set<String> getSuffixLiteralTags() {
        return suffixLiteralTags;
    }

    public void setSuffixLiteralTags(Set<String> suffixLiteralTags) {
        this.suffixLiteralTags = suffixLiteralTags;
    }

    /**
     * Format keys for use in a metric name.
     */
    public String formatKeys() {

        StringBuilder buff = new StringBuilder();

        for (String key : keySet()) {

            if ( buff.length() > 0 ) {
                buff.append( "/" );
            }

            if ( suffixLiteralTags.contains( key ) ) {

                String value = get( key ).getValue();

                buff.append( key );
                buff.append( "/" );
                buff.append( value );

            } else {
                buff.append( key );
            }

        }

        return buff.toString();

    }

    /**
     * Get the tags but excluding any of them that are literal.
     *
     * @return
     */
    public List<Tag> getTags() {

        List<Tag> result = Lists.newArrayList();

        for (Tag tag : values()) {

            if ( suffixLiteralTags.contains( tag.getName() ) ) {
                continue;
            }

            result.add( tag );

        }

        return result;

    }

}
