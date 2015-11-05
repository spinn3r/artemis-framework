package com.spinn3r.artemis.init;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class ServiceReferences extends CopyOnWriteArrayList<ServiceReference> {

    public ServiceReferences() {
    }

    public ServiceReferences( ServiceReference... serviceReferences ) {

        for (ServiceReference ref : serviceReferences) {
            add( ref );
        }

    }

    public void add( Class<? extends Service> service ) {
        add( new ServiceReference( service ) );
    }

    public String format() {

        StringBuilder buff = new StringBuilder();

        for (ServiceReference serviceReference : this) {
            buff.append( "    " + serviceReference.toString() + "\n" );
        }

        return buff.toString();

    }

}
