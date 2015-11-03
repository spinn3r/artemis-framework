package com.spinn3r.artemis.util.crypto;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 *
 */
public class SHA256 {

    public static byte[] encode( String data ) {
        return Hashing.sha256().hashString( data, Charsets.UTF_8 ).asBytes();
    }

    public static byte[] encode( byte[] data ) {
        return Hashing.sha256().hashBytes( data ).asBytes();
    }

}
