package com.spinn3r.artemis.util.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * An iterator that can throw IO exceptions.
 */
public interface InputIterator<T> extends Closeable {

    boolean hasNext() throws IOException;

    T next() throws IOException;

}
