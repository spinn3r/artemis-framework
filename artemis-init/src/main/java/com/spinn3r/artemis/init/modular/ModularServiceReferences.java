package com.spinn3r.artemis.init.modular;

import java.util.LinkedHashMap;

import static com.google.common.base.Preconditions.*;

/**
 * Contains a map of ServiceType to ModularService.  This way we can combine
 * ModularServiceReferences by replacing the service type with a new service type.
 * This allows us to swap in testing services replacing production services for
 * mocking and integration.
 */
public class ModularServiceReferences {

    protected LinkedHashMap<Class<? extends ServiceType>,ServiceMapping> backing = new LinkedHashMap<>();

    public <T extends ServiceType> ModularServiceReferences put( Class<T> serviceType, Class<? extends T> service ) {
        checkNotNull( serviceType );
        checkNotNull( service );

        ServiceMapping serviceMapping = new ServiceMapping( serviceType, service );
        backing.put( serviceType, serviceMapping );
        return this;

    }

    public ModularServiceReferences put( Class<? extends ServiceType> serviceType, ServiceMapping serviceMapping ) {
        backing.put( serviceType, serviceMapping );
        return this;
    }

    /**
     * Add additional services after the given pointer.  We DO NOT add services
     * if they are already in the index.
     */
    public ModularServiceReferences include( Class<? extends ServiceType> serviceTypePointer, ModularServiceReferences additionalModularServiceReferences ) {

        LinkedHashMap<Class<? extends ServiceType>,ServiceMapping> newBacking = new LinkedHashMap<>();

        boolean found = false;

        for ( ServiceMapping serviceMapping : backing.values() ) {

            newBacking.put( serviceMapping.getSource(), serviceMapping );

            if ( serviceMapping.getSource().equals( serviceTypePointer ) ) {

                found = true;

                for (ServiceMapping newServiceMapping : additionalModularServiceReferences.backing.values()) {
                    newBacking.put( newServiceMapping.getSource(), newServiceMapping );
                }

            }

        }

        if ( ! found ) {
            throw new RuntimeException( String.format( "Could not find index of: %s in %s", serviceTypePointer, backing ) );
        }

        backing = newBacking;
        return this;

    }

    /**
     * Replace service references in this mapping with the references in the
     * given binding.
     */
    public ModularServiceReferences replace( ModularServiceReferences newServiceReferences ) {
        backing.putAll( newServiceReferences.backing );
        return this;
    }

    public ServiceMapping get( Class<? extends ServiceType> serviceType ) {
        return backing.get( serviceType );
    }

    /**
     * @return the number of services references used.
     */
    public int size() {
        return backing.size();
    }

    /**
     * Format the service setup.
     */
    public String format() {

        // TODO: might want to consider using the TableFormatter in the future
        // as that might be easier than manually printing a table.

        StringBuilder buff = new StringBuilder();

        for ( ServiceMapping serviceMapping : backing.values() ) {

            buff.append( "    " );
            buff.append( String.format( "%70s = %-70s", serviceMapping.getSource().getName(),  serviceMapping.getTarget().getName() ) );
            buff.append( "\n" );

        }

        return buff.toString();

    }

}
