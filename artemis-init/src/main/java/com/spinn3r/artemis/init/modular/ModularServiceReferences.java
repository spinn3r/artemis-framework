package com.spinn3r.artemis.init.modular;

import java.util.LinkedHashMap;

/**
 * Contains a map of ServiceType to ModularService.  This way we can combine
 * ModularServiceReferences by replacing the service type with a new service type.
 * This allows us to swap in testing services replacing production services for
 * mocking and integration.
 */
public class ModularServiceReferences {

    private LinkedHashMap<Class<? extends ServiceType>,Class<? extends ModularService>> backing = new LinkedHashMap<>();

    public void put( Class<? extends ServiceType> serviceType, Class<? extends ModularService> service ) {
        backing.put( serviceType, service );
    }

}
