package com.spinn3r.artemis.util.text;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class CollectionFormatter {

    /**
     * Formats the collection as a list which is indented and multiline.
     *
     * @param collection
     * @return
     */
    public static String format( Collection<? extends Object> collection ) {

        StringBuilder buff = new StringBuilder();

        for (Object value : collection) {
            buff.append( String.format( "    %s\n", value.toString() ) );
        }

        return buff.toString();

    }

    public static String table( Collection<? extends Object> collection ) {

        StringBuilder buff = new StringBuilder();

        for (Object value : collection) {
            buff.append( String.format( "%s\n", value.toString() ) );
        }

        return buff.toString();

    }


    /**
     * More portable and deterministic version of toString which sorts values
     *
     * Various set implementations have different semantics on how keys are sorted.
     *
     * @param set the set to sort and then return toSring.
     * @return
     */
    public static String toString(Set<?> set) {

        Set<String> stringSet = Sets.newTreeSet();

        for (Object obj : set) {
            stringSet.add( obj.toString() );
        }


        return stringSet.toString();

    }

}
