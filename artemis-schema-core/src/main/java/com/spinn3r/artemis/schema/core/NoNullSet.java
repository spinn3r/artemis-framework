package com.spinn3r.artemis.schema.core;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * A set used for the purposes of CQL that does not accept null values.  Nulls
 * fail when they are added.
 */
@SuppressWarnings( "serial" )
public class NoNullSet<T> extends LinkedHashSet<T> {

    private static final String ADD_ALL_MSG = "Attempt to addAll when collection contains null. (nulls not accepted).";
    private static final String ADD_MSG = "Null not accepted.";

    @Override
    public boolean add(T t) {

        if ( t == null )
            throw new NullPointerException( ADD_MSG );

        return super.add( t );

    }

    @Override
    public boolean addAll(Collection<? extends T> c) {

        validate( c );

        return super.addAll( c );

    }

    /**
     * Validate that the given collection does not have null values.
     */
    public static void validate( Collection<?> values ) {

        for( Object obj : values ) {

            if ( obj == null )
                throw new NullPointerException( ADD_ALL_MSG );

        }

    }

}
