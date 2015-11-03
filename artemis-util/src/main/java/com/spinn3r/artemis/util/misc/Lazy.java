package com.spinn3r.artemis.util.misc;

/**
 * Perform lazy init of an object which requires reading from some external
 * system.  This only works with code that DOES NOT throw an exception. If the
 * underlying code throws an exception you MUST NOT attempt to reuse a lazy
 * object.  You must fail otherwise we will return null which is incorrect.
 */
public abstract class Lazy<T> {

    protected boolean initialized = false;

    protected T value = null;

    public T get() {

        if ( ! initialized ) {

            try {
                value = read();
            } finally {
                initialized = true;
            }

        }

        return value;

    }

    /**
     * Read the value backing this item if we are have not yet read it.
     */
    protected abstract T read();

}
