package com.spinn3r.artemis.schema.core;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Set that supports byte arrays.
 */
@SuppressWarnings("serial")
public class BlobSet extends TreeSet<byte[]> implements Set<byte[]> {

    public BlobSet() {
        super( new ByteArrayComparator() );
    }

    public BlobSet( Set<ByteBuffer> set ) {
        this();
        for( ByteBuffer current : set ) {
            this.add( current.array() );
        }

    }

    public Set<ByteBuffer> toByteBufferSet() {

        List<ByteBuffer> list = new ArrayList<>( size() );

        for (byte[] current : this) {
            list.add( ByteBuffer.wrap( current ) );
        }

        return new CopyOnWriteArraySet<>( list );

    }

}


