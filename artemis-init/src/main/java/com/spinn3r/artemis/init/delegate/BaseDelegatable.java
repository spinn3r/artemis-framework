package com.spinn3r.artemis.init.delegate;

/**
 *
 */
public abstract class BaseDelegatable<T> {

    protected T delegate;

    public void setDelegate(T delegate) {
        this.delegate = delegate;
    }

    public T getDelegate() {
        return delegate;
    }

}
