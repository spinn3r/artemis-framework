package com.spinn3r.artemis.util.misc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class BigIntegers {


    public static long mean( Long... input ) {
        return mean( Arrays.asList( input ) );
    }

    public static long mean( List<Long> input ) {

        BigInteger value = BigInteger.valueOf( 0 );

        for (long current : input) {

            value = value.add( BigInteger.valueOf( current ) );

        }

        return value.divide( BigInteger.valueOf( input.size() ) ).longValue();

    }

}
