package com.spinn3r.artemis.util.iterators;

/**
 *
 */
public interface CheckedIterator<T> extends ThrowingIterator<T,Exception> {

    boolean hasNext() throws Exception;

    T next() throws Exception;

}
