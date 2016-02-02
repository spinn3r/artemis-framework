package com.spinn3r.artemis.init.modular;

import java.util.LinkedHashMap;
import java.util.Map;

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

    // TODO: we need to be able to support include() of more service...

    public String format() {

        StringBuilder buff = new StringBuilder();

        for (Map.Entry<Class<? extends ServiceType>, Class<? extends ModularService>> entry : backing.entrySet()) {

            Class<? extends ServiceType> serviceType = entry.getKey();
            Class<? extends ModularService> modularService = entry.getValue();

            buff.append( "    " );
            buff.append( String.format( "%s=%s", serviceType.getName(),  modularService.getName() ) );
            buff.append( "\n" );

        }

        return buff.toString();

    }

}
