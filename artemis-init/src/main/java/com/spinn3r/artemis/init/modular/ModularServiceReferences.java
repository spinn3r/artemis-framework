package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.Service;
import com.spinn3r.artemis.init.ServiceReferences;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * Contains a map of ServiceType to ModularService.  This way we can combine
 * ModularServiceReferences by replacing the service type with a new service type.
 * This allows us to swap in testing services replacing production services for
 * mocking and integration.
 */
public class ModularServiceReferences {

    protected LinkedHashMap<Class<? extends ServiceType>,ServiceMapping> backing = new LinkedHashMap<>();

    public <T extends ServiceType> ModularServiceReferences put( Class<T> serviceType, Class<? extends Service> service ) {
        checkNotNull( serviceType );
        checkNotNull( service );

        ServiceMapping serviceMapping = new ServiceMapping( serviceType, service );
        backing.put( serviceType, serviceMapping );
        return this;

    }

    /**
     * Add a service, automatically determine it's ServiceType, and update the
     * mapping.
     *
     * If an existing service with the same key is present, it's replace,
     * preserving the original ServiceType order.
     */
    public <T extends ServiceType & Service> ModularServiceReferences put(Class<T> service) {
        Class<? extends ServiceType> serviceType = ServiceTypes.determineServiceType(service);
        ServiceMapping serviceMapping = new ServiceMapping( serviceType, service );
        put(serviceType, serviceMapping);
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
     * Convert this to a list of services to initialize the older/legacy
     * Launcher which just uses service references.
     */
    public ServiceReferences toServiceReferences() {

        ServiceReferences result = new ServiceReferences();

        for (Map.Entry<Class<? extends ServiceType>, ServiceMapping> entry : backing.entrySet()) {
            result.add(entry.getValue().getTarget());
        }

        return result;

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
