package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.Service;

/**
 *
 */
public class ServiceMapping {

    private final Class<? extends ServiceType> source;

    private final Class<? extends Service> target;

    protected ServiceMapping(Class<? extends ServiceType> source, Class<? extends Service> target) {
        this.source = source;
        this.target = target;
    }

    public Class<? extends ServiceType> getSource() {
        return source;
    }

    public Class<? extends Service> getTarget() {
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
