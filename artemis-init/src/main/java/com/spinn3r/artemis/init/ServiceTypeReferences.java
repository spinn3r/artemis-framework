package com.spinn3r.artemis.init;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * Contains a map of ServiceType to ModularService.  This way we can combine
 * ModularServiceReferences by replacing the service type with a new service type.
 * This allows us to swap in testing services replacing production services for
 * mocking and integration.
 */
public class ServiceTypeReferences {

    protected LinkedHashMap<Class<? extends ServiceType>,ServiceTypeReference> backing = new LinkedHashMap<>();

    public <T extends ServiceType> ServiceTypeReferences put(Class<T> serviceType, Class<? extends Service> service ) {
        checkNotNull( serviceType );
        checkNotNull( service );

        ServiceTypeReference serviceTypeReference = new ServiceTypeReference(serviceType, service );
        backing.put(serviceType, serviceTypeReference);
        return this;

    }

    /**
     * Add a service, automatically determine it's ServiceType, and update the
     * mapping.
     *
     * If an existing service with the same key is present, it's replace,
     * preserving the original ServiceType order.
     */
    public <T extends ServiceType & Service> ServiceTypeReferences put(Class<T> service) {
        Class<? extends ServiceType> serviceType = ServiceTypes.determineServiceType(service);
        ServiceTypeReference serviceTypeReference = new ServiceTypeReference(serviceType, service );
        put(serviceType, serviceTypeReference);
        return this;
    }

    public ServiceTypeReferences put(Class<? extends ServiceType> serviceType, ServiceTypeReference serviceTypeReference) {
        backing.put(serviceType, serviceTypeReference);
        return this;
    }

    /**
     * Add additional services after the given pointer.  We DO NOT add services
     * if they are already in the index.
     */
    public ServiceTypeReferences include(Class<? extends ServiceType> serviceTypePointer, ServiceTypeReferences additionalServiceTypeReferences) {

        LinkedHashMap<Class<? extends ServiceType>,ServiceTypeReference> newBacking = new LinkedHashMap<>();

        boolean found = false;

        for ( ServiceTypeReference serviceTypeReference : backing.values() ) {

            newBacking.put(serviceTypeReference.getSource(), serviceTypeReference);

            if ( serviceTypeReference.getSource().equals(serviceTypePointer ) ) {

                found = true;

                for (ServiceTypeReference newServiceTypeReference : additionalServiceTypeReferences.backing.values()) {
                    newBacking.put(newServiceTypeReference.getSource(), newServiceTypeReference);
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

        for (Map.Entry<Class<? extends ServiceType>, ServiceTypeReference> entry : backing.entrySet()) {
            result.add(entry.getValue().getTarget());
        }

        return result;

    }

    /**
     * Replace service references in this mapping with the references in the
     * given binding.
     */
    public ServiceTypeReferences replace(ServiceTypeReferences newServiceReferences ) {
        backing.putAll( newServiceReferences.backing );
        return this;
    }

    public ServiceTypeReference get(Class<? extends ServiceType> serviceType ) {
        return backing.get( serviceType );
    }

    /**
     * @return the number of services references used.
     */
    public int size() {
        return backing.size();
    }

    public ImmutableCollection<ServiceTypeReference> entries() {
        return ImmutableList.copyOf(backing.values());
    }

    /**
     * Format the service setup.
     */
    public String format() {

        // TODO: might want to consider using the TableFormatter in the future
        // as that might be easier than manually printing a table.

        StringBuilder buff = new StringBuilder();

        for ( ServiceTypeReference serviceTypeReference : backing.values() ) {

            buff.append( "    " );
            buff.append( String.format("%70s = %-70s", serviceTypeReference.getSource().getName(), serviceTypeReference.getTarget().getName() ) );
            buff.append( "\n" );

        }

        return buff.toString();

    }

}
