package com.spinn3r.artemis.util.primitives;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.guava.ImmutableCollectors;

/**
 *
 */
public class Characters {

    public static ImmutableList<Character> range(char start, char end) {

        return Integers.range(start, end)
                       .stream()
                       .map( current -> (char)current.intValue())
                       .collect(ImmutableCollectors.toImmutableList());

    }

}
