package com.spinn3r.artemis.util.canonicalizers;

/**
 * Takes an input object, and produces a new output object which is canonicalized.
 */
public interface Canonicalizer<T> {

    T canonicalize(T input);

}
