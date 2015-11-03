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

    protected final AtomicInteger position = new AtomicInteger( Integer.MIN_VALUE );

    protected final int size;

    public CyclicalConcurrentIterator(Collection<E> members) {

        this.members = new CopyOnWriteArrayList<>( members );
        this.size = members.size();

        if ( this.size == 0 ) {
            throw new RuntimeException( "No members" );
        }

    }

    public E next() {
        return members.get( computeIndex() );
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    protected int computeIndex() {

        long result = ((long)position.getAndIncrement()) + Integer.MAX_VALUE + 1;

        result = (result % size);

        return (int)result;

    }

    public int size() {
        return size;
    }

}
