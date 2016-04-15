package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class CollectionOptionals {

    public static <T> Optional<T> first(Collection<T> input) {

        return first( Lists.newArrayList(input) );

    }

    public static <T> Optional<T> first(List<T> input) {

        if ( input.size() == 0 )
            return Optional.empty();

        return Optional.of(input.get(0));
    }

    public static <T> Optional<T> last(Collection<T> input) {

        return last( Lists.newArrayList(input) );
    }

    public static <T> Optional<T> last(List<T> input) {

        if ( input.size() == 0 )
            return null;

        return Optional.of( input.get( input.size() - 1 ) );

    }

}
