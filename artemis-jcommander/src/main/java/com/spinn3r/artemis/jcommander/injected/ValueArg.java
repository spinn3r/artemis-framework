package com.spinn3r.artemis.jcommander.injected;

/**
 *
 */
public abstract class ValueArg<T> {

    private final T value;

    public ValueArg(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
