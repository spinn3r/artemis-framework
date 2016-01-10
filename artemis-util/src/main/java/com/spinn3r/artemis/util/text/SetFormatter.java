package com.spinn3r.artemis.util.text;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class SetFormatter {

    public static  <E> String table(Set<E> set) {
        checkNotNull( set );

        StringBuilder buff = new StringBuilder();

        for (E e : set) {
            buff.append( e.toString() );
            buff.append( "\n" );
        }

        return buff.toString();

    }

    public static String toString( Set<?> set ) {

        Set<String> stringSet = Sets.newTreeSet();

        for (Object value : set) {
            stringSet.add( value.toString() );
        }

        return stringSet.toString();

    }

}
