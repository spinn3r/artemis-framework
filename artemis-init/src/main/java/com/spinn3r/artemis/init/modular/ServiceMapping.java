package com.spinn3r.artemis.init.modular;

/**
 *
 */
public class ServiceMapping {

    private final Class<? extends ServiceType> source;

    private final Class<? extends ServiceType> target;

    protected ServiceMapping(Class<? extends ServiceType> source, Class<? extends ServiceType> target) {
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
