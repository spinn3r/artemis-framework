package com.spinn3r.artemis.util.crypto;

import com.spinn3r.artemis.util.misc.Base64;

/**
 * Used so that we can take a value / tag and generate a blackbox token which
 * can be used externally.  We use these for various things like vendor codes
 * and assigned tags for sources.  We have to use a salt so that people can't
 * try to brute force test for various values (like competitor vendor codes).
 *
 */
public class TokenGenerator {

    protected static String SALT = "8mbnpmwkxnh";

    /**
     * Generate an auth token from the vendor code.
     *
     */
    public static String generate(String value) {
        return Base64.encode( SHA1.encode( SALT + ":" + value ) );
    }

}
