package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.time.sequence.SequenceReference;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contains a list/set of ServiceReferences. Unlike a set the references
 * are ordered but we do not allow identical services to be added twice.
 */
public class ServiceReferences {

    private List<ServiceReference> delegate = new CopyOnWriteArrayList<>();

    public ServiceReferences() {
    }

    public ServiceReferences( ServiceReference... serviceReferences ) {

        for (ServiceReference ref : serviceReferences) {
            delegate.add( ref );
        }

    }

    public void include( ServiceReference pointer, ServiceReference... additionalServiceReferences ) {
        include( pointer, Lists.newArrayList( additionalServiceReferences ) );
    }

    /**
     * Add additional services after the given pointer.  We DO NOT add services
     * if they are already in the index.
     */
    public void include( ServiceReference pointer, List<ServiceReference> additionalServiceReferences ) {

        int index = indexOf( pointer );

        if (index == -1 ) {
            throw new RuntimeException( String.format( "Could not find index of: %s in %s", pointer, delegate ) );
        }

        int newServiceIndex = index + 1;

        for (ServiceReference additionalServiceReference : additionalServiceReferences) {

            if ( delegate.contains( additionalServiceReference ) )
                continue;

            delegate.add( newServiceIndex++, additionalServiceReference );

        }

    }

    /**
     * Return true if we already contain this sequence reference.
     *
     */
    public boolean contains( ServiceReference serviceReference ) {
        return delegate.contains( serviceReference );
    }

    public ServiceReferences add( ServiceReference serviceReference ) {

        if ( delegate.contains( serviceReference ) )
            return this;

        delegate.add( serviceReference );

        return this;

    }

    public ServiceReferences add( int index, ServiceReference serviceReference ) {
        delegate.add( index, serviceReference );
        return this;
    }

    public ServiceReferences add( Class<? extends Service> service ) {
        add( new ServiceReference( service ) );
        return this;
    }

    public String format() {

        StringBuilder buff = new StringBuilder();

        for (ServiceReference serviceReference : delegate) {
            buff.append( "    " );
            buff.append( serviceReference.toString() );
            buff.append( "\n" );
        }

        return buff.toString();

    }

    public int size() {
        return delegate.size();
    }

    public ServiceReference get( int index ) {
        return delegate.get( index );
    }

    public int indexOf( ServiceReference serviceReference ) {
        return delegate.indexOf( serviceReference );
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
