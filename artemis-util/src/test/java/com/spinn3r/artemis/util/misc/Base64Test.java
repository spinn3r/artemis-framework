package com.spinn3r.artemis.util.misc;

import com.spinn3r.artemis.util.misc.Base64;
import org.junit.Test;

import static org.junit.Assert.*;
//import static org.hamcrest.Matchers.*;

public class Base64Test {

    @Test
    public void testEncode() throws Exception {

        byte[] data = new byte[1];

        for (int i = -128; i < 127; i++) {

            data[0] = (byte)i;

            String encoded = Base64.encode( data );
            System.out.printf( "%s\n", encoded );

            assertFalse( encoded.contains( "~" ) );
            assertFalse( encoded.contains( "+" ) );
            assertFalse( encoded.contains( "/" ) );
            assertFalse( encoded.contains( "=" ) );

        }

    }

}