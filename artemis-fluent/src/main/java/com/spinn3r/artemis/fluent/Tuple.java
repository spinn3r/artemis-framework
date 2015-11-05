package com.spinn3r.artemis.fluent;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.CollectionUtils;
import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A tuple of elements which are all non-null.
 */
public class Tuple<T> {

    protected List<T> backing = Lists.newArrayList();

    @SafeVarargs
    public Tuple(T... elements) {

        for (T element : elements) {
            if ( element != null )
                backing.add( element );
        }

    }

    public Tuple(Collection<T> elements) {

        for (T element : elements) {
            if ( element != null )
                backing.add( element );
        }

    }

    public Tuple<T> filter( Matcher<? super T> matcher ) {

        List<T> tmp = Lists.newArrayList();

        for (T current : backing) {

            if ( matcher.matches( current ) ) {
                tmp.add( current );
            }

        }

        return new Tuple<>( tmp );

    }

    public Tuple<T> filter( Predicate<T> predicate ) {

        List<T> tmp = Lists.newArrayList();

        for (T current : backing) {

            if ( predicate.test( current ) ) {
                tmp.add( current );
            }

        }

        return new Tuple<>( tmp );

    }


    public T first() {
        return CollectionUtils.first( backing );
    }

    public T last() {
        return CollectionUtils.last( backing );
    }

    public Tuple<T> consumeFirst(Consumer<T> elementQueryFunction) {

        T first = first();

        if ( first != null ) {
            elementQueryFunction.accept( first );
        }

        return this;

    }

    public Tuple<T> head( int count ) {
        return new Tuple<>( CollectionUtils.head( backing, count ) );
    }

    public Tuple<T> tail( int count ) {
        return new Tuple<>( CollectionUtils.tail( backing, count ) );
    }

    @Override
    public String toString() {
        return backing.toString();
    }

}
