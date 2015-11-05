package com.spinn3r.artemis.metrics.tags;

/**
 *
 */
public class TagNameSets {

    public static TagNameSet tagNameSet(String... names) {

        TagNameSet tagNameSet = new TagNameSet();

        for (String name : names) {
            tagNameSet.add( name );
        }

        return tagNameSet;

    }

}
