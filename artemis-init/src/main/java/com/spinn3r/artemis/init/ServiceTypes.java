package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 */
public class ServiceTypes {

    public static Class<? extends ServiceType> determineServiceType(Class<? extends ServiceType> service) {

        List<Class<? extends ServiceType>> serviceTypes = Lists.newArrayList();

        for (Class<?> inter : service.getInterfaces()) {

            if ( ServiceType.class.isAssignableFrom(inter)) {

                Class<? extends ServiceType> serviceType = inter.asSubclass(ServiceType.class);

                serviceTypes.add(serviceType);
            }

        }

        if ( serviceTypes.size() == 0 ) {

            throw new IllegalArgumentException("No interfaces found for: " + service.getClass().getName());

        } else if ( serviceTypes.size() == 1 ) {

            return serviceTypes.get(0);

        } else {

            throw new IllegalArgumentException("Class implements too many service types: " + service.getClass().getName());

        }

    }

}
