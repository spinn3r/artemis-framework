package com.spinn3r.artemis.util.hashcodes;

import com.spinn3r.artemis.util.misc.Base64;
import com.spinn3r.artemis.util.crypto.SHA1;

/**
 *
 */
public class Hashcodes {

    public static String getHashcode( String data ) {

        if ( data == null )
            throw new NullPointerException( "data" );

        return Base64.encode( SHA1.encode( data ) );

    }

}
