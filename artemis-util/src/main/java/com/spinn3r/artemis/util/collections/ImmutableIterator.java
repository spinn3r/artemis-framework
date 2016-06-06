package com.spinn3r.artemis.util.collections;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 *
 */
public class ImmutableIterator<T> implements Iterator<T> {

    private final Iterator<T> delegate;

    public ImmutableIterator(Iterator<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public T next() {
        return delegate.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        delegate.forEachRemaining(action);
    }

}
