package com.spinn3r.artemis.init;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for handling subsystems init and start and stop.
 */
@SuppressWarnings( "serial" )
public class Services extends CopyOnWriteArrayList<Service> {

    public Services(Service... services) {

        for (Service service : services) {
            add( service );
        }

    }

    /**
     * Add the additional services given to be initialized later.
     *
     * @param additional
     */
    public void addAll( Services additional ) {

        for (Service service : additional) {
            add( service );
        }

    }

    public static ServiceReference ref( Class<? extends Service> clazz ) {
        return new ServiceReference( clazz );
    }

    public static Services services( Service... services ) {
        return new Services( services );
    }

}
