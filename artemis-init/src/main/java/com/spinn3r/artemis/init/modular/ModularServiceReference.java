package com.spinn3r.artemis.init.modular;

import com.google.common.base.Preconditions;
import com.spinn3r.artemis.init.Service;
import com.spinn3r.artemis.init.ServiceReference;

/**
 *
 */
public class ModularServiceReference implements Comparable<ModularServiceReference> {

    private final Class<? extends ModularService> backing;

    public ModularServiceReference(Class<? extends ModularService> backing) {
        Preconditions.checkNotNull( backing );
        this.backing = backing;
    }

    public Class<? extends ModularService> getBacking() {
        return backing;
    }

    @Override
    public int compareTo(ModularServiceReference o) {
        return backing.getName().compareTo( o.getBacking().getName() );
    }

    public String toString() {
        return backing.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof ModularServiceReference )) return false;

        ModularServiceReference that = (ModularServiceReference) o;

        return backing.equals( that.backing );

    }

    @Override
    public int hashCode() {
        return backing.hashCode();
    }

}
