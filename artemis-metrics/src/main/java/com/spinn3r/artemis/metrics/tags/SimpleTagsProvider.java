package com.spinn3r.artemis.metrics.tags;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.List;
import java.util.Set;

/**
 * A tags provider which makes it easy to used denormalized tags.
 */
public abstract class SimpleTagsProvider<T> implements TagsProvider<T> {

    private boolean denormalizedOnly = false;

    private Set<TagNameSet> denormalizedTagNames = Sets.newHashSet();

    public void setDenormalizedTagNames( Set<TagNameSet> denormalizedTagNames ) {
        this.denormalizedTagNames = denormalizedTagNames;
    }

    public void setDenormalizedTagNames( TagNameSet... tagNameSets ) {

        denormalizedTagNames = Sets.newLinkedHashSet();

        for (TagNameSet tagNameSet : tagNameSets) {
            denormalizedTagNames.add( tagNameSet );
        }

    }

    public void setDenormalizedTagNames( String... denormalizedTagNames ) {

        this.denormalizedTagNames = Sets.newLinkedHashSet();

        for (String tagName : denormalizedTagNames) {
            this.denormalizedTagNames.add( TagNameSets.tagNameSet( tagName ) );
        }

    }

    @Override
    public List<TagMap> denormalize(T value, List<Tag> tags ) {

        TagMap currentTags = new TagMap( tags );

        List<TagMap> result = Lists.newArrayList();

        for (TagNameSet tagNameSet : denormalizedTagNames) {

            if ( currentTags.keySet().containsAll( tagNameSet ) ) {

                TagMap tagMap = new TagMap();

                for (String tagName : tagNameSet) {
                    tagMap.put( tagName, currentTags.get( tagName ) );
                }

                tagMap.setSuffixLiteralTags( tagNameSet.getSuffixLiteralTags() );

                result.add( tagMap );

            }

        }

        return result;

    }

    @Override
    public boolean getDenormalizedOnly() {
        return denormalizedOnly;
    }

    public void setDenormalizedOnly(boolean denormalizedOnly) {
        this.denormalizedOnly = denormalizedOnly;
    }

    /**
     * Enable use of an optional tag value.  If the tag value is null, we use
     * the default value.  This can provide a more meaningful value instead of
     * null or an empty string.
     *
     */
    protected String optional( Object value, String _default ) {

        if ( value == null || "".equals( value.toString().trim() ) ) {
            value = _default;
        }

        return value.toString();

    }

}
