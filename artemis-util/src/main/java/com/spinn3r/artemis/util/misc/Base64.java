package com.spinn3r.artemis.util.misc;

import com.google.common.io.BaseEncoding;

/**
 * Frontend to a simple Base64 encoder.
 */
public class Base64 {

    /**
     * Base64 with no padding, and filesafe encoding.
     *
     * @param data
     * @return
     */
    public static String encode( byte[] data ) {
        return BaseEncoding.base64Url().omitPadding().encode( data );
    }

    public static byte[] decode( String data ) {
        return BaseEncoding.base64Url().omitPadding().decode( data );
    }

}
