package com.spinn3r.artemis.util.misc;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 *
 */
public class Longs {

    public static long sum( Collection<Long> values ) {

        long result = 0;

        for (long value : values) {
            result += value;
        }

        return result;

    }

    public static double mean( Collection<Long> values ) {

        if ( values.size() == 0 ) {
            return Double.NaN;
        }

        return sum( values ) / values.size();

    }

    public static long median( List<Long> values ) {
        checkState( values.size() > 0 );

        return values.get( values.size() / 2 );

    }

    public static long parse( byte[] b ) {

        //This works by taking each of the bit patterns and converting them to
        //ints taking into account 2s complement and then adding them..

        if ( b.length == 4 ) {

            return (((((long) b[3]) & 0xFF)       )+
                      ((((long) b[2]) & 0xFF) << 8  ) +
                      ((((long) b[1]) & 0xFF) << 16 ) +
                      ((((long) b[0]) & 0xFF) << 24 ));

        } else {

            return (((((long) b[7]) & 0xFF)       ) +
                      ((((long) b[6]) & 0xFF) << 8  ) +
                      ((((long) b[5]) & 0xFF) << 16 ) +
                      ((((long) b[4]) & 0xFF) << 24 ) +
                      ((((long) b[3]) & 0xFF) << 32 ) +
                      ((((long) b[2]) & 0xFF) << 40 ) +
                      ((((long) b[1]) & 0xFF) << 48 ) +
                      ((((long) b[0]) & 0xFF) << 56 ));

        }

    }

    public static Long[] toArray( List<Long> input ) {

        Long[] result = new Long[input.size()];
        return input.toArray(result);

    }


}
