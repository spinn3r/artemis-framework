package com.spinn3r.artemis.schema.core;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * Comparator for two byte arrays.
 */
public class ByteBufferComparator implements Comparator<ByteBuffer> {

    @Override
    public int compare( ByteBuffer v1, ByteBuffer v2 ) {

        if ( v1 == null && v2 != null )
            return -1;

        if ( v1 != null && v2 == null )
            return 1;

        if ( v1 == null && v2 == null )
            return 0;

        return v1.compareTo( v2 );

    }


}
