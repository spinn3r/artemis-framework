package com.spinn3r.artemis.init.modular;

/**
 *
 */
public class ServiceMapping {

    private final Class<? extends ServiceType> source;

    private final Class<? extends ServiceType> target;

    public <T extends ServiceType> ServiceMapping(Class<T> source, Class<? extends T> target ) {
        this.source = source;
        this.target = target;
    }

    public Class<? extends ServiceType> getSource() {
        return source;
    }

    public Class<? extends ServiceType> getTarget() {
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
