package com.spinn3r.artemis.util.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create an iterator over the given collection of objects, that resets itself.
 *
 * This is threadsafe... the one flaw is that on overflow, when the internal pointer
 * resets, we may prematurely miss some of the items and restart iteration again
 * too early.  There is a fix for this by using my own CAS operations but for our
 * usage this is not necessary.
 *
 */
public class CyclicalConcurrentIterator<E> implements Iterator<E> {

    protected final CopyOnWriteArrayList<E> members;

    protected final AtomicInteger position = new AtomicInteger( 0 );

    protected final int size;

    public CyclicalConcurrentIterator(Collection<E> members) {

        // make sure we have no null items
        for (E member : members) {

            if ( member == null ) {
                throw new NullPointerException( "Can not add null members" );
            }

        }

        this.members = new CopyOnWriteArrayList<>( members );
        this.size = members.size();

        if ( this.size == 0 ) {
            throw new RuntimeException( "No members" );
        }

    }

    public E next() {

        int index = computeIndex();

        E result = members.get( index );

        if ( result == null ) {
            throw new IllegalStateException( "Null object found for index: " + index );
        }

        return result;

    }

    @Override
    public boolean hasNext() {
        return true;
    }

    protected int computeIndex() {

        // get the current position by taking the current value and incrementing
        // it if the value is greater than or equal to zero and less than MAX_VALUE
        // to prevent overflow.

        int pos = position.getAndUpdate( value -> value >= 0 && value < Integer.MAX_VALUE ? ++value : 0 );
        return Math.abs( pos % size );

    }

    public int size() {
        return size;
    }

}
