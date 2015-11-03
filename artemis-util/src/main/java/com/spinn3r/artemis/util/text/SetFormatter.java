package com.spinn3r.artemis.util.text;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 *
 */
public class SetFormatter {

    public static String toString( Set<?> set ) {

        Set<String> stringSet = Sets.newTreeSet();

        for (Object value : set) {
            stringSet.add( value.toString() );
        }

        return stringSet.toString();

    }

}
