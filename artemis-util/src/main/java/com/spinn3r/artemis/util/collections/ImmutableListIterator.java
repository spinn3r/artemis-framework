package com.spinn3r.artemis.util.collections;

import java.util.ListIterator;
import java.util.function.Consumer;

/**
 *
 */
public class ImmutableListIterator<T> implements ListIterator<T> {

    private final ListIterator<T> delegate;

    public ImmutableListIterator(ListIterator<T> delegate) {
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
    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    @Override
    public T previous() {
        return delegate.previous();
    }

    @Override
    public int nextIndex() {
        return delegate.nextIndex();
    }

    @Override
    public int previousIndex() {
        return delegate.previousIndex();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        delegate.forEachRemaining(action);
    }

}
