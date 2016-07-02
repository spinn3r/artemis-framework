package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.Service;
import com.spinn3r.artemis.init.modular.ServiceType;

/**
 *
 */
public class ServiceTypeReference {

    private final Class<? extends ServiceType> source;

    private final Class<? extends Service> target;

    protected ServiceTypeReference(Class<? extends ServiceType> source, Class<? extends Service> target) {
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
        return "ServiceTypeReference{" +
                 "source=" + source +
                 ", target=" + target +
                 '}';
    }

}
