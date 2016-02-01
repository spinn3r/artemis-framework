package com.spinn3r.artemis.fluent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class Tuples {

    /**
     * Create a new tuple
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> Tuple<T> tuple( T... values ) {

        Tuple<T> result = new Tuple<>();
        for (T value : values) {
            result.add( value );
        }

        return result;

    }

    public static Str str(String value) {
        return new Str( value );
    }

    public static Str str(Object obj) {
        return new Str( obj );
    }

    /**
     * @Deprecated use tuple() instead.
     */
    public static StringTuple strs(Object... values) {

        List<String> list = new ArrayList<>( values.length );

        for( Object value : values ) {

            if ( value == null ) {
                // really the only things we can do here are to keep the null,
                // by inserting a new null, or we can skip it entirely.
                list.add( null );
            } else {
                list.add( value.toString() );
            }

        }

        return new StringTuple( list );

    }

    public static StringTuple strings( List<String> values ) {
        return new StringTuple( values );
    }

    /**
     * @Deprecated use tuple() instead.
     */
    public static Tuple<Date> dates( Date... values ) {
        return new Tuple<>( values );
    }
}
