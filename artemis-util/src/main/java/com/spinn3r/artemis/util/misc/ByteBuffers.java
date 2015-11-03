package com.spinn3r.artemis.util.misc;

import com.google.common.base.Charsets;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 */
public class ByteBuffers {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String toHex(ByteBuffer byteBuffer) {

        int length = byteBuffer.limit() - byteBuffer.position();

        StringBuilder sb = new StringBuilder();

        for ( int j = 0; j < length; j++ ) {

            byte b = byteBuffer.get( byteBuffer.position() + j );
            sb.append( String.format( "0x%02x ", b & 0xff ) );

        }

        return sb.toString().trim();

    }

    public static String toString( ByteBuffer byteBuffer ) {
        int length = byteBuffer.limit() - byteBuffer.position();
        return new String( byteBuffer.array(), byteBuffer.position(), length, Charsets.UTF_8 );
    }

}
