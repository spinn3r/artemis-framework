package com.spinn3r.artemis.schema.core;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 */
public class ByteBufferInputStreams {

    public static InputStream toInputStream( ByteBuffer byteBuffer ) {

        if ( byteBuffer.hasArray() ) {
            int length = byteBuffer.limit() - byteBuffer.position();
            return new ByteArrayInputStream( byteBuffer.array(), byteBuffer.position(), length );
        } else {
            return new ByteBufferBackedInputStream( byteBuffer );
        }

    }

}
