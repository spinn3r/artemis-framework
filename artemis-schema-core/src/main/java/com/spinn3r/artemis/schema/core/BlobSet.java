package com.spinn3r.artemis.schema.core;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Set that supports byte arrays.
 */
@SuppressWarnings("serial")
public class BlobSet extends TreeSet<ByteBuffer> implements Set<ByteBuffer> {

    public BlobSet() {
        super( new ByteBufferComparator() );
    }

    public BlobSet( Set<ByteBuffer> set ) {
        super( new ByteBufferComparator() );
        for( ByteBuffer current : set ) {
            this.add( current );
        }

    }

    public void add( byte[] data ) {
        super.add( ByteBuffer.wrap( data ) );
    }

    public boolean contains( byte[] data ) {
        return super.contains( ByteBuffer.wrap( data ) );
    }

    public void remove( byte[] data ) {
        super.remove( ByteBuffer.wrap( data ) );
    }

    public Set<byte[]> toByteArraySet() {

        Set<byte[]> result = new TreeSet<>( new ByteArrayComparator() );

        // create a copy as this is how the older API implemented this.
        for (ByteBuffer current : this) {
            current = current.duplicate();
            byte[] data = new byte[current.remaining()];
            current.get(data);
            result.add(data);
        }

        return result;

    }

    public Set<ByteBuffer> toByteBufferSet() {

        List<ByteBuffer> list = new ArrayList<>( size() );

        // create a copy as this is how the older API implemented this.
        for (ByteBuffer current : this) {
            list.add( current );
        }

        return new CopyOnWriteArraySet<>( list );

    }

}


