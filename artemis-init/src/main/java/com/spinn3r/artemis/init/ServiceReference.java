package com.spinn3r.artemis.init;

/**
 *
 */
public class ServiceReference implements Comparable<ServiceReference> {

    private final Class<? extends Service> backing;

    public ServiceReference(Class<? extends Service> backing) {
        this.backing = backing;
    }

    public Class<? extends Service> getBacking() {
        return backing;
    }

    @Override
    public int compareTo(ServiceReference o) {
        return backing.getName().compareTo( o.getBacking().getName() );
    }

    public String toString() {
        return backing.getName();
    }

}
