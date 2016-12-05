package com.spinn3r.artemis.util.io;

import com.spinn3r.artemis.util.iterators.CheckedIterator;

import java.io.Closeable;
import java.io.IOException;

/**
 * An iterator that can throw IO exceptions.
 */
public interface InputIterator<T> extends Closeable, CheckedIterator<T> {

    @Override
    boolean hasNext() throws IOException;

    @Override
    T next() throws IOException;

}
