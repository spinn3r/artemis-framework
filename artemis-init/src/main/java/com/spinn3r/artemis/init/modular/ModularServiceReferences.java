package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.util.text.MapFormatter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains a map of ServiceType to ModularService.  This way we can combine
 * ModularServiceReferences by replacing the service type with a new service type.
 * This allows us to swap in testing services replacing production services for
 * mocking and integration.
 */
public class ModularServiceReferences {

    protected LinkedHashMap<Class<? extends ServiceType>,Class<? extends ModularService>> backing = new LinkedHashMap<>();

    public <T extends ServiceType> ModularServiceReferences put( Class<T> serviceType, Class<? extends T> service ) {

        backing.put( serviceType, service );
        return this;

    }

    // TODO: we need to be able to support include() of more service...

    /**
     * Add additional services after the given pointer.  We DO NOT add services
     * if they are already in the index.
     */
    public void include( Class<? extends ServiceType> serviceTypePointer, ModularServiceReferences additionalModularServiceReferences ) {

        LinkedHashMap<Class<? extends ServiceType>,Class<? extends ModularService>> newBacking = new LinkedHashMap<>();

        boolean found = false;

        for (Map.Entry<Class<? extends ServiceType>, Class<? extends ModularService>> entry : backing.entrySet()) {

            newBacking.put( entry.getKey(), entry.getValue() );

            if ( entry.getKey().equals( serviceTypePointer ) ) {

                found = true;

                for (Map.Entry<Class<? extends ServiceType>, Class<? extends ModularService>> newEntry : additionalModularServiceReferences.backing.entrySet()) {
                    newBacking.put( newEntry.getKey(), newEntry.getValue() );
                }

            }

        }

        if ( ! found ) {
            throw new RuntimeException( String.format( "Could not find index of: %s in %s", serviceTypePointer, backing ) );
        }

        backing = newBacking;

    }


    public String format() {

        // TODO: might want to consider using the TableFormatter

        StringBuilder buff = new StringBuilder();

        for (Map.Entry<Class<? extends ServiceType>, Class<? extends ModularService>> entry : backing.entrySet()) {

            Class<? extends ServiceType> serviceType = entry.getKey();
            Class<? extends ModularService> modularService = entry.getValue();

            buff.append( "    " );
            buff.append( String.format( "%70s = %-70s", serviceType.getName(),  modularService.getName() ) );
            buff.append( "\n" );

        }

        return buff.toString();

    }

}
