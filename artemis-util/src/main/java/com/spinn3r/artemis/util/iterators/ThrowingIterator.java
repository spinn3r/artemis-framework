package com.spinn3r.artemis.util.iterators;

/**
 *
 */
public interface ThrowingIterator<T,E extends Throwable> {

    boolean hasNext() throws E;

    T next() throws E;

}

