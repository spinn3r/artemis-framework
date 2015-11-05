package com.spinn3r.artemis.init.delegate;

/**
 *
 */
public interface Delegatable<T> {

    public void setDelegate( T delegate );

    public T getDelegate();

}
