package com.spinn3r.artemis.init;

import com.google.common.base.Preconditions;

/**
 *
 */
public class ServiceReference implements Comparable<ServiceReference> {

    private final Class<? extends Service> backing;

    public ServiceReference(Class<? extends Service> backing) {
        Preconditions.checkNotNull( backing );
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof ServiceReference )) return false;

        ServiceReference that = (ServiceReference) o;

        return backing.equals( that.backing );

    }

    @Override
    public int hashCode() {
        return backing.hashCode();
    }
}
