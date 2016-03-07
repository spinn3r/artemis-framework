package com.spinn3r.artemis.streams.optionals;

import java.util.OptionalInt;

/**
 *
 */
public class OptionalInts {

    public static OptionalInt parse( String value ) {

        if ( value.matches( "[0-9]+" ) ) {
            return OptionalInt.of( Integer.parseInt( value ) );
        }

        return OptionalInt.empty();

    }

}
