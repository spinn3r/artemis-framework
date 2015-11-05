package com.spinn3r.artemis.metrics.tags;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Keeps a set of tag names together as one set.
 */
public class TagNameSet extends LinkedHashSet<String> {

    private Set<String> suffixLiteralTags = new LinkedHashSet<>();

    /**
     * Make the tags use a suffix literal. For example if we have tags on
     * publisher_type=mainstream news then this would expand to a suffix of
     * publisher_type/mainstream_news for each tag.
     *
     * DO NOT do this on high cardinality tags. Only low cardinality tag values
     * with just a few metric names.
     *
     * @param suffixLiteralTags
     * @return
     */
    public TagNameSet withSuffixLiteralTags(String... suffixLiteralTags) {

        for (String suffixLiteral : suffixLiteralTags) {
            this.suffixLiteralTags.add( suffixLiteral );
        }

        return this;

    }

    public Set<String> getSuffixLiteralTags() {
        return suffixLiteralTags;
    }

}
