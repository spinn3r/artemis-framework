package com.spinn3r.artemis.init.modular;

/**
 *
 */
public class ClassMapping<T> {

    private final Class<? extends T> source;

    private final Class<? extends T> target;

    public ClassMapping(Class<T> source, Class<? extends T> target ) {
        this.source = source;
        this.target = target;
    }

    public Class<? extends T> getSource() {
        return source;
    }

    public Class<? extends T> getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "ClassMapping{" +
                 "source=" + source +
                 ", target=" + target +
                 '}';
    }
}
