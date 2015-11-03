package com.spinn3r.artemis.util.crypto;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.spinn3r.artemis.util.misc.Longs;

/**
 *
 */
public class SHA1 {

    private static final int SIZEOF_LONG = 8;

    public static byte[] encode( String data ) {
        return Hashing.sha1().hashString( data, Charsets.UTF_8 ).asBytes();
    }

    public static byte[] encode( byte[] data ) {
        return Hashing.sha1().hashBytes( data ).asBytes();
    }

    public static long encodeAsLong( String data ) {

        byte[] sha1 = encode( data );

        byte[] bytes = new byte[ SIZEOF_LONG ];

        System.arraycopy( sha1, 0, bytes, 0, SIZEOF_LONG );

        return Longs.parse( bytes );

    }

}
