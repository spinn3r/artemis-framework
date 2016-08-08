package com.spinn3r.artemis.util.io;

import com.google.common.collect.ImmutableList;

import java.io.Closeable;
import java.io.IOException;

/**
 * Holds a set of closeables so that we can use all of them in a try-with-resources
 * try block.
 */
public class CloseableHolder<T extends Closeable> implements Closeable {

    private final ImmutableList<T> backings;

    public CloseableHolder(ImmutableList<T> backings) {
        this.backings = backings;
    }

    public ImmutableList<T> getBackings() {
        return backings;
    }

    @Override
    public void close() throws IOException {
        Closeables.close(backings);
    }

}
